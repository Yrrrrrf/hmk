package kt.nrda.hmk.service

import kt.nrda.hmk.domain.model.User
import kt.nrda.hmk.domain.repository.UserRepository
import kt.nrda.hmk.web.dto.UserForm
import org.springframework.stereotype.Service

@Service
class AuthService(private val userRepository: UserRepository) {

    fun register(form: UserForm): User {
        // Business Rule: Check if user exists (Optional, repo handles unique constraint too)
        if (userRepository.findByLogin(form.login) != null) {
            throw IllegalArgumentException("Username '${form.login}' is already taken.")
        }

        // Create new User Domain Object
        val newUser = User(
            login = form.login,
            password = form.password, // In a real app, hash this!
            email = form.email
        )

        return userRepository.save(newUser)
    }

    fun login(form: UserForm): User? {
        val user = userRepository.findByLogin(form.login) ?: return null
        
        // Simple password check (matches your old logic)
        return if (user.password == form.password) user else null
    }
}
