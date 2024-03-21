package dev.gianmarcodavid.telegram.math

import dev.gianmarcodavid.telegram.command.ArgumentParser
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject
import kotlin.random.Random

@Inject
class RandomNumberHandler : CommandHandler {
    override val description: String = "Generates a random number"

    override suspend fun handle(text: String): Reply {
        val args = ArgumentParser.parse(text)

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

    companion object {
        const val COMMAND = "randnum"
    }
}
