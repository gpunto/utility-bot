package dev.gianmarcodavid.telegram.math

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.buildReply
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject
import kotlin.random.Random

@Inject
class RandomCommandHandler : CommandHandler {
    override val description: String = "Utilities for random numbers"

    private val subcommands = listOf(
        SubCommand(
            name = "help",
            aliases = listOf("h"),
            description = "Provides information about other subcommands",
            usage = "help [subcommand]",
            handler = ::help
        ),
        SubCommand(
            name = "number",
            aliases = listOf("num"),
            description = "Generates a random number",
            usage = "number [min] [max]",
            handler = ::randomNumber
        ),
        SubCommand(
            name = "choice",
            aliases = emptyList(),
            description = "Chooses a random element",
            usage = "choice [element1], [element2], …",
            handler = ::randomChoice
        )
    )
    private val subcommandsMap = buildMap {
        subcommands.forEach { subCommand ->
            put(subCommand.name, subCommand)
            subCommand.aliases.forEach { alias -> put(alias, subCommand) }
        }
    }

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text)

        return args.getOrNull(0)
            ?.let(subcommandsMap::get)
            ?.handler?.invoke(args.drop(1))
            ?: buildReply {
                add("Usage: ".regular())
                add("/$COMMAND [subcommand] [args]".code())
                add("\n\nAvailable subcommands: ".regular())
                subcommands.forEachIndexed { index, subCommand ->
                    add(subCommand.name.code())
                    if (index < subcommands.lastIndex) add(", ".regular())
                }
            }
    }

    private fun help(args: List<String>): Reply {
        val arg = args.getOrNull(0) ?: return Reply("Usage: ".regular(), "/$COMMAND help [subcommand]".code())

        return subcommandsMap[arg]?.let { subCommand ->
            Reply(
                subCommand.name.code(), " - ${subCommand.description}".regular(),
                "\n\nUsage: ".regular(), "/$COMMAND ".code(), subCommand.usage.code()
            )
        } ?: Reply("Unknown subcommand: '$arg'".regular())
    }

    private fun randomNumber(args: List<String>): Reply {
        val arg1 = args.getOrNull(0)
        val arg2 = args.getOrNull(1)

        var min: Int
        var max: Int

        if (arg1 != null && arg2 != null) {
            min = arg1.toIntOrNull() ?: return Reply("Invalid number: '$arg1'")
            max = arg2.toIntOrNull() ?: return Reply("Invalid number: '$arg2'")
        } else if (arg1 != null) {
            max = arg1.toIntOrNull() ?: return Reply("Invalid number: '$arg1'")
            min = if (max > 0) 1 else 0
        } else {
            min = 1
            max = 100
        }

        if (min > max) {
            val temp = min
            min = max
            max = temp
        }

        return Reply(
            "Random number from $min to $max: ".regular(),
            Random.nextInt(min, max + 1).toString().bold()
        )
    }

    private fun randomChoice(args: List<String>): Reply = if (args.size <= 1) {
        Reply("Usage: ".regular(), "/$COMMAND choice [element1], [element2], …".code())
    } else {
        Reply("I choose: ".regular(), args.random().bold())
    }

    companion object {
        const val COMMAND = "rng"
    }
}

private class SubCommand(
    val name: String,
    val aliases: List<String>,
    val description: String,
    val usage: String,
    val handler: suspend (List<String>) -> Reply
)