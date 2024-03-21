package dev.gianmarcodavid.telegram.finance

data class Money(val amount: Double, val currency: String, val post: Boolean)

operator fun Money.times(multiplier: Double): Money = copy(amount = amount * multiplier)

operator fun Money.div(divisor: Double): Money = copy(amount = amount / divisor)

fun Money.formatted(): String {
    val formattedAmount = "%.2f".format(amount)
    return if (post) "$formattedAmount$currency" else "$currency$formattedAmount"
}
