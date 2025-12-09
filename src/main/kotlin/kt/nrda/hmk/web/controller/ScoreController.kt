package kt.nrda.hmk.web.controller

import kt.nrda.hmk.service.ScoreService
import kt.nrda.hmk.web.dto.ScoreResponse
import kt.nrda.hmk.web.dto.ScoreSubmission
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scores")
class ScoreController(private val scoreService: ScoreService) {

    @PostMapping
    fun submitScore(@RequestBody submission: ScoreSubmission): ResponseEntity<ScoreResponse> {
        val score = scoreService.submitScore(submission.userId, submission.gameId, submission.score)
        val response = ScoreResponse(
            username = score.user.login,
            score = score.score,
            playedAt = score.playedAt
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/top")
    fun getTopScores(
        @RequestParam(defaultValue = "1") gameId: Long,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<List<ScoreResponse>> {
        val scores = scoreService.getTopScores(gameId, limit)
        val response = scores.map {
            ScoreResponse(
                username = it.user.login,
                score = it.score,
                playedAt = it.playedAt
            )
        }
        return ResponseEntity.ok(response)
    }
}
