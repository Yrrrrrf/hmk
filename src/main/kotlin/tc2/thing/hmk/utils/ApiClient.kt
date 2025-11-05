package tc2.thing.hmk.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    // The base URL of your FastAPI backend
    const val BASE_URL = "http://localhost:8000"

    // Lazy initialization to avoid initialization issues in servlet containers
    val client: HttpClient by lazy {
        HttpClient(CIO) {
            // Increase timeout values to handle potentially slow container communication
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000 // 60 seconds
                connectTimeoutMillis = 60_000 // 60 seconds
                socketTimeoutMillis = 60_000  // 60 seconds
            }

            // Configure CIO engine for better container networking
            engine {
                // Increase connection pool size and timeout settings
                endpoint.connectTimeout = 60_000
                endpoint.socketTimeout = 60_000
            }

            // Configure JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Very useful for API evolution
                })
            }
            
            // Add logging for debugging API calls
            install(Logging) {
                level = LogLevel.ALL
            }

            // Follow redirects and don't fail on non-success status codes
            expectSuccess = false
        }
    }
}