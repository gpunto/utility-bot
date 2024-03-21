package dev.gianmarcodavid.telegram

import dev.gianmarcodavid.telegram.inject.SingletonComponent
import dev.gianmarcodavid.telegram.inject.create

suspend fun main() {
    SingletonComponent::class.create().app.run()
}
