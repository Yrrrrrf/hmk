package kt.nrda.hmk.domain.repository

import kt.nrda.hmk.domain.model.Score

interface ScoreRepository {
    fun save(score: Score): Score
    fun findTopScores(gameId: Long, limit: Int): List<Score>
}
