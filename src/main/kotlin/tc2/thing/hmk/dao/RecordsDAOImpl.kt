package tc2.thing.hmk.dao

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import tc2.thing.hmk.models.*
import tc2.thing.hmk.utils.ApiClient
import java.text.SimpleDateFormat
import java.util.*

class RecordsDAOImpl : RecordsDAO {
    private val client get() = ApiClient.client
    private val recordsUrl = "${ApiClient.BASE_URL}/hmb/records"
    private val usuarioUrl = "${ApiClient.BASE_URL}/hmb/usuario"
    private val juegoUrl = "${ApiClient.BASE_URL}/hmb/juego"
    
    // Helper to parse ISO 8601 date strings from the API
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun crear(r: Record): Record {
        return runBlocking {
            val createDTO = CreateRecordDTO(
                usuarioId = r.usuario.id,
                juegoId = r.juego.id,
                puntaje = r.puntaje
            )
            val response = client.post(recordsUrl) {
                contentType(ContentType.Application.Json)
                setBody(createDTO)
            }
            if (response.status == HttpStatusCode.Created) {
                val createdRecordDTO = response.body<RecordDTO>()
                // Return the original record with the new ID assigned
                r.id = createdRecordDTO.id
                r
            } else {
                throw Exception("Failed to create record. Status: ${response.status.value}")
            }
        }
    }

    override fun getTopScores(juegoId: Long, limit: Int): List<Record> {
        return runBlocking {
            val recordDTOs: List<RecordDTO> = client.get(recordsUrl) {
                url {
                    parameters.append("juego_id", juegoId.toString())
                    parameters.append("order_by", "puntaje")
                    parameters.append("order_dir", "desc")
                    parameters.append("limit", limit.toString())
                }
            }.body()

            // IMPORTANT: This is an N+1 query problem. For a high-performance application,
            // you would ideally modify the API to return user data embedded in the record response.
            // For this guide, we replicate the original logic.
            recordDTOs.map { dto ->
                // Fetch the corresponding Usuario
                val usuario = client.get(usuarioUrl) {
                    url { parameters.append("id", dto.usuarioId.toString()) }
                }.body<List<Usuario>>().first()

                // Fetch the corresponding Juego
                val juego = client.get(juegoUrl) {
                    url { parameters.append("id", dto.juegoId.toString()) }
                }.body<List<Juego>>().first()

                // Assemble the final Record object
                Record(
                    id = dto.id,
                    usuario = usuario,
                    juego = juego,
                    puntaje = dto.puntaje,
                    fecha = isoFormat.parse(dto.fecha) ?: Date()
                )
            }
        }
    }
    
    override fun consultar(j: Juego, u: Usuario): Record? {
         return runBlocking {
            val recordDTOs: List<RecordDTO> = client.get(recordsUrl) {
                url {
                    parameters.append("juego_id", j.id.toString())
                    parameters.append("usuario_id", u.id.toString())
                    parameters.append("limit", "1")
                }
            }.body()

            recordDTOs.firstOrNull()?.let { dto ->
                 Record(
                    id = dto.id,
                    usuario = u, // We already have the user
                    juego = j,   // We already have the game
                    puntaje = dto.puntaje,
                    fecha = isoFormat.parse(dto.fecha) ?: Date()
                )
            }
        }
    }

    override fun consultar(j: Juego): List<Record> {
        return runBlocking {
            val recordDTOs: List<RecordDTO> = client.get(recordsUrl) {
                url {
                    parameters.append("juego_id", j.id.toString())
                    parameters.append("order_by", "puntaje")
                    parameters.append("order_dir", "desc")
                }
            }.body()

            recordDTOs.map { dto ->
                // Fetch the corresponding Usuario
                val usuario = client.get(usuarioUrl) {
                    url { parameters.append("id", dto.usuarioId.toString()) }
                }.body<List<Usuario>>().first()

                val juego = j // We already have the game

                // Assemble the final Record object
                Record(
                    id = dto.id,
                    usuario = usuario,
                    juego = juego,
                    puntaje = dto.puntaje,
                    fecha = isoFormat.parse(dto.fecha) ?: Date()
                )
            }
        }
    }

    override fun consultar(r: Record): Record? {
        return runBlocking {
            val recordDTOs: List<RecordDTO> = client.get(recordsUrl) {
                url {
                    parameters.append("id", r.id.toString())
                }
            }.body()

            recordDTOs.firstOrNull()?.let { dto ->
                // Fetch the corresponding Usuario
                val usuario = client.get(usuarioUrl) {
                    url { parameters.append("id", dto.usuarioId.toString()) }
                }.body<List<Usuario>>().first()

                // Fetch the corresponding Juego
                val juego = client.get(juegoUrl) {
                    url { parameters.append("id", dto.juegoId.toString()) }
                }.body<List<Juego>>().first()

                // Assemble the final Record object
                Record(
                    id = dto.id,
                    usuario = usuario,
                    juego = juego,
                    puntaje = dto.puntaje,
                    fecha = isoFormat.parse(dto.fecha) ?: Date()
                )
            }
        }
    }

    override fun consultarNumeroDeRecords(j: Juego): Int {
        return runBlocking {
            val recordDTOs: List<RecordDTO> = client.get(recordsUrl) {
                url {
                    parameters.append("juego_id", j.id.toString())
                }
            }.body()

            recordDTOs.size
        }
    }
}