package dev.gianmarcodavid.telegram.translate

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.code
import dev.gianmarcodavid.telegram.command.regular
import me.tatarka.inject.annotations.Inject

@Inject
class TranslateCommandHandler(private val api: DeepLApi) : CommandHandler {
    override val description: String = "Translate text"

    override suspend fun handle(text: String): Reply {
        val bodyDto = parseCommand(text)

        return if (bodyDto.text.first().isEmpty()) {
            Reply(USAGE)
        } else {
            val translation = api.translate(bodyDto)
            Reply(translation.translations.first().text)
        }
    }

    private fun parseCommand(text: String): TranslateBodyDto {
        val targetLang = text.take(4)

        return if (targetLang.matches(targetLanguageRegex)) {
            TranslateBodyDto(text = text.drop(4), targetLang = targetLang.substring(1, 3).uppercase())
        } else {
            TranslateBodyDto(text = text, targetLang = "IT")
        }
    }

    companion object {
        const val COMMAND = "translate"
        private val USAGE = listOf(
            "Usage: ".regular(),
            "/translate >languageCode TextToTranslate".code(),
            "\nNote: ".regular(), "languageCode".code(), " is optional and defaults to IT.".regular(),
            "\n\nExamples:".regular(),
            "\n translate >es Hello world".code(),
            "\n translate Hello world".code()
        )

        private val targetLanguageRegex = Regex(">[a-z]{2} ")
    }
}