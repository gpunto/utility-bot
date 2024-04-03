package dev.gianmarcodavid.telegram.holiday

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter

@Inject
class HolidayHandler(
    private val repository: HolidaysRepository,
) : CommandHandler {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

    override val description: String = "Get the next holidays for a country"

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text)

        return if (args.isEmpty()) {
            Reply("Usage: ".regular(), "/$COMMAND [country code] [how many]".code())
        } else {
            val countArg = args.getOrNull(1)
            val count = countArg?.toIntOrNull()

            if (countArg != null && (count == null || count <= 0)) {
                Reply(
                    "Invalid ".regular(),
                    "how many".code(),
                    " argument '$countArg'. It should be a positive integer.".regular()
                )
            } else {
                fetchHolidays(args[0], count = count ?: 3)
            }
        }
    }

    private suspend fun fetchHolidays(countryCode: String, count: Int): Reply {
        val nextHolidays = buildList {
            addAll(
                repository.fetchHolidays(
                    countryCode = countryCode,
                    startingFrom = LocalDate.now(),
                    limit = count
                )
            )

            if (size < count) addAll(
                repository.fetchHolidays(
                    countryCode = countryCode,
                    startingFrom = Year.now().plusYears(1).atDay(1),
                    limit = count - size
                )
            )
        }

        return if (nextHolidays.isEmpty()) {
            Reply("No holidays found")
        } else {
            Reply(
                "The next holidays are: \n",
                nextHolidays.joinToString("\n", transform = { it.formatted() }),
            )
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
