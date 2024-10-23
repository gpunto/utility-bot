package dev.gianmarcodavid.telegram.inject

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.gianmarcodavid.telegram.command.CommandHandler
import dev.gianmarcodavid.telegram.counter.CounterCommandHandler
import dev.gianmarcodavid.telegram.database.CounterQueries
import dev.gianmarcodavid.telegram.database.Database
import dev.gianmarcodavid.telegram.finance.AlphaVantageApi
import dev.gianmarcodavid.telegram.finance.FinanceCommandHandler
import dev.gianmarcodavid.telegram.finance.StockPriceCommandHandler
import dev.gianmarcodavid.telegram.`fun`.JokeCommandHandler
import dev.gianmarcodavid.telegram.`fun`.JokesApi
import dev.gianmarcodavid.telegram.`fun`.RockPslsGameCommandHandler
import dev.gianmarcodavid.telegram.holiday.HolidayApi
import dev.gianmarcodavid.telegram.holiday.HolidayHandler
import dev.gianmarcodavid.telegram.math.CoinCommandHandler
import dev.gianmarcodavid.telegram.math.RandomCommandHandler
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
    fun bindRandomCommandHandler(handler: RandomCommandHandler): Pair<String, CommandHandler> =
        RandomCommandHandler.COMMAND to handler

    @Provides
    @IntoMap
    fun bindCoinHandler(handler: CoinCommandHandler): Pair<String, CommandHandler> =
        CoinCommandHandler.COMMAND to handler

    @Provides
    fun provideHolidayApi(retrofit: Retrofit) = retrofit.create<HolidayApi>()

    @Provides
    @IntoMap
    fun bindHolidayHandler(handler: HolidayHandler): Pair<String, CommandHandler> = HolidayHandler.COMMAND to handler

    @Provides
    @Singleton
    fun provideDatabase(): CounterQueries = JdbcSqliteDriver("jdbc:sqlite:telegram.db")
        .also(Database.Schema::create)
        .let(Database::invoke)
        .counterQueries

    @Provides
    @IntoMap
    fun bindCounterHandler(handler: CounterCommandHandler): Pair<String, CommandHandler> =
        CounterCommandHandler.COMMAND to handler
}
