package kt.nrda.hmk.persistence.repository

import kt.nrda.hmk.persistence.entity.GameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameJpaRepository : JpaRepository<GameEntity, Long> {
    fun findByName(name: String): GameEntity?
}
