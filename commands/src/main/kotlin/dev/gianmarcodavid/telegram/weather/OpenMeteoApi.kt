package dev.gianmarcodavid.telegram.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("https://geocoding-api.open-meteo.com/v1/search?count=1&language=en&format=json")
    suspend fun getCity(@Query("name") name: String): GetCityResponseDto

    @GET("forecast?current=temperature_2m,apparent_temperature,weather_code")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): GetWeatherResponseDto
}

@Serializable
data class GetCityResponseDto(val results: List<CityDto>? = null)

@Serializable
data class CityDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class GetWeatherResponseDto(
    val current: CurrentWeatherDto,
    @SerialName("current_units")
    val currentUnits: UnitsDto
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("temperature_2m")
    val temperature2m: Double,
    @SerialName("apparent_temperature")
    val apparentTemperature: Double,
    @SerialName("weather_code")
    val weatherCode: Int,
)

@Serializable
data class UnitsDto(
    @SerialName("temperature_2m")
    val temperature2m: String,
    @SerialName("apparent_temperature")
    val apparentTemperature: String,
)