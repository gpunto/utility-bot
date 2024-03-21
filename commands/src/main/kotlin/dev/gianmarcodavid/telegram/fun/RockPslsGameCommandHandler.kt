package dev.gianmarcodavid.telegram.`fun`

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject

@Inject
class RockPslsGameCommandHandler : CommandHandler {
    override val description: String = "Play the rock-paper-scissors-lizard-Spock game"

    private val beats = buildMap {
        put(Move.Rock, setOf(Move.Scissors, Move.Lizard))
        put(Move.Paper, setOf(Move.Rock, Move.Spock))
        put(Move.Scissors, setOf(Move.Paper, Move.Lizard))
        put(Move.Lizard, setOf(Move.Paper, Move.Spock))
        put(Move.Spock, setOf(Move.Rock, Move.Scissors))
    }

    override suspend fun handle(text: String): Reply {
        if (text == "rules") {
            return Reply(RULES)
        }

        val userMove = Move.entries.find { it.name.equals(text, ignoreCase = true) }

        return if (userMove == null) {
            val availableMoves = Move.entries.joinToString("|", transform = Move::name)
            Reply("Usage: ".regular(), "/$COMMAND [$availableMoves]".code())
        } else {
            val botMove = Move.entries.random()

            val result = when {
                userMove == botMove -> "It's a tie!"
                botMove in beats[userMove]!! -> "You win!"
                else -> "You lose!"
            }
            Reply("I play ${botMove.name} ${botMove.emoji}. ".regular(), result.bold())
        }
    }

    private enum class Move(val emoji: String) {
        Rock("🪨"), Paper("📄"), Scissors("✂️"), Lizard("🦎"), Spock("🖖")
    }

    companion object {
        const val COMMAND = "rockpsls"
        private const val RULES = "The rules are simple: \n" +
            "• Rock crushes Scissors and Lizard\n" +
            "• Paper covers Rock and disproves Spock\n" +
            "• Scissors cuts Paper and decapitates Lizard\n" +
            "• Lizard eats Paper and poisons Spock\n" +
            "• Spock smashes Scissors and vaporizes Rock"
    }
}