package tc2.thing.hmk

import kotlinx.coroutines.runBlocking
import tc2.thing.hmk.models.Usuario
import tc2.thing.hmk.dao.UsuariosDAOImpl
import tc2.thing.hmk.utils.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Test script to verify API connectivity
 */
fun main() = runBlocking {
    println("Starting API connection test...")
    println("Base URL: ${ApiClient.BASE_URL}")
    
    try {
        // Test basic connectivity to the API
        testBasicConnectivity()
        
        // Test specific endpoints
        testUsuarioEndpoints()
        
        println("\nAPI connection test completed!")
    } catch (e: Exception) {
        println("Error during API connection test: ${e.message}")
        e.printStackTrace()
    }
}

private suspend fun testBasicConnectivity() {
    println("\n--- Testing basic connectivity ---")
    
    try {
        val response = ApiClient.client.get("${ApiClient.BASE_URL}/health")
        println("Health check response status: ${response.status}")
        
        if (response.status.isSuccess()) {
            val healthData = response.body<Map<String, Any>>()
            println("Health check response: $healthData")
        } else {
            println("Health check failed with status: ${response.status}")
        }
    } catch (e: Exception) {
        println("Health check failed with exception: ${e.message}")
        e.printStackTrace()
    }
}

private suspend fun testUsuarioEndpoints() {
    println("\n--- Testing Usuario endpoints ---")
    
    val usuariosDAO = UsuariosDAOImpl()
    
    // First, let's try to list all users (safe operation)
    try {
        println("Attempting to list all users...")
        val allUsers = usuariosDAO.consultar()
        println("Found ${allUsers.size} users")
        allUsers.forEach { user ->
            println("  - User ID: ${user.id}, Login: ${user.login}")
        }
    } catch (e: Exception) {
        println("Failed to list users: ${e.message}")
        e.printStackTrace()
    }
    
    // Test creating a temporary user (if you have the endpoint)
    try {
        println("\nAttempting to create a test user...")
        val testUser = Usuario(
            id = 0, // Will be assigned by backend
            login = "test_user_${System.currentTimeMillis()}",
            password = "password123",
            correo = "test@example.com"
        )
        
        val createdUser = usuariosDAO.crear(testUser)
        println("Created user with ID: ${createdUser.id}, Login: ${createdUser.login}")
        
        // Clean up: delete the test user
        val deleted = usuariosDAO.borrar(createdUser)
        println("Cleaned up test user, delete result: $deleted")
    } catch (e: Exception) {
        println("Failed to create/test user: ${e.message}")
        e.printStackTrace()
    }
}