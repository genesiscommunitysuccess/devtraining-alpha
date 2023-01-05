package global.genesis

import com.google.inject.Inject
import global.genesis.gen.view.entity.TradeView
import global.genesis.gen.view.repository.TradeViewRx3Repository
import global.genesis.testsupport.dataserver.DataServerMsg
import global.genesis.testsupport.dataserver.DataServerTest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx3.await
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList

class AlphaDataServerTradeViewTest : DataServerTest<TradeView>(
    genesisHome = "/GenesisHome/",
    scriptFileName = "alpha-dataserver.kts",
    initialDataFile = "data/TEST_DATA.csv",
    mapperBuilder = TradeView::buildRowMapper,
    authCacheOverride = "ENTITY_VISIBILITY"
) {
    @Inject
    lateinit var repo: TradeViewRx3Repository

    @Test
    fun `test ALL_TRADES trades`() = runBlocking(coroutineContext) {
        authorise("ENTITY_VISIBILITY", "2", "JaneDee")

        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<TradeView>>()
        val updateJob = launch {
            dataLogon("ALL_TRADES", 5)
                .filterIsInstance<DataServerMsg.QueryUpdate<TradeView>>()
                .collect { updates.add(it) }
        }

        val trades = repo.getBulk().toList().await()

        await untilCallTo { updates } has { size == 1 }

        val rows = updates.first().rows

        //assert(rows.map { it.tradeId }.sorted() == trades.map { it.tradeId }.sorted())

        updateJob.cancel()
    }
}

