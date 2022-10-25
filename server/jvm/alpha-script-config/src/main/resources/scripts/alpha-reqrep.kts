/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide request reply config for multi-pro-code-test.
 *
 * Modification History
 */
requestReplies {
    requestReply("INSTRUMENT_MARKET_DATA", INSTRUMENT_PRICE) {

        request {
            INSTRUMENT_ID
        }

        reply {
            INSTRUMENT_PRICE.INSTRUMENT_ID
            INSTRUMENT_PRICE.LAST_PRICE
        }

        where { row, parameters ->
            row.instrumentId.equals(parameters.getString("INSTRUMENT_ID"))
        }
    }
}
