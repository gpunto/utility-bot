package dev.gianmarcodavid.telegram.weather

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject

@Inject
class WeatherCommandHandler(
    private val api: OpenMeteoApi,
) : CommandHandler {
    override val description = DESCRIPTION

    override suspend fun handle(text: String): Reply =
        if (text.isEmpty()) {
            Reply("Usage: ".regular(), "/$COMMAND CityName".code())
        } else {
            val cities = api.getCity(text).results

            if (cities.isNullOrEmpty()) {
                Reply("City '$text' not found".regular())
            } else {
                val city = cities.first()
                val weather = api.getWeather(city.latitude, city.longitude)

                weather.run {
                    val temperature = "${current.temperature2m}${currentUnits.temperature2m}"
                    val apparent = "${current.apparentTemperature}${currentUnits.apparentTemperature}"
                    val emoji = wmoCodeToEmoji(current.weatherCode)

                    Reply(
                        city.name.bold(),
                        ": $emoji $temperature (feels like $apparent)".regular()
                    )
                }
            }
        }

    companion object {
        const val COMMAND = "weather"
        const val DESCRIPTION = "Get the weather for a city"
    }
}

private fun wmoCodeToEmoji(wmoCode: Int): String = when (wmoCode) {
    0 -> "☀️"
    1 -> "🌤️"
    2 -> "⛅"
    3 -> "🌥️"
    4 -> "☁️"
    10 -> "🌧️"
    11 -> "🌧️"
    12 -> "🌦️"
    18 -> "🌧️"
    19 -> "🌧️"
    20 -> "🌦️"
    21 -> "🌦️"
    22 -> "🌦️"
    23 -> "🌧️"
    24 -> "🌧️"
    29 -> "🌧️"
    30 -> "❄️"
    31 -> "❄️"
    32 -> "🌨️"
    33 -> "🌨️"
    34 -> "❄️"
    35 -> "❄️"
    40 -> "🌩️"
    41 -> "🌩️"
    42 -> "🌩️"
    else -> "❓"
}
