package kt.nrda.hmk.persistence.repository

import kt.nrda.hmk.persistence.entity.ScoreEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoreJpaRepository : JpaRepository<ScoreEntity, Long> {
    fun findByGameIdOrderByScoreAsc(gameId: Long, pageable: Pageable): List<ScoreEntity>
}
