// src/main/kotlin/kt/nrda/hmk/persistence/repository/UserRepositoryImpl.kt
package kt.nrda.hmk.persistence.repository

import kt.nrda.hmk.domain.model.User
import kt.nrda.hmk.domain.repository.UserRepository
import kt.nrda.hmk.persistence.mapper.UserMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val mapper: UserMapper
) : UserRepository {

    override fun save(user: User): User {
        val entity = mapper.toEntity(user)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findByLogin(login: String): User? {
        val entity = jpaRepository.findByLogin(login)
        return entity?.let { mapper.toDomain(it) }
    }

    override fun findById(id: Long): User? {
        val entity = jpaRepository.findById(id).orElse(null)
        return entity?.let { mapper.toDomain(it) }
    }

    override fun findAll(): List<User> {
        return mapper.toDomainList(jpaRepository.findAll())
    }
}
