package dev.gianmarcodavid.telegram

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.TextFragment
import dev.gianmarcodavid.telegram.foundation_api.BuildConfig
import dev.inmo.micro_utils.coroutines.safely
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.setMessageReaction
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onUnhandledCommand
import dev.inmo.tgbotapi.types.BotCommand
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.utils.EntitiesBuilder
import dev.inmo.tgbotapi.utils.PreviewFeature
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.code
import dev.inmo.tgbotapi.utils.regular
import io.ktor.utils.io.*
import me.tatarka.inject.annotations.Inject
import org.slf4j.Logger

@Inject
class App(
    private val handlers: Map<String, CommandHandler>,
    private val logger: Logger,
) {
    private val allowedChatIds = BuildConfig.ALLOWED_CHATS.split(",").mapTo(mutableSetOf(), String::toLong).toSet()
    private val messageFilter = { message: TextMessage -> message.chat.id.chatId.long in allowedChatIds }

    @OptIn(PreviewFeature::class)
    suspend fun run() {
        logger.info("Starting bot")
        val bot = telegramBot(BuildConfig.BOT_TOKEN)

        bot.buildBehaviourWithLongPolling(defaultExceptionsHandler = { throwable ->
            if (throwable !is CancellationException) logger.error("Uncaught error", throwable)
        }) {
            setMyCommands(handlers.map { (command, handler) -> BotCommand(command, handler.description) })

            handlers.forEach { (command, handler) ->
                onCommandWithHandler(command, handler)
            }

            onUnhandledCommand(requireOnlyCommandInMessage = false, initialFilter = messageFilter) { message ->
                setMessageReaction(message, "🤔")
            }
        }.join()
    }

    private suspend fun BehaviourContext.onCommandWithHandler(command: String, handler: CommandHandler) {
        onCommand(command, requireOnlyCommandInMessage = false, initialFilter = messageFilter) { message ->
            safely(onException = { throwable ->
                setMessageReaction(message, "💩")
                logger.error("Error handling command '$command'", throwable)
            }) {
                setMessageReaction(message, "🫡")

                val commandBody = message.content.text.replace("/$command(@[a-zA-Z_]+ )?".toRegex(), "").trim()

                val reply = handler.handle(commandBody)
                reply(to = message) { add(reply) }
            }
        }
    }

    private fun EntitiesBuilder.add(reply: Reply) {
        reply.text.forEach { fragment ->
            when (fragment.type) {
                TextFragment.Type.Regular -> regular(fragment.text)
                TextFragment.Type.Bold -> bold(fragment.text)
                TextFragment.Type.Code -> code(fragment.text)
            }
        }
    }
}
