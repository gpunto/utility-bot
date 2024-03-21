package dev.gianmarcodavid.telegram.translate

import dev.gianmarcodavid.telegram.foundation_api.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeepLApi {
    @POST("https://api-free.deepl.com/v2/translate")
    @Headers("Authorization: DeepL-Auth-Key ${BuildConfig.DEEPL_KEY}")
    suspend fun translate(@Body body: TranslateBodyDto): TranslateResponseDto
}

@Serializable
data class TranslateBodyDto(
    val text: List<String>,
    @SerialName("target_lang")
    val targetLang: String
) {
    constructor(text: String, targetLang: String) : this(listOf(text), targetLang)
}

@Serializable
data class TranslateResponseDto(
    val translations: List<TranslationDto>
)

@Serializable
data class TranslationDto(
    @SerialName("detected_source_language")
    val detectedSourceLanguage: String,
    val text: String
)