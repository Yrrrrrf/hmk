package tc2.thing.hmk.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import tc2.thing.hmk.dao.UsuariosDAOImpl
import tc2.thing.hmk.models.Usuario

@WebServlet(name = "loginServlet", value = ["/login"])
class LoginServlet : HttpServlet() {
    private val usuariosDAO = UsuariosDAOImpl()

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val login = request.getParameter("login")
        val password = request.getParameter("password")

        if (login.isNullOrEmpty() || password.isNullOrEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required")
            request.getRequestDispatcher("/login.jsp").forward(request, response)
            return
        }

        val usuario = usuariosDAO.authenticate(login, password)

        if (usuario != null) {
            val session: HttpSession = request.session
            session.setAttribute("usuario", usuario)
            session.setAttribute("isLoggedIn", true)

            // MODIFIED: Use contextPath for the redirect
            response.sendRedirect(request.contextPath + "/game")
        } else {
            request.setAttribute("errorMessage", "Invalid username or password")
            request.getRequestDispatcher("/login.jsp").forward(request, response)
        }
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val session = request.session
        if (session.getAttribute("isLoggedIn") == true) {
            // MODIFIED: Use contextPath for the redirect
            response.sendRedirect(request.contextPath + "/game")
        } else {
            request.getRequestDispatcher("/login.jsp").forward(request, response)
        }
    }
}