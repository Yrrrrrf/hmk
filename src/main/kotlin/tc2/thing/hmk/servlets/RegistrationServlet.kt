package tc2.thing.hmk.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import tc2.thing.hmk.dao.UsuariosDAOImpl
import tc2.thing.hmk.models.Usuario

@WebServlet(name = "registrationServlet", value = ["/register"])
class RegistrationServlet : HttpServlet() {
    private val usuariosDAO = UsuariosDAOImpl()

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val login = request.getParameter("login")
        val password = request.getParameter("password")
        val confirmPassword = request.getParameter("confirmPassword")
        val correo = request.getParameter("correo")

        // Validate input
        if (login.isNullOrEmpty() || password.isNullOrEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required")
            request.getRequestDispatcher("/register.jsp").forward(request, response)
            return
        }

        if (password != confirmPassword) {
            request.setAttribute("errorMessage", "Passwords do not match")
            request.getRequestDispatcher("/register.jsp").forward(request, response)
            return
        }

        try {
            // Create new user
            val newUser = Usuario(
                login = login!!,
                password = password!!,
                correo = correo
            )

            // Save user to database
            val savedUser = usuariosDAO.crear(newUser)

            // Redirect to login page after successful registration
            request.setAttribute("successMessage", "Registration successful! Please log in.")
            request.getRequestDispatcher("/login.jsp").forward(request, response)
        } catch (e: Exception) {
            request.setAttribute("errorMessage", e.message ?: "Registration failed")
            request.getRequestDispatcher("/register.jsp").forward(request, response)
        }
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        // Show registration page
        request.getRequestDispatcher("/register.jsp").forward(request, response)
    }
}