package dev.gianmarcodavid.telegram.finance

@JvmInline
value class Percent(val value: Double)

fun Percent.formatted(): String = "%.2f%%".format(value * 100)
