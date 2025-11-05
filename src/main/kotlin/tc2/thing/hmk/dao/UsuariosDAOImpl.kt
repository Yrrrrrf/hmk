package tc2.thing.hmk.dao

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import tc2.thing.hmk.models.Usuario
import tc2.thing.hmk.utils.ApiClient
import java.util.logging.Logger

class UsuariosDAOImpl : UsuariosDAO {
    private val logger = Logger.getLogger(UsuariosDAOImpl::class.simpleName)
    private val client get() = ApiClient.client
    private val baseUrl = "${ApiClient.BASE_URL}/hmb/usuario"

    override fun crear(u: Usuario): Usuario {
        logger.info("Creating user: ${u.login}")
        return try {
            runBlocking {
                val response = client.post(baseUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(u)
                }
                logger.info("Create user response status: ${response.status}")
                if (response.status.isSuccess()) {
                    val createdUser = response.body<Usuario>()
                    logger.info("User created successfully with ID: ${createdUser.id}")
                    createdUser
                } else {
                    logger.severe("Failed to create user. Status: ${response.status.value}")
                    throw Exception("Failed to create user. Status: ${response.status.value}")
                }
            }
        } catch (e: Exception) {
            logger.severe("User creation failed with exception: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
    
    override fun authenticate(login: String, password: String): Usuario? {
        logger.info("Authenticating user: $login")
        return try {
            runBlocking {
                val response = client.get(baseUrl) {
                    url {
                        parameters.append("login", login)
                        parameters.append("password", password)
                    }
                }
                logger.info("Authentication response status: ${response.status}")
                
                if (response.status.isSuccess()) {
                    val users: List<Usuario> = response.body()
                    val authenticatedUser = users.firstOrNull()
                    if (authenticatedUser != null) {
                        logger.info("Authentication successful for user ID: ${authenticatedUser.id}")
                    } else {
                        logger.warning("Authentication failed for user: $login - no user found")
                    }
                    authenticatedUser
                } else {
                    logger.severe("Authentication request failed with status: ${response.status}")
                    null
                }
            }
        } catch (e: Exception) {
            logger.severe("Authentication failed with exception: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    // Other methods like consultar(), modificar(), and borrar() would be implemented similarly.
    // For example, consultar(u: Usuario):
    override fun consultar(u: Usuario): List<Usuario> {
        logger.info("Consulting users with criteria: id=${u.id}, login=${u.login}")
        return try {
            runBlocking {
                val response = client.get(baseUrl) {
                    url {
                        if (u.id != 0L) parameters.append("id", u.id.toString())
                        if (u.login.isNotEmpty()) parameters.append("login", u.login)
                        // ... add other parameters as needed
                    }
                }
                logger.info("Consult users response status: ${response.status}")
                if (response.status.isSuccess()) {
                    response.body<List<Usuario>>()
                } else {
                    logger.severe("Consult users failed with status: ${response.status}")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            logger.severe("Consult users failed with exception: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override fun consultar(): List<Usuario> {
        logger.info("Consulting all users")
        return try {
            runBlocking {
                val response = client.get(baseUrl)
                logger.info("Consult all users response status: ${response.status}")
                if (response.status.isSuccess()) {
                    response.body<List<Usuario>>()
                } else {
                    logger.severe("Consult all users failed with status: ${response.status}")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            logger.severe("Consult all users failed with exception: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override fun modificar(u: Usuario): Usuario {
        logger.info("Modifying user with ID: ${u.id}")
        return try {
            runBlocking {
                val response = client.put("$baseUrl/${u.id}") {
                    contentType(ContentType.Application.Json)
                    setBody(u)
                }
                logger.info("Modify user response status: ${response.status}")
                if (response.status.isSuccess()) {
                    val updatedUser = response.body<Usuario>()
                    logger.info("User modified successfully with ID: ${updatedUser.id}")
                    updatedUser
                } else {
                    logger.severe("Failed to update user. Status: ${response.status.value}")
                    throw Exception("Failed to update user. Status: ${response.status.value}")
                }
            }
        } catch (e: Exception) {
            logger.severe("Modify user failed with exception: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override fun borrar(u: Usuario): Int {
        logger.info("Deleting user with ID: ${u.id}")
        return try {
            runBlocking {
                val response = client.delete("$baseUrl/${u.id}")
                logger.info("Delete user response status: ${response.status}")
                if (response.status.isSuccess()) {
                    logger.info("User deleted successfully with ID: ${u.id}")
                    1 // Return 1 to indicate successful deletion
                } else {
                    logger.severe("Failed to delete user with ID: ${u.id}. Status: ${response.status.value}")
                    0 // Return 0 to indicate failure
                }
            }
        } catch (e: Exception) {
            logger.severe("Delete user failed with exception: ${e.message}")
            e.printStackTrace()
            0 // Return 0 to indicate failure
        }
    }
}