package tc2.thing.hmk.dao

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import tc2.thing.hmk.models.Usuario
import tc2.thing.hmk.utils.ApiClient

class UsuariosDAOImpl : UsuariosDAO {
    private val client get() = ApiClient.client
    private val baseUrl = "${ApiClient.BASE_URL}/hmb/usuario"

    override fun crear(u: Usuario): Usuario {
        return runBlocking {
            val response = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(u)
            }
            if (response.status == HttpStatusCode.Created) {
                response.body<Usuario>()
            } else {
                throw Exception("Failed to create user. Status: ${response.status.value}")
            }
        }
    }
    
    override fun authenticate(login: String, password: String): Usuario? {
        return runBlocking {
            val users: List<Usuario> = client.get(baseUrl) {
                url {
                    parameters.append("login", login)
                    parameters.append("password", password)
                }
            }.body()
            users.firstOrNull()
        }
    }
    
    // Other methods like consultar(), modificar(), and borrar() would be implemented similarly.
    // For example, consultar(u: Usuario):
    override fun consultar(u: Usuario): List<Usuario> {
        return runBlocking {
             client.get(baseUrl) {
                url {
                    if (u.id != 0L) parameters.append("id", u.id.toString())
                    if (u.login.isNotEmpty()) parameters.append("login", u.login)
                    // ... add other parameters as needed
                }
            }.body<List<Usuario>>()
        }
    }

    override fun consultar(): List<Usuario> {
        return runBlocking {
            client.get(baseUrl).body<List<Usuario>>()
        }
    }

    override fun modificar(u: Usuario): Usuario {
        return runBlocking {
            val response = client.put("$baseUrl/${u.id}") {
                contentType(ContentType.Application.Json)
                setBody(u)
            }
            if (response.status.isSuccess()) {
                response.body<Usuario>()
            } else {
                throw Exception("Failed to update user. Status: ${response.status.value}")
            }
        }
    }

    override fun borrar(u: Usuario): Int {
        return runBlocking {
            val response = client.delete("$baseUrl/${u.id}")
            if (response.status.isSuccess()) {
                1 // Return 1 to indicate successful deletion
            } else {
                0 // Return 0 to indicate failure
            }
        }
    }
}