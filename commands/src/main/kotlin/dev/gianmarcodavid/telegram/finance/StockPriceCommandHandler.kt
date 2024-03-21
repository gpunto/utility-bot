package dev.gianmarcodavid.telegram.finance

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject

@Inject
class StockPriceCommandHandler(private val api: AlphaVantageApi) : CommandHandler {
    override val description: String = "Get the current price of a stock"

    override suspend fun handle(text: String): Reply {
        if (text.isEmpty()) {
            return Reply("Usage: ".regular(), "/$COMMAND [symbol]".code())
        }

        val quote = api.getQuote(text).globalQuote

        if (quote.isEmpty()) {
            return Reply("No data found for '$text'".regular())
        }

        return Reply(
            "The current price of ${quote.symbol} is ${quote.price}, daily change: ${quote.changePercent}"
        )
    }

    companion object {
        const val COMMAND = "stock"
    }
}

private fun GlobalQuoteDto.isEmpty() = symbol == null || price == null || changePercent == null
