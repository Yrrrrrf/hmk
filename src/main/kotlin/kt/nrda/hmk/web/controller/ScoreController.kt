package kt.nrda.hmk.web.controller

import jakarta.servlet.http.HttpSession
import kt.nrda.hmk.domain.model.User
import kt.nrda.hmk.service.ScoreService
import kt.nrda.hmk.web.dto.ScoreResponse
import kt.nrda.hmk.web.dto.ScoreSubmission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scores")
class ScoreController(private val scoreService: ScoreService) {

    @PostMapping
    fun submitScore(
        @RequestBody submission: ScoreSubmission,
        session: HttpSession
    ): ResponseEntity<*> {
        // Securely retrieve the user from the session
        val user = session.getAttribute("user") as? User
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Not logged in"))

        // user.id should not be null if it came from the DB
        val userId = user.id ?: return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Invalid user state"))

        val score = scoreService.submitScore(userId, submission.gameId, submission.score)
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
