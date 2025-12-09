package kt.nrda.hmk.persistence.repository

import kt.nrda.hmk.domain.model.Score
import kt.nrda.hmk.domain.repository.ScoreRepository
import kt.nrda.hmk.persistence.mapper.ScoreEntityMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class ScoreRepositoryImpl(
    private val jpaRepository: ScoreJpaRepository,
    private val mapper: ScoreEntityMapper
) : ScoreRepository {

    override fun save(score: Score): Score {
        val entity = mapper.toEntity(score)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findTopScores(gameId: Long, limit: Int): List<Score> {
        val entities = jpaRepository.findByGameIdOrderByScoreAsc(gameId, PageRequest.of(0, limit))
        return mapper.toDomainList(entities)
    }
}
