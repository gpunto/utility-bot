package dev.gianmarcodavid.telegram.holiday

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Inject
class HolidayHandler(
    private val holidayApi: HolidayApi,
    private val mapper: HolidayMapper,
) : CommandHandler {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

    override val description: String = "Get holidays for a country"

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text)

        return if (args.isEmpty()) {
            Reply("Usage: ".regular(), "/$COMMAND [country code]".code())
        } else {
            val now = LocalDate.now()
            val holidays = holidayApi.getHolidays(year = now.year, countryCode = args[0])
                .map(mapper::map)

            val nextHolidays = holidays
                .dropWhile { it.date <= now }
                .take(3)

            when {
                holidays.isEmpty() -> Reply("No holidays found")
                nextHolidays.isEmpty() -> Reply("No more holidays this year")
                else -> Reply(
                    "The next holidays are: \n",
                    nextHolidays.joinToString("\n", transform = { it.formatted() })
                )
            }
        }
    }

    private fun Holiday.formatted(): String {
        val formattedDate = dateTimeFormatter.format(date)

        return if (global || counties.isEmpty()) "• $name on $formattedDate"
        else "• $name on $formattedDate (only in ${counties.joinToString()})"
    }

    companion object {
        const val COMMAND = "holidays"
    }
}
