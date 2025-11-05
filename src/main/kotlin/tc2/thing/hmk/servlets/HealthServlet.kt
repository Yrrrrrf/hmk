package tc2.thing.hmk.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tc2.thing.hmk.utils.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.logging.Logger

@Serializable
data class HealthResponse(
    val status: String,
    val backendStatus: String,
    val details: String? = null
)

@WebServlet(name = "healthServlet", value = ["/health"])
class HealthServlet : HttpServlet() {

    // Get a logger to print messages to the server console
    private val logger = Logger.getLogger(HealthServlet::class.java.name)

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        // This is the server-side log you requested.
        logger.info("Received a request for /health from ${request.remoteAddr}")

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        var healthResponse: HealthResponse

        try {
            healthResponse = runBlocking {
                // Now, the Kotlin server will try to connect to the Python API
                logger.info("Attempting to connect to the backend API at ${ApiClient.BASE_URL}/health...")
                
                val apiResponse = ApiClient.client.get("${ApiClient.BASE_URL}/health")

                logger.info("Received response from backend API with status: ${apiResponse.status}")

                if (apiResponse.status.isSuccess()) {
                    // The API responded successfully!
                    val bodyText = apiResponse.body<String>()
                    logger.info("Backend API response body: $bodyText")
                    HealthResponse(status = "ok", backendStatus = "ok", details = bodyText)
                } else {
                    // The API responded with an error status (like 404, 500, etc.)
                    val errorBody = apiResponse.body<String>()
                    logger.severe("Backend API returned an error: ${apiResponse.status}. Body: $errorBody")
                    HealthResponse(
                        status = "error",
                        backendStatus = "error",
                        details = "API responded with status ${apiResponse.status.value}"
                    )
                }
            }
        } catch (e: Exception) {
            // This block will catch critical network errors, like "Connection refused".
            // THIS IS LIKELY WHERE YOUR CURRENT PROBLEM WILL BE LOGGED.
            logger.severe("Failed to connect to backend API: ${e.message}")
            e.printStackTrace()
            healthResponse = HealthResponse(
                status = "error",
                backendStatus = "unreachable",
                details = e.message ?: "An unknown connection error occurred."
            )
            response.status = 500 // Internal Server Error
        }

        // Send the final JSON response back to the browser
        val jsonResponse = Json.encodeToString(healthResponse)
        logger.info("Sending final JSON response to the browser: $jsonResponse")
        response.writer.write(jsonResponse)
    }
}