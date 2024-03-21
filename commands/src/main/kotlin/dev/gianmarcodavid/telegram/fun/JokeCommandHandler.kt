package dev.gianmarcodavid.telegram.`fun`

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import me.tatarka.inject.annotations.Inject

@Inject
class JokeCommandHandler(private val api: JokesApi) : CommandHandler {
    override val description: String = "Tell a random joke"

    override suspend fun handle(text: String): Reply {
        val joke = api.getJoke()
        return Reply("${joke.setup}\n…\n${joke.punchline}")
    }

    companion object {
        const val COMMAND = "joke"
    }
}
