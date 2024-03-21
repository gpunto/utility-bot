package dev.gianmarcodavid.telegram.inject

import FoundationComponent
import dev.gianmarcodavid.telegram.App
import me.tatarka.inject.annotations.Component

@Component
@Singleton
interface SingletonComponent : CommandsComponent, FoundationComponent {
    val app: App
}
