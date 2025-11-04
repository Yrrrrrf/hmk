package tc2.thing.hmk.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession

@WebServlet(name = "logoutServlet", value = ["/logout"])
class LogoutServlet : HttpServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        // Invalidate session
        val session: HttpSession = request.session
        session.invalidate()
        
        // Redirect to home/login
        response.sendRedirect("/login")
    }
    
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        // Invalidate session
        val session: HttpSession = request.session
        session.invalidate()
        
        // Redirect to home/login
        response.sendRedirect("/login")
    }
}