package dev.gianmarcodavid.telegram.command

import dev.gianmarcodavid.telegram.command.TextFragment.Type

data class Reply(val text: List<TextFragment>)

data class TextFragment(val text: String, val type: Type) {
    enum class Type { Regular, Bold, Code }
}

fun Reply(fragment: TextFragment, vararg fragments: TextFragment) =
    Reply(buildList { add(fragment); addAll(fragments) })

fun Reply(fragment: String, vararg fragments: String) = Reply(buildList {
    add(TextFragment(fragment, Type.Regular))
    fragments.forEach { add(TextFragment(it, Type.Regular)) }
})

fun Reply(text: String) = Reply(listOf(TextFragment(text, Type.Regular)))

fun String.regular() = TextFragment(this, Type.Regular)
fun String.bold() = TextFragment(this, Type.Bold)
fun String.code() = TextFragment(this, Type.Code)
