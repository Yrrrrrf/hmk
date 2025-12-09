package kt.nrda.hmk.web.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller // Use @Controller for HTML/Thymeleaf, @RestController for JSON
class HomeController {

    @GetMapping("/")
    fun welcome(model: Model): String {
        // We will pass some data to the view to prove Kotlin is running
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        
        model.addAttribute("appName", "How Many Krabby Patties?")
        model.addAttribute("serverTime", now)
        model.addAttribute("status", "ONLINE 🟢")
        
        // This looks for a file named "index.html" in src/main/resources/templates/
        return "index" 
    }
}
