package dev.gianmarcodavid.telegram.inject

import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.finance.AlphaVantageApi
import dev.gianmarcodavid.telegram.finance.FinanceCommandHandler
import dev.gianmarcodavid.telegram.finance.StockPriceCommandHandler
import dev.gianmarcodavid.telegram.`fun`.JokeCommandHandler
import dev.gianmarcodavid.telegram.`fun`.JokesApi
import dev.gianmarcodavid.telegram.`fun`.RockPslsGameCommandHandler
import dev.gianmarcodavid.telegram.math.CoinCommandHandler
import dev.gianmarcodavid.telegram.math.RandomChoiceHandler
import dev.gianmarcodavid.telegram.math.RandomNumberHandler
import dev.gianmarcodavid.telegram.nutrition.OpenFoodFactsApi
import dev.gianmarcodavid.telegram.translate.DeepLApi
import dev.gianmarcodavid.telegram.translate.TranslateCommandHandler
import dev.gianmarcodavid.telegram.weather.OpenMeteoApi
import dev.gianmarcodavid.telegram.weather.WeatherCommandHandler
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import retrofit2.Retrofit
import retrofit2.create

interface CommandsComponent {
    @Provides
    fun provideOpenMeteoApi(retrofit: Retrofit) = retrofit.newBuilder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .build()
        .create<OpenMeteoApi>()

    @Provides
    @IntoMap
    fun bindWeatherCommandHandler(handler: WeatherCommandHandler): Pair<String, CommandHandler> =
        WeatherCommandHandler.COMMAND to handler

    @Provides
    fun provideDeepLApi(retrofit: Retrofit) = retrofit.create<DeepLApi>()

    @Provides
    @IntoMap
    fun bindTranslateCommandHandler(handler: TranslateCommandHandler): Pair<String, CommandHandler> =
        TranslateCommandHandler.COMMAND to handler

    @Provides
    @IntoMap
    fun bindRpsGameCommandHandler(handler: RockPslsGameCommandHandler): Pair<String, CommandHandler> =
        RockPslsGameCommandHandler.COMMAND to handler

    @Provides
    fun provideAlphaVantageApi(retrofit: Retrofit) = retrofit.create<AlphaVantageApi>()

    @Provides
    @IntoMap
    fun bindStockPriceCommandHandler(handler: StockPriceCommandHandler): Pair<String, CommandHandler> =
        StockPriceCommandHandler.COMMAND to handler

    @Provides
    @IntoMap
    fun bindFinanceCommandHandler(handler: FinanceCommandHandler): Pair<String, CommandHandler> =
        FinanceCommandHandler.COMMAND to handler

    @Provides
    fun provideJokesApi(retrofit: Retrofit) = retrofit.create<JokesApi>()

    @Provides
    @IntoMap
    fun bindJokeCommandHandler(handler: JokeCommandHandler): Pair<String, CommandHandler> =
        JokeCommandHandler.COMMAND to handler

    @Provides
    @IntoMap
    fun bindRandomNumberHandler(handler: RandomNumberHandler): Pair<String, CommandHandler> =
        RandomNumberHandler.COMMAND to handler

    @Provides
    @IntoMap
    fun bindCoinHandler(handler: CoinCommandHandler): Pair<String, CommandHandler> = "coin" to handler

    @Provides
    @IntoMap
    fun bindRandomChoiceHandler(handler: RandomChoiceHandler): Pair<String, CommandHandler> =
        RandomChoiceHandler.COMMAND to handler

    @Provides
    fun provideOpenFoodFactsApi(retrofit: Retrofit) = retrofit.newBuilder()
        .baseUrl("https://world.openfoodfacts.org/")
        .build()
        .create<OpenFoodFactsApi>()

}
