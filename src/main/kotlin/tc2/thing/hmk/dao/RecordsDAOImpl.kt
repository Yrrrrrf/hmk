package tc2.thing.hmk.dao

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import tc2.thing.hmk.models.*
import tc2.thing.hmk.utils.ApiClient
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class RecordsDAOImpl : RecordsDAO {
    private val logger = Logger.getLogger(RecordsDAOImpl::class.simpleName)
    private val client get() = ApiClient.client
    private val recordsUrl = "${ApiClient.BASE_URL}/hmb/records"
    private val usuarioUrl = "${ApiClient.BASE_URL}/hmb/usuario"
    private val juegoUrl = "${ApiClient.BASE_URL}/hmb/juego"
    
    // Helper to parse ISO 8601 date strings from the API
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun crear(r: Record): Record {
        logger.info("Creating record - User ID: ${r.usuario.id}, Game ID: ${r.juego.id}, Score: ${r.puntaje}")
        return try {
            runBlocking {
                val createDTO = CreateRecordDTO(
                    usuarioId = r.usuario.id,
                    juegoId = r.juego.id,
                    puntaje = r.puntaje
                )
                val response = client.post(recordsUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(createDTO)
                }
                logger.info("Create record response status: ${response.status}")
                if (response.status.isSuccess()) {
                    val createdRecordDTO = response.body<RecordDTO>()
                    logger.info("Record created successfully with ID: ${createdRecordDTO.id}")
                    // Return the original record with the new ID assigned
                    r.id = createdRecordDTO.id
                    r
                } else {
                    logger.severe("Failed to create record. Status: ${response.status.value}")
                    throw Exception("Failed to create record. Status: ${response.status.value}")
                }
            }
        } catch (e: Exception) {
            logger.severe("Create record failed with exception: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override fun getTopScores(juegoId: Long, limit: Int): List<Record> {
        logger.info("Getting top scores - Game ID: $juegoId, Limit: $limit")
        return try {
            runBlocking {
                val response = client.get(recordsUrl) {
                    url {
                        parameters.append("juego_id", juegoId.toString())
                        parameters.append("order_by", "puntaje")
                        parameters.append("order_dir", "desc")
                        parameters.append("limit", limit.toString())
                    }
                }
                logger.info("Get top scores response status: ${response.status}")
                
                if (!response.status.isSuccess()) {
                    logger.severe("Get top scores request failed with status: ${response.status}")
                    return@runBlocking emptyList()
                }
                
                val recordDTOs: List<RecordDTO> = response.body()

                // IMPORTANT: This is an N+1 query problem. For a high-performance application,
                // you would ideally modify the API to return user data embedded in the record response.
                // For this guide, we replicate the original logic.
                recordDTOs.map { dto ->
                    logger.info("Fetching user and game data for record ID: ${dto.id}")
                    // Fetch the corresponding Usuario
                    val usuarioResponse = client.get(usuarioUrl) {
                        url { parameters.append("id", dto.usuarioId.toString()) }
                    }
                    logger.info("Fetch usuario response status: ${usuarioResponse.status}")
                    if (!usuarioResponse.status.isSuccess()) {
                        logger.severe("Failed to fetch user for record ${dto.id}, status: ${usuarioResponse.status}")
                        throw Exception("Failed to fetch user data for record ${dto.id}")
                    }
                    val usuarios = usuarioResponse.body<List<Usuario>>()
                    if (usuarios.isEmpty()) {
                        logger.severe("No user found for ID: ${dto.usuarioId}")
                        throw Exception("User not found for ID: ${dto.usuarioId}")
                    }
                    val usuario = usuarios.first()

                    // Fetch the corresponding Juego
                    val juegoResponse = client.get(juegoUrl) {
                        url { parameters.append("id", dto.juegoId.toString()) }
                    }
                    logger.info("Fetch juego response status: ${juegoResponse.status}")
                    if (!juegoResponse.status.isSuccess()) {
                        logger.severe("Failed to fetch game for record ${dto.id}, status: ${juegoResponse.status}")
                        throw Exception("Failed to fetch game data for record ${dto.id}")
                    }
                    val juegos = juegoResponse.body<List<Juego>>()
                    if (juegos.isEmpty()) {
                        logger.severe("No game found for ID: ${dto.juegoId}")
                        throw Exception("Game not found for ID: ${dto.juegoId}")
                    }
                    val juego = juegos.first()

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
        } catch (e: Exception) {
            logger.severe("Get top scores failed with exception: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    override fun consultar(j: Juego, u: Usuario): Record? {
        logger.info("Consulting record - Game ID: ${j.id}, User ID: ${u.id}")
        return try {
            runBlocking {
                val response = client.get(recordsUrl) {
                    url {
                        parameters.append("juego_id", j.id.toString())
                        parameters.append("usuario_id", u.id.toString())
                        parameters.append("limit", "1")
                    }
                }
                logger.info("Consult record response status: ${response.status}")
                
                if (!response.status.isSuccess()) {
                    logger.severe("Consult record request failed with status: ${response.status}")
                    return@runBlocking null
                }
                
                val recordDTOs: List<RecordDTO> = response.body()

                recordDTOs.firstOrNull()?.let { dto ->
                    logger.info("Found record with ID: ${dto.id}")
                     Record(
                        id = dto.id,
                        usuario = u, // We already have the user
                        juego = j,   // We already have the game
                        puntaje = dto.puntaje,
                        fecha = isoFormat.parse(dto.fecha) ?: Date()
                    )
                }
            }
        } catch (e: Exception) {
            logger.severe("Consult record failed with exception: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    override fun consultar(j: Juego): List<Record> {
        logger.info("Consulting all records for game ID: ${j.id}")
        return try {
            runBlocking {
                val response = client.get(recordsUrl) {
                    url {
                        parameters.append("juego_id", j.id.toString())
                        parameters.append("order_by", "puntaje")
                        parameters.append("order_dir", "desc")
                    }
                }
                logger.info("Consult game records response status: ${response.status}")
                
                if (!response.status.isSuccess()) {
                    logger.severe("Consult game records request failed with status: ${response.status}")
                    return@runBlocking emptyList()
                }
                
                val recordDTOs: List<RecordDTO> = response.body()

                recordDTOs.map { dto ->
                    logger.info("Processing record ID: ${dto.id}")
                    // Fetch the corresponding Usuario
                    val usuarioResponse = client.get(usuarioUrl) {
                        url { parameters.append("id", dto.usuarioId.toString()) }
                    }
                    logger.info("Fetch usuario for record ${dto.id} response status: ${usuarioResponse.status}")
                    if (!usuarioResponse.status.isSuccess()) {
                        logger.severe("Failed to fetch user for record ${dto.id}, status: ${usuarioResponse.status}")
                        throw Exception("Failed to fetch user data for record ${dto.id}")
                    }
                    val usuarios = usuarioResponse.body<List<Usuario>>()
                    if (usuarios.isEmpty()) {
                        logger.severe("No user found for ID: ${dto.usuarioId}")
                        throw Exception("User not found for ID: ${dto.usuarioId}")
                    }
                    val usuario = usuarios.first()

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
        } catch (e: Exception) {
            logger.severe("Consult game records failed with exception: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override fun consultar(r: Record): Record? {
        logger.info("Consulting record with ID: ${r.id}")
        return try {
            runBlocking {
                val response = client.get(recordsUrl) {
                    url {
                        parameters.append("id", r.id.toString())
                    }
                }
                logger.info("Consult specific record response status: ${response.status}")
                
                if (!response.status.isSuccess()) {
                    logger.severe("Consult specific record request failed with status: ${response.status}")
                    return@runBlocking null
                }
                
                val recordDTOs: List<RecordDTO> = response.body()

                recordDTOs.firstOrNull()?.let { dto ->
                    logger.info("Found record to process with ID: ${dto.id}")
                    // Fetch the corresponding Usuario
                    val usuarioResponse = client.get(usuarioUrl) {
                        url { parameters.append("id", dto.usuarioId.toString()) }
                    }
                    logger.info("Fetch usuario for record ${dto.id} response status: ${usuarioResponse.status}")
                    if (!usuarioResponse.status.isSuccess()) {
                        logger.severe("Failed to fetch user for record ${dto.id}, status: ${usuarioResponse.status}")
                        throw Exception("Failed to fetch user data for record ${dto.id}")
                    }
                    val usuarios = usuarioResponse.body<List<Usuario>>()
                    if (usuarios.isEmpty()) {
                        logger.severe("No user found for ID: ${dto.usuarioId}")
                        throw Exception("User not found for ID: ${dto.usuarioId}")
                    }
                    val usuario = usuarios.first()

                    // Fetch the corresponding Juego
                    val juegoResponse = client.get(juegoUrl) {
                        url { parameters.append("id", dto.juegoId.toString()) }
                    }
                    logger.info("Fetch juego for record ${dto.id} response status: ${juegoResponse.status}")
                    if (!juegoResponse.status.isSuccess()) {
                        logger.severe("Failed to fetch game for record ${dto.id}, status: ${juegoResponse.status}")
                        throw Exception("Failed to fetch game data for record ${dto.id}")
                    }
                    val juegos = juegoResponse.body<List<Juego>>()
                    if (juegos.isEmpty()) {
                        logger.severe("No game found for ID: ${dto.juegoId}")
                        throw Exception("Game not found for ID: ${dto.juegoId}")
                    }
                    val juego = juegos.first()

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
        } catch (e: Exception) {
            logger.severe("Consult specific record failed with exception: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    override fun consultarNumeroDeRecords(j: Juego): Int {
        logger.info("Consulting number of records for game ID: ${j.id}")
        return try {
            runBlocking {
                val response = client.get(recordsUrl) {
                    url {
                        parameters.append("juego_id", j.id.toString())
                    }
                }
                logger.info("Consult number of records response status: ${response.status}")
                
                if (!response.status.isSuccess()) {
                    logger.severe("Consult number of records request failed with status: ${response.status}")
                    return@runBlocking 0
                }
                
                val recordDTOs: List<RecordDTO> = response.body()

                logger.info("Found ${recordDTOs.size} records for game ID: ${j.id}")
                recordDTOs.size
            }
        } catch (e: Exception) {
            logger.severe("Consult number of records failed with exception: ${e.message}")
            e.printStackTrace()
            0
        }
    }
}