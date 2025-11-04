package tc2.thing.hmk.dao

import tc2.thing.hmk.models.Juego
import tc2.thing.hmk.models.Record
import tc2.thing.hmk.models.Usuario

/**
 * Interfaz para el Acceso a Datos de Récords (DAO).
 * Define las operaciones para gestionar los récords de los juegos.
 */
interface RecordsDAO {

    /**
     * Inserta un nuevo récord en la base de datos.
     * @param r El objeto Record a crear.
     * @return El récord creado con su ID asignado.
     */
    fun crear(r: Record): Record

    /**
     * Consulta todos los récords de un juego específico, ordenados por puntaje.
     * @param j El juego del cual se quieren obtener los récords.
     * @return Una lista de récords para ese juego.
     */
    fun consultar(j: Juego): List<Record>

    /**
     * Consulta el récord específico de un usuario en un juego.
     * @param j El juego a consultar.
     * @param u El usuario a consultar.
     * @return El objeto Record si existe, o null si el usuario no tiene récord en ese juego.
     */
    fun consultar(j: Juego, u: Usuario): Record?

    /**
     * Consulta un récord por su ID u otras propiedades.
     * @param r Un objeto Record con los criterios de búsqueda.
     * @return El récord encontrado o null si no existe.
     */
    fun consultar(r: Record): Record?

    /**
     * Obtiene el número total de récords para un juego.
     * @param j El juego del cual se quiere contar los récords.
     * @return La cantidad de récords.
     */
    fun consultarNumeroDeRecords(j: Juego): Int
    
    /**
     * Gets the top scores for a game with a specified limit.
     * @param juegoId The ID of the game.
     * @param limit The maximum number of scores to return.
     * @return A list of the top records for the game.
     */
    fun getTopScores(juegoId: Long, limit: Int = 10): List<Record>
}
