package dev.gianmarcodavid.telegram.finance

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject
import kotlin.math.pow

@Inject
class FinanceCommandHandler : CommandHandler {
    override val description: String = "Finance calculator"

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text)

        return when (args.firstOrNull()?.lowercase()) {
            "compound" -> compound(args.drop(1))
            "fire", "🔥" -> fire(args.drop(1))
            else -> Reply("Usage: ".regular(), "/$COMMAND [subcommand] [args]".code())
        }
    }

    private fun compound(args: List<String>): Reply {
        if (args.size != 3) {
            return Reply(
                "Usage: ".regular(),
                "/$COMMAND compound [principal] [interest] [years]".code()
            )
        }
        val argPrincipal = args[0]
        val argRate = args[1]
        val argYears = args[2]

        val principal = argPrincipal.toMoneyOrNull()
        val rate = argRate.toPercentOrNull()
        val years = argYears.toIntOrNull()?.takeIf { it > 0 }

        if (principal == null) return invalidArgument(
            name = "principal",
            value = argPrincipal,
            description = "It should be a number with or without currency. Examples: 100€, 2500."
        )
        if (rate == null) return invalidArgument(
            name = "rate",
            value = argRate,
            description = "It should be a percentage or a positive number. Examples: 0.04, 15%"
        )
        if (years == null) return invalidArgument(
            name = "years",
            value = argYears,
            description = "It should be a positive integer. Examples: 5, 20."
        )

        val amount = principal * (1 + rate.value).pow(years)

        return Reply(
            ("After $years years, at a rate of ${rate.formatted()}, " +
                "${principal.formatted()} become ${amount.formatted()}").regular()
        )
    }

    private fun fire(args: List<String>): Reply {
        return when (args.size) {
            1 -> fire("4%", args[0])
            2 -> fire(args[0], args[1])
            else -> Reply(
                "Usage: ".regular(),
                "/$COMMAND fire [safe withdrawal rate] [expenses]".code()
            )
        }
    }

    private fun fire(argRate: String, argExpenses: String): Reply {
        val rate = argRate.toPercentOrNull()
        val expenses = argExpenses.toMoneyOrNull()

        if (rate == null) return invalidArgument(
            name = "safe withdrawal rate",
            value = argRate,
            description = "It should be a percentage or a positive number. Examples: 0.04, 3%"
        )

        if (expenses == null) return invalidArgument(
            name = "expenses",
            value = argExpenses,
            description = "It should be a number with or without currency. Examples: 8000€, 20000."
        )

        val amount = expenses / rate.value

        return Reply(
            "To cover ${expenses.formatted()} with a safe withdrawal rate of ${argRate}, you need ${amount.formatted()}"
        )
    }

    private fun invalidArgument(name: String, value: String, description: String): Reply =
        Reply("Invalid ".regular(), name.code(), " argument '$value'. $description".regular())

    companion object {
        const val COMMAND = "finance"
    }
}

private fun String.toPercentOrNull(): Percent? {
    if (endsWith("%")) {
        return dropLast(1).toDoubleOrNull()?.let { Percent(it / 100) } ?: return null
    }

    return Percent(toDoubleOrNull() ?: return null)
}

private fun String.toMoneyOrNull(): Money? {
    val match = Regex("(\\D*)(\\d+(?:\\.\\d+)?)(\\D*)").matchEntire(this) ?: return null

    val amount = match.groupValues[2].toDoubleOrNull() ?: return null

    match.groupValues[1].takeUnless(String::isEmpty)?.let { currency ->
        return Money(amount, currency, post = false)
    }
    match.groupValues[3].takeUnless(String::isEmpty).let { currency ->
        return Money(amount, currency ?: "€", post = true)
    }
}
