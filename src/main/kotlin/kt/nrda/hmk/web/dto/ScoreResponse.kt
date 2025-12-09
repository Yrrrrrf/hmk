package kt.nrda.hmk.web.dto

import java.time.LocalDateTime

data class ScoreResponse(
    val username: String,
    val score: Int,
    val playedAt: LocalDateTime?
)
