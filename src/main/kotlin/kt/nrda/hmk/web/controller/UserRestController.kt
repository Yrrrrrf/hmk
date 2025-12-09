package kt.nrda.hmk.web.controller

import kt.nrda.hmk.domain.model.User
import kt.nrda.hmk.service.AuthService
import kt.nrda.hmk.web.dto.UserResponse
import kt.nrda.hmk.web.mapper.UserDtoMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserRestController(
    private val authService: AuthService,
    private val userDtoMapper: UserDtoMapper
) {

    @GetMapping("/all")
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = authService.findAll()
        return ResponseEntity.ok(userDtoMapper.toResponseList(users))
    }

    @PostMapping("/save")
    fun saveUser(@RequestBody user: User): ResponseEntity<UserResponse> {
        val savedUser = authService.save(user)
        return ResponseEntity.ok(userDtoMapper.toResponse(savedUser))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        authService.delete(id)
        return ResponseEntity.noContent().build()
    }
}