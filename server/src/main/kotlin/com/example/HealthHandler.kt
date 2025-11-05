package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Duration
import java.time.Instant
import javax.sql.DataSource

class HealthCheckService(private val dataSource: DataSource) {
    private val startTime = Instant.now()

    suspend fun getHealthStatus(): Map<String, Any> {
        val uptime = Duration.between(startTime, Instant.now())
        val dbStatus = try {
            dataSource.connection.use { connection ->
                connection.createStatement().use { stmt ->
                    stmt.execute("SELECT 1")
                }
            }
            "ok" to null
        } catch (e: Exception) {
            "error" to e.message
        }

        return mapOf(
            "status" to "ok",
            "uptime" to formatDuration(uptime),
            "db_status" to dbStatus.first,
            "db_error" to dbStatus.second
        )
    }

    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = duration.toMinutesPart()
        val seconds = duration.toSecondsPart()
        val nanos = duration.toNanosPart()
        return "%d:%02d:%02d.%06d".format(hours, minutes, seconds, nanos / 1000)
    }
}

fun Route.healthRoute(healthCheckService: HealthCheckService) {
    get("/health") {
        val healthStatus = healthCheckService.getHealthStatus()
        call.respond(healthStatus)
    }
}