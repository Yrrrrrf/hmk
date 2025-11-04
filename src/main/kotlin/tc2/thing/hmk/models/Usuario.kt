package tc2.thing.hmk.models

import kotlinx.serialization.Serializable

/**
 * Representa a un usuario en el sistema.
 *
 * @property id El identificador único del usuario. Por defecto es 0.
 * @property login El nombre de usuario para iniciar sesión.
 * @property password La contraseña del usuario.
 * @property correo La dirección de correo electrónico del usuario.
 */
@Serializable
data class Usuario(
    var id: Long = 0,
    var login: String,
    var password: String,
    var correo: String? = null // Hacemos el correo opcional (puede ser nulo)
) {
    /**
     * Constructor secundario para cumplir con el diagrama UML que
     * pedía un constructor con solo login y password.
     */
    constructor(login: String, password: String) : this(
        id = 0,
        login = login,
        password = password,
        correo = null
    )
}
