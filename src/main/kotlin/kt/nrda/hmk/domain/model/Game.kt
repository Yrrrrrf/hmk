package kt.nrda.hmk.domain.model

import java.time.LocalDateTime

data class Game(
    val id: Long? = null,
    val name: String,
    val createdAt: LocalDateTime? = null
)
