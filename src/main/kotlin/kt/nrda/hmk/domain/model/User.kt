// src/main/kotlin/kt/nrda/hmk/domain/model/User.kt
package kt.nrda.hmk.domain.model

data class User(
    val id: Long? = null,
    val login: String,
    val email: String?,
    // We do NOT map the password here for security in the domain layer usually,
    // but for this simple migration, we will keep it but treat it carefully.
    val password: String 
)
