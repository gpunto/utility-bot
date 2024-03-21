import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.gianmarcodavid.telegram.inject.Singleton
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import kotlin.random.Random

interface FoundationComponent {
    @Provides
    fun provideRandom(): Random = Random.Default

    @Provides
    @Singleton
    fun provideLogger(): Logger = LoggerFactory.getLogger("dev.gianmarcodavid.telegram")

    @Provides
    fun provideBaseRetrofit(logger: Logger): Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://nonexistent.abcxyz")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor(logger::info).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .build()
}

private val json = Json {
    ignoreUnknownKeys = true
}
