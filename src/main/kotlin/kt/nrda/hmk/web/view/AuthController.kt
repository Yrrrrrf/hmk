package kt.nrda.hmk.web.view

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import kt.nrda.hmk.service.AuthService
import kt.nrda.hmk.web.dto.UserForm
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class AuthController(private val authService: AuthService) {

    @GetMapping("/login")
    fun loginPage(model: Model): String {
        model.addAttribute("userForm", UserForm())
        return "login" // Renders login.html
    }

    @PostMapping("/login")
    fun performLogin(
        @ModelAttribute("userForm") form: UserForm,
        session: HttpSession,
        model: Model
    ): String {
        val user = authService.login(form)
        if (user != null) {
            session.setAttribute("user", user)
            return "redirect:/" // Success! Go home
        }
        model.addAttribute("error", "Invalid username or password")
        return "login"
    }

    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("userForm", UserForm())
        return "register" // Renders register.html
    }

    @PostMapping("/register")
    fun performRegister(
        @Valid @ModelAttribute("userForm") form: UserForm,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            return "register"
        }

        try {
            authService.register(form)
            return "redirect:/login?registered=true"
        } catch (e: Exception) {
            model.addAttribute("error", e.message)
            return "register"
        }
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login"
    }
}
