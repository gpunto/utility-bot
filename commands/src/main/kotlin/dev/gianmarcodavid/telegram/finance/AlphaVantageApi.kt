package dev.gianmarcodavid.telegram.finance

import dev.gianmarcodavid.telegram.foundation_api.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface AlphaVantageApi {
    @GET("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&apikey=${BuildConfig.ALPHA_VANTAGE}")
    suspend fun getQuote(@Query("symbol") symbol: String): StockQuoteDto
}

@Serializable
class StockQuoteDto(
    @SerialName("Global Quote")
    val globalQuote: GlobalQuoteDto
)

@Serializable
data class GlobalQuoteDto(
    @SerialName("01. symbol")
    val symbol: String? = null,
    @SerialName("05. price")
    val price: String? = null,
    @SerialName("10. change percent")
    val changePercent: String? = null,
)