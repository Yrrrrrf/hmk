package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.html.*
import javax.sql.DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.time.Duration
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8000, host = "0.0.0.0", module = Application::module)
    server.start(wait = true)
}

fun Application.module() {
    // Install content negotiation for JSON
    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json()
    }
    
    // Set up database connection
    val dataSource = setupDatabase()
    
    // Create health check service
    val healthCheckService = HealthCheckService(dataSource)
    
    routing {
        // Health check route
        healthRoute(healthCheckService)
        
        // Main page route
        get("/") {
            call.respondHtml {
                head {
                    title { +"Ktor Application" }
                }
                body {
                    h1 { +"Ktor Application" }
                    p { +"This is a basic Ktor application with database connectivity testing." }
                    
                    div {
                        h2 { +"Database Connectivity Test" }
                        button {
                            id = "testButton"
                            onClick = "testHealth()"
                            +"Test Database Connection"
                        }
                        
                        div {
                            id = "response"
                        }
                    }
                    
                    script {
                        unsafe {
                            +"""
                            async function testHealth() {
                                const responseDiv = document.getElementById('response');
                                responseDiv.innerHTML = 'Testing...';
                                
                                try {
                                    const response = await fetch('/health');
                                    const data = await response.json();
                                    
                                    responseDiv.innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
                                } catch (error) {
                                    responseDiv.innerHTML = 'Error: ' + error.message;
                                }
                            }
                            """.trimIndent()
                        }
                    }
                }
            }
        }
    }
}

fun setupDatabase(): DataSource {
    val config = HikariConfig().apply {
        jdbcUrl = System.getenv("JDBC_DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/testdb"
        username = System.getenv("DB_USERNAME") ?: "postgres"
        password = System.getenv("DB_PASSWORD") ?: "password"
        driverClassName = "org.postgresql.Driver"
        
        maximumPoolSize = 10
        minimumIdle = 2
        idleTimeout = Duration.ofMinutes(10).toMillis()
        maxLifetime = Duration.ofMinutes(30).toMillis()
        connectionTimeout = Duration.ofSeconds(30).toMillis()
    }
    
    return HikariDataSource(config)
}