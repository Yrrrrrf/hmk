package kt.nrda.hmk.web.mapper

import kt.nrda.hmk.domain.model.User
import kt.nrda.hmk.web.dto.UserResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserDtoMapper {
    fun toResponse(user: User): UserResponse
    fun toResponseList(users: List<User>): List<UserResponse>
}
