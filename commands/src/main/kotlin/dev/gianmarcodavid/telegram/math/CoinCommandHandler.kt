package dev.gianmarcodavid.telegram.math

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject
import kotlin.random.Random

@Inject
class CoinCommandHandler(private val random: Random) : CommandHandler {
    override val description: String = "Flip a coin"
    override suspend fun handle(text: String): Reply {
        val result = if (random.nextBoolean()) "heads" else "tails"
        return Reply("Coin flip: ".regular(), result.bold())
    }
}