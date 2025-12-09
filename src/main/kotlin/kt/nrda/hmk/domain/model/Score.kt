package kt.nrda.hmk.domain.model

import java.time.LocalDateTime

data class Score(
    val id: Long? = null,
    val user: User,
    val game: Game,
    val score: Int,
    val playedAt: LocalDateTime? = null
)
