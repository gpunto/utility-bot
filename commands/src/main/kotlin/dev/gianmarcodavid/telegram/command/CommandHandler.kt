package dev.gianmarcodavid.telegram.command

interface CommandHandler {
    val description: String

    suspend fun handle(text: String): Reply
}
