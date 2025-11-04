package tc2.thing.hmk
import jakarta.servlet.http.*
import jakarta.servlet.annotation.*

@WebServlet(name = "helloServlet", value = ["/hello-servlet"])
class HelloServlet : HttpServlet() {
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        // Redirect to login page
        response.sendRedirect("login")
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        // Redirect to login page
        response.sendRedirect("login")
    }
}