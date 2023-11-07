package global.genesis

import global.genesis.gen.dao.Trade
import global.genesis.gen.dao.description.TradeDescription
import global.genesis.testsupport.dataserver.DataServerMsg
import global.genesis.testsupport.dataserver.DataServerTest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Test
import java.util.concurrent.CopyOnWriteArrayList

class AlphaDataServerTradeTest : DataServerTest<Trade>(
    genesisHome = "/GenesisHome/",
    scriptFileName = "alpha-dataserver.kts",
    initialDataFile = "data/TEST_DATA.csv",
    mapperBuilder = TradeDescription::buildRowMapper,
    authCacheOverride = "ENTITY_VISIBILITY"
) {

    @Test
    fun `test ALL_PRICES trades`() = runBlocking(coroutineContext) {
        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<Trade>>()
        val updateJob = launch {
            dataLogon("ALL_PRICES", 5)
                .filterIsInstance<DataServerMsg.QueryUpdate<Trade>>()
                .collect { updates.add(it) }
        }

        val trades = entityDb.getBulk<Trade>().take(4).toList()

        await.until { updates.size == 1 }

        val rows: List<Trade> = updates.first().rows

        assert(rows.map { it.price }.sorted() == trades.map { it.price }.sorted())

        updateJob.cancel()
    }
}

