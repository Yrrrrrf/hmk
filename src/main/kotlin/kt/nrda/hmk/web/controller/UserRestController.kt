package kt.nrda.hmk.web.controller

import kt.nrda.hmk.domain.repository.UserRepository
import kt.nrda.hmk.web.dto.UserResponse
import kt.nrda.hmk.web.mapper.UserDtoMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserRestController(
    private val userRepository: UserRepository,
    private val userDtoMapper: UserDtoMapper
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userRepository.findAll()
        return ResponseEntity.ok(userDtoMapper.toResponseList(users))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
