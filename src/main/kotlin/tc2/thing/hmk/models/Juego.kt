package tc2.thing.hmk.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Representa un juego en el sistema.
 * Usamos 'data class' porque nos proporciona automáticamente
 * getters, setters, equals(), hashCode() y toString().
 *
 * @property id El identificador único del juego.
 * @property nombre El nombre del juego.
 */
@Serializable
data class Juego(
    val id: Long,
    val nombre: String
)
