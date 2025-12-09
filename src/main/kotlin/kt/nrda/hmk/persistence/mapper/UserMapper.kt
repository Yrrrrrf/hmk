// src/main/kotlin/kt/nrda/hmk/persistence/mapper/UserMapper.kt
package kt.nrda.hmk.persistence.mapper

import kt.nrda.hmk.domain.model.User
import kt.nrda.hmk.persistence.entity.UserEntity
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {
    
    fun toEntity(domain: User): UserEntity

    @InheritInverseConfiguration
    fun toDomain(entity: UserEntity): User

    fun toDomainList(entities: List<UserEntity>): List<User>
}
