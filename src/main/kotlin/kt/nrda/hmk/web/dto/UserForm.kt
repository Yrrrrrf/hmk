package kt.nrda.hmk.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserForm(
    @field:NotBlank(message = "Username is required")
    val login: String = "",

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 3, message = "Password must be at least 3 characters")
    val password: String = "",
    
    val email: String? = null
)
