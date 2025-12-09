package kt.nrda.hmk.web.dto

data class ScoreSubmission(
    val userId: Long,
    val gameId: Long,
    val score: Int
)
