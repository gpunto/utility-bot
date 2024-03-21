package dev.gianmarcodavid.telegram.`fun`

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface JokesApi {
    @GET("https://official-joke-api.appspot.com/random_joke")
    suspend fun getJoke(): JokeDto
}

@Serializable
data class JokeDto(
    val setup: String,
    val punchline: String,
)
