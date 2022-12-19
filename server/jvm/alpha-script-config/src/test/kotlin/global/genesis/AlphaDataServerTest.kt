/*
package global.genesis

import com.google.inject.Inject
import global.genesis.dictionary.GenesisDictionary
import global.genesis.gen.view.entity.TradeView
import global.genesis.gen.view.repository.TradeViewRx3Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx3.await
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList
import global.genesis.dataserver.pal.

class AlphaDataServerTest : DataServerTest<TradeView>(
    genesisHome = "/genesisHome/",
    scriptFileName = "genesis-range-dataserver.kts",
    initialDataFile = "/SystemTest/simple-data-load-range.csv",
    mapperBuilder = TradeView::buildRowMapper,
) {
    override fun createDictionary(): GenesisDictionary =
        testDictionaries("genesisHome/generated/cfg/genesis-tables-dictionary.kts")

    @Inject
    lateinit var repo: TradeViewRx3Repository

    @Test
    fun `test usd trades only`() = runBlocking(coroutineContext) {
        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<TradeView>>()
        val updateJob = launch {
            dataLogon("TRADE_VIEW_RANGED_TRADE_RANGE_USD", 10)
                .filterIsInstance<DataServerMsg.QueryUpdate<TradeView>>()
                .collect { updates.add(it) }
        }

        val usdTrades = repo.getRange(TradeView.ByCurrencyId("USD"), 1)
            .toList()
            .await()

        await untilCallTo { updates } has { size == 1 }

        val rows = updates.first().rows

        assert(rows.map { it.tradeId }.sorted() == usdTrades.map { it.tradeId }.sorted())

        updateJob.cancel()
    }

    @Test
    fun `test qty range trades`() = runBlocking(coroutineContext) {
        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<TradeView>>()
        val updateJob = launch {
            dataLogon("TRADE_VIEW_RANGED_TRADE_RANGE_QTY", 10)
                .filterIsInstance<DataServerMsg.QueryUpdate<TradeView>>()
                .collect { updates.add(it) }
        }

        val qtyRangeTrades = repo.getBulk()
            .filter { it.tradeQuantity in 101..999 }
            .toList()
            .await()

        await untilCallTo { updates } has { size == 1 }

        val rows = updates.first().rows

        assert(rows.map { it.tradeId }.sorted() == qtyRangeTrades.map { it.tradeId }.sorted())

        updateJob.cancel()
    }
}
*/

