package tc2.thing.hmk.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient
import java.util.Date // Importamos la clase Date de Java

// This class represents the JSON object returned by the API
@Serializable
data class RecordDTO(
    val id: Long,
    @SerialName("usuario_id")
    val usuarioId: Long,
    @SerialName("juego_id")
    val juegoId: Long,
    val puntaje: Int,
    val fecha: String // API returns date as string
)

// This will be your primary domain model, which is what the servlets use
data class Record(
    var id: Long = 0,
    var usuario: Usuario,
    var juego: Juego,
    var puntaje: Int,
    var fecha: Date, // We can convert the string to a Date
    var posicion: Int? = null
)

// This class is for creating a new record via the API
@Serializable
data class CreateRecordDTO(
    @SerialName("usuario_id")
    val usuarioId: Long,
    @SerialName("juego_id")
    val juegoId: Long,
    val puntaje: Int
)
