package global.genesis

import com.google.inject.Inject
import global.genesis.gen.dao.Trade
import global.genesis.gen.dao.description.TradeDescription
import global.genesis.gen.dao.repository.TradeAsyncRepository
import global.genesis.session.AuthCacheFactory
import global.genesis.session.MasterAuthCache
import global.genesis.testsupport.dataserver.DataServerMsg
import global.genesis.testsupport.dataserver.DataServerTest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList

class AlphaDataServerTradeTest : DataServerTest<Trade>(
    genesisHome = "/GenesisHome/",
    scriptFileName = "alpha-dataserver.kts",
    initialDataFile = "data/TEST_DATA.csv",
    mapperBuilder = TradeDescription::buildRowMapper,
) {
    private lateinit var authCache: MasterAuthCache

    @Inject
    private lateinit var tableRepo: TradeAsyncRepository

    @Before
    fun prepareMap() {
        authCache = AuthCacheFactory.newWriter("ENTITY_VISIBILITY", rxDb.updateQueue)
    }

    @After
    fun cleanMap() {
        (authCache.toMap() as MutableMap<String, Set<String>>).clear()
    }

    /*
    @Test
    fun `test first trade`() = runBlocking(coroutineContext) {
        authCache.publish("00000000001TRSP0", "JaneDee", true)
        authCache.publish("00000000002TRSP0", "JaneDee", true)
        authCache.publish("00000000003TRSP0", "JaneDee", true)
        authCache.publish("00000000004TRSP0", "JaneDee", true)
        authCache.publish("00000000005TRSP0", "JaneDee", true)

        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<Trade>>()
        val updateJob = launch {
            dataLogon("ALL_PRICES", 10)
                .filterIsInstance<DataServerMsg.QueryUpdate<Trade>>()
                .collect { updates.add(it) }
        }

        val trades = tableRepo.getBulk().toList()

        await untilCallTo { updates } has { size == 1 }

        val rows: List<Trade> = updates.first().rows

        assert(rows.map { it.tradeId }.sorted() == trades.map { it.tradeId }.sorted())

        updateJob.cancel()
    }

     */
}
