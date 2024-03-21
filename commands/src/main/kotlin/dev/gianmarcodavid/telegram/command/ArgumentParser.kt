package dev.gianmarcodavid.telegram.command

object ArgumentParser {
    fun parse(text: String, separator: Regex = Separator.whitespace): List<String> =
        text.split(separator).filterNot(String::isEmpty)

    object Separator {
        val whitespace = Regex("\\s+")
        val commaSpace = Regex("\\s*,\\s*")
    }
}
