package kt.nrda.hmk.service

import kt.nrda.hmk.domain.model.Score
import kt.nrda.hmk.domain.repository.GameRepository
import kt.nrda.hmk.domain.repository.ScoreRepository
import kt.nrda.hmk.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ScoreService(
    private val scoreRepository: ScoreRepository,
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository
) {

    @Transactional
    fun submitScore(userId: Long, gameId: Long, scoreValue: Int): Score {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("User with ID $userId not found")
        val game = gameRepository.findById(gameId)
            ?: throw IllegalArgumentException("Game with ID $gameId not found")

        val score = Score(
            user = user,
            game = game,
            score = scoreValue,
            playedAt = LocalDateTime.now()
        )
        return scoreRepository.save(score)
    }

    fun getTopScores(gameId: Long, limit: Int = 10): List<Score> {
        return scoreRepository.findTopScores(gameId, limit)
    }
}
