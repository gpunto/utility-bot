package dev.gianmarcodavid.telegram.holiday

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApi {
    @GET("https://date.nager.at/api/v3/publicholidays/{year}/{countryCode}")
    suspend fun getHolidays(@Path("year") year: Int, @Path("countryCode") countryCode: String): List<HolidayDto>
}

@Serializable
data class HolidayDto(
    val date: String,
    val name: String,
    val countryCode: String,
    val global: Boolean,
    val counties: List<String>?,
)
