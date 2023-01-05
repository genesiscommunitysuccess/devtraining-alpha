package global.genesis

import com.google.inject.Inject
import global.genesis.commons.model.GenesisSet
import global.genesis.db.DbRecord
import global.genesis.db.entity.*
import global.genesis.db.query.api.reading.DataTypeConverter
import global.genesis.db.query.api.reading.DbRecordReader
import global.genesis.db.query.api.reading.GenesisSetReader
import global.genesis.db.query.api.reading.ValueReader
import global.genesis.db.query.api.rowmapper.RowMapper
import global.genesis.dictionary.pal.TableField
import global.genesis.gen.config.tables.TRADE
import global.genesis.gen.dao.Trade
import global.genesis.gen.dao.description.TradeDescription
import global.genesis.gen.dao.enums.Direction
import global.genesis.gen.dao.enums.TradeStatus
import global.genesis.gen.dao.repository.TradeAsyncRepository
import global.genesis.testsupport.dataserver.DataServerMsg
import global.genesis.testsupport.dataserver.DataServerTest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.Test
import java.io.Serializable
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

class AlphaDataServerTradeTest : DataServerTest<Trade>(
    genesisHome = "/GenesisHome/",
    scriptFileName = "alpha-dataserver.kts",
    initialDataFile = "data/TEST_DATA.csv",
    mapperBuilder = AlphaDataServerTradeTestDescription::buildRowMapper,
    authCacheOverride = "ENTITY_VISIBILITY"
) {

    @Inject
    private lateinit var tableRepo: TradeAsyncRepository

    @Test
    fun `test usd trades only`() = runBlocking(coroutineContext) {
        val updates = CopyOnWriteArrayList<DataServerMsg.QueryUpdate<Trade>>()
        val updateJob = launch {
            dataLogon("ALL_PRICES", 4)
                .filterIsInstance<DataServerMsg.QueryUpdate<Trade>>()
                .collect { updates.add(it) }
        }

        val trades = tableRepo.getBulk().toList()

        //await.atMost(5, TimeUnit.MINUTES) untilCallTo { updates } has { size == 4 }
        //await untilCallTo { updates } has { size == 4 }
        await untilCallTo { updates } has { size == 4 }
        //await untilCallTo { updates } has { count() == 1 }
        //await untilCallTo { updates.count() } matches { count -> count == 1 }

        val rows: List<Trade> = updates.first().rows

        assert(rows.map { it.price }.sorted() == trades.map { it.price }.sorted())

        updateJob.cancel()
    }

    object AlphaDataServerTradeTestDescription : Serializable, EntityDescription<Trade> {
        public override val tableName: String = "TRADE"

        private val dbRecordReader: TradeRowMapper<DbRecord> = TradeRowMapper(DbRecordReader)

        private val genesisSetReader: TradeRowMapper<GenesisSet> = TradeRowMapper(GenesisSetReader)

        public override val fieldToPropertyMap: Map<String, KMutableProperty1<Trade, *>> = mapOf(
            "TRADE_ID" to GenesisGeneratedKProperty(Trade::tradeIdOrNull, Trade::tradeId),
            "INSTRUMENT_ID" to Trade::instrumentId,
            "PRICE" to Trade::price,
            "SYMBOL" to Trade::symbol,
        )

        public override val primaryKey: UniqueEntityIndexReference<Trade>
            get() = Trade.ById

        public override val allIndexes: List<EntityIndexReference<Trade>> = listOf(
            Trade.ById
        )

        public override val fields: List<TableField<TRADE, *>> = listOf(
            TRADE.TRADE_ID,
            TRADE.INSTRUMENT_ID,
            TRADE.PRICE,
            TRADE.SYMBOL,
        )

        public override val entityClass: KClass<Trade> = Trade::class

        public override fun <I> buildRowMapper(valueReader: ValueReader<I>): RowMapper<I, Trade> =
            TradeRowMapper(valueReader)

        public override fun toDbRecord(entity: Trade): DbRecord {
            val dbRecord = DbRecord("TRADE")
            if (entity.isTradeIdInitialised) {
                dbRecord.setString("TRADE_ID", entity.tradeId)
            }
            dbRecord.setString("INSTRUMENT_ID", entity.instrumentId)
            dbRecord.setDouble("PRICE", entity.price)
            dbRecord.setString("SYMBOL", entity.symbol)
            return dbRecord
        }

        public override fun toDbRecordKeyFieldsOnly(entity: Trade): DbRecord {
            val dbRecord = DbRecord("TRADE")
            if (entity.isTradeIdInitialised) {
                dbRecord.setString("TRADE_ID", entity.tradeId)
            }
            dbRecord.setString("INSTRUMENT_ID", entity.instrumentId)
            dbRecord.setDouble("PRICE", entity.price)
            return dbRecord
        }

        public override fun fromDbRecord(dbRecord: DbRecord): Trade = try {
            dbRecordReader.mapRow(dbRecord)
        } catch (e: Exception) {
            throw EntityBuildingException(
                """
        |Error building entity from:
        |""".trimMargin() + dbRecord.toString(), e
            )
        }

        public override fun fromGenesisSet(genesisSet: GenesisSet): Trade = try {
            genesisSetReader.mapRow(genesisSet)
        } catch (e: Exception) {
            throw EntityBuildingException(
                """
        |Error building entity from:
        |""".trimMargin() + genesisSet.toString(), e
            )
        }

        public class TradeRowMapper<I>(
            private val valueReader: ValueReader<I>,
        ) : RowMapper<I, Trade> {
            public override fun mapRow(input: I): Trade {
                val trade = Trade {
                    valueReader.readNullableValue(input, String::class, "TRADE_ID")?.let { tradeId = it }
                    instrumentId = valueReader.value(input, "INSTRUMENT_ID")
                    price = valueReader.value(input, "PRICE")
                    symbol = valueReader.nullableValue(input, "SYMBOL")
                }
                return trade
            }
        }
    }
}

