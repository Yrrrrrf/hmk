package kt.nrda.hmk.web.dto

data class UserResponse(
    val id: Long?,
    val login: String,
    val email: String?
)
