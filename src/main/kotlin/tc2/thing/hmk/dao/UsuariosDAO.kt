package tc2.thing.hmk.dao

import tc2.thing.hmk.models.Usuario

/**
 * Interfaz para el Acceso a Datos de Usuarios (DAO).
 * Define las operaciones CRUD (Crear, Leer, Actualizar, Borrar) para los usuarios.
 */
interface UsuariosDAO {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * @param u El objeto Usuario a crear.
     * @return El usuario creado, posiblemente con el ID asignado por la base de datos.
     */
    fun crear(u: Usuario): Usuario

    /**
     * Consulta todos los usuarios existentes.
     * @return Una lista de todos los usuarios.
     */
    fun consultar(): List<Usuario> // Es más idiomático en Kotlin usar List en lugar de ArrayList

    /**
     * Busca usuarios que coincidan con las propiedades del usuario proporcionado.
     * @param u Un objeto Usuario con los criterios de búsqueda.
     * @return Una lista de usuarios que coinciden.
     */
    fun consultar(u: Usuario): List<Usuario>

    /**
     * Actualiza la información de un usuario existente.
     * @param u El objeto Usuario con la información actualizada.
     * @return El usuario con los datos ya actualizados.
     */
    fun modificar(u: Usuario): Usuario

    /**
     * Elimina un usuario de la base de datos.
     * @param u El usuario a eliminar.
     * @return El número de registros eliminados (usualmente 1 si tuvo éxito).
     */
    fun borrar(u: Usuario): Int
    
    /**
     * Authenticates a user with login and password.
     * @param login The login name of the user.
     * @param password The password of the user.
     * @return The Usuario object if authentication is successful, null otherwise.
     */
    fun authenticate(login: String, password: String): Usuario?
}
