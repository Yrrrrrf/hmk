package tc2.thing.hmk.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    // The base URL of your FastAPI backend
    const val BASE_URL = "http://localhost:8000"

    // Lazy initialization to avoid initialization issues in servlet containers
    val client: HttpClient by lazy {
        HttpClient(CIO) {
            // Configure JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Very useful for API evolution
                })
            }
            // Optional: Add logging to see request/response details in console
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }
}