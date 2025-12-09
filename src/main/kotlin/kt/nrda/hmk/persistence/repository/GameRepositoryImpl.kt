package kt.nrda.hmk.persistence.repository

import kt.nrda.hmk.domain.model.Game
import kt.nrda.hmk.domain.repository.GameRepository
import kt.nrda.hmk.persistence.mapper.GameMapper
import org.springframework.stereotype.Repository

@Repository
class GameRepositoryImpl(
    private val jpaRepository: GameJpaRepository,
    private val mapper: GameMapper
) : GameRepository {
    override fun findByName(name: String): Game? {
        val entity = jpaRepository.findByName(name)
        return entity?.let { mapper.toDomain(it) }
    }

    override fun findById(id: Long): Game? {
        val entity = jpaRepository.findById(id).orElse(null)
        return entity?.let { mapper.toDomain(it) }
    }

    override fun save(game: Game): Game {
        val entity = mapper.toEntity(game)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }
}
