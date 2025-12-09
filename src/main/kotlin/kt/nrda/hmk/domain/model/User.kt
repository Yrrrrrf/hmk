// src/main/kotlin/kt/nrda/hmk/domain/model/User.kt
package kt.nrda.hmk.domain.model

data class User(
    val id: Long? = null,
    val login: String,
    val email: String? = null,
    val password: String 
)