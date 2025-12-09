// src/main/kotlin/kt/nrda/hmk/domain/repository/UserRepository.kt
package kt.nrda.hmk.domain.repository

import kt.nrda.hmk.domain.model.User

interface UserRepository {
    fun save(user: User): User
    fun findByLogin(login: String): User?
    fun findById(id: Long): User?
    fun findAll(): List<User>
    fun deleteById(id: Long)
}
