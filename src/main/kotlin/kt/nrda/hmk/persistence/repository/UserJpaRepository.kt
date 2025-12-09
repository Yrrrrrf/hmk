// src/main/kotlin/kt/nrda/hmk/persistence/repository/UserJpaRepository.kt
package kt.nrda.hmk.persistence.repository

import kt.nrda.hmk.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByLogin(login: String): UserEntity?
}
