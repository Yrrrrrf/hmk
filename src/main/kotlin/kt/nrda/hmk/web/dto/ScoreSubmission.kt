package kt.nrda.hmk.web.dto

data class ScoreSubmission(
    // userId is removed for security; we get it from the session
    val gameId: Long,
    val score: Int
)