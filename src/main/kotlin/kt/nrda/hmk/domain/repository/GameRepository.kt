package kt.nrda.hmk.domain.repository

import kt.nrda.hmk.domain.model.Game

interface GameRepository {
    fun findByName(name: String): Game?
    fun findById(id: Long): Game?
    fun save(game: Game): Game
}
