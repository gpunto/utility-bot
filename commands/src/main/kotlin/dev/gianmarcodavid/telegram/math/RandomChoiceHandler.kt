package dev.gianmarcodavid.telegram.math

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject

@Inject
class RandomChoiceHandler : CommandHandler {
    override val description: String = "Select a random element from a list"

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text, ArgumentParser.Separator.commaSpace)

        return if (args.isEmpty()) {
            Reply("Usage: ".regular(), "/$COMMAND [element1], [element2], …".code())
        } else {
            Reply("Random choice: ".regular(), args.random().bold())
        }
    }

    companion object {
        const val COMMAND = "randchoice"
    }
}
