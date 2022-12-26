package global.genesis

import com.google.inject.Inject
import global.genesis.gen.view.entity.TradeView
import global.genesis.gen.view.repository.TradeViewRx3Repository
import global.genesis.session.MasterAuthCache
import global.genesis.testsupport.dataserver.DataServerMsg
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx3.await
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.Test
import org.junit.Before
import org.junit.After
import java.util.concurrent.CopyOnWriteArrayList
import global.genesis.testsupport.dataserver.DataServerTest
import global.genesis.session.AuthCache.Companion.newWriter
import global.genesis.session.AuthCacheFactory

class AlphaDataServerTradeViewTest : DataServerTest<TradeView>(
    genesisHome = "/GenesisHome/",
    scriptFileName = "alpha-dataserver.kts",
    initialDataFile = "data/TEST_DATA.csv",
    mapperBuilder = TradeView::buildRowMapper,
) {
    private lateinit var authCache: MasterAuthCache

    @Inject
    lateinit var repo: TradeViewRx3Repository

    @Before
    fun prepareMap() {
        authCache = AuthCacheFactory.newWriter("ENTITY_VISIBILITY", rxDb.updateQueue)
    }

    @After
    fun cleanMap() {
        (authCache.toMap() as MutableMap<String, Set<String>>).clear()
    }

    @Test
    fun `test all trades`() = runBlocking(coroutineContext) {
        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<TradeView>>()
        val updateJob = launch {
            dataLogon("ALL_TRADES", 10)
                .filterIsInstance<DataServerMsg.QueryUpdate<TradeView>>()
                .collect { updates.add(it) }
        }

        val allTrades = repo.getBulk()
            .toList()
            .await()

        await untilCallTo { updates } has { size == 4 }

        val rows = updates.first().rows

        assert(rows.map { it.tradeId }.sorted() == allTrades.map { it.tradeId }.sorted())

        updateJob.cancel()
    }
}
