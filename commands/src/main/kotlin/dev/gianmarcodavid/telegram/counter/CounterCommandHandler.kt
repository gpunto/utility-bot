package dev.gianmarcodavid.telegram.counter

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.regular
import dev.gianmarcodavid.telegram.database.CounterQueries
import me.tatarka.inject.annotations.Inject
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Inject
class CounterCommandHandler(private val counterQueries: CounterQueries) : CommandHandler {
    override val description: String = "Translate text"

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text)

        return when (args.getOrNull(0)) {
            "add" -> addCounter(args.drop(1))
            else -> getCount(args)
        }
    }

    private suspend fun addCounter(args: List<String>): Reply {
        val argName = args.getOrNull(0) ?: return TODO
        val argDate = args.getOrNull(1) ?: return TODO

        val date = argDate.toInstantOrNull() ?: return TODO

        // todo correct IDs

        val existing = counterQueries.selectOne(name = argName, ownerId = 0).executeAsOneOrNull()
        if (existing != null) return TODO

        counterQueries.insert(name = argName, date = date.toEpochMilli(), ownerId = 0, chatId = 0)

        return Reply("Added counter with name $argName and date $argDate".regular())
    }

    private suspend fun getCount(args: List<String>): Reply {
        val argName = args.getOrNull(0) ?: return TODO

        val counter = counterQueries.selectOne(name = argName, ownerId = 0).executeAsOneOrNull() ?: return TODO
        val time = Instant.ofEpochMilli(counter.date)
        val now = Instant.now()
        val days = time.until(now, ChronoUnit.DAYS)
        val hours = time.until(now, ChronoUnit.HOURS) % 24

        val formattedTime = listOfNotNull(
            if (days > 0) "${days}d" else null,
            if (hours > 0) "${hours}h" else null
        ).joinToString(" ")

        return Reply("$argName: $formattedTime".regular())
    }

    // todo
    //  - list
    //  - delete

    companion object {
        const val COMMAND = "since"

        // todo
        private val TODO = Reply(
            "TODO".regular(),
        )
    }
}

private fun String.toInstantOrNull(): Instant? = when (this) {
    "today" -> Instant.now()
    "yesterday" -> Instant.now().minus(1, ChronoUnit.DAYS)
    "tomorrow" -> Instant.now().plus(1, ChronoUnit.DAYS)
    else ->
        if (matches(dateRegex)) dateFormatter.parse(this, Instant::from)
        else null
}

private val dateRegex = Regex("""\d{2}/\d{2}/\d{4}""")
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
