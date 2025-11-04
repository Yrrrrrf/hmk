package tc2.thing.hmk.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import tc2.thing.hmk.dao.RecordsDAOImpl
import tc2.thing.hmk.dao.UsuariosDAOImpl
import tc2.thing.hmk.models.Juego
import tc2.thing.hmk.models.Record
import tc2.thing.hmk.models.Usuario
import java.util.Date

@WebServlet(name = "gameServlet", value = ["/game"])
class GameServlet : HttpServlet() {
    private val recordsDAO = RecordsDAOImpl()
    private val usuariosDAO = UsuariosDAOImpl()
    private val juego = Juego(id = 1, nombre = "How Many Burgers")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val session: HttpSession = request.session
        val isLoggedIn = session.getAttribute("isLoggedIn") == true
        
        if (!isLoggedIn) {
            // Redirect to login if not logged in
            response.sendRedirect("/login")
            return
        }

        // Get top scores
        val topScores = recordsDAO.getTopScores(juego.id, 10)
        request.setAttribute("topScores", topScores)
        
        // Get current user's best score if logged in
        val usuario = session.getAttribute("usuario") as? Usuario
        if (usuario != null) {
            val userRecord = recordsDAO.consultar(juego, usuario)
            request.setAttribute("userBestScore", userRecord)
        }

        // Forward to game page
        request.getRequestDispatcher("/game.jsp").forward(request, response)
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val session: HttpSession = request.session
        val isLoggedIn = session.getAttribute("isLoggedIn") == true
        
        if (!isLoggedIn) {
            response.sendRedirect("/login")
            return
        }

        try {
            val scoreStr = request.getParameter("score")
            val score = scoreStr?.toIntOrNull()

            if (score == null) {
                request.setAttribute("errorMessage", "Invalid score")
                request.getRequestDispatcher("/game.jsp").forward(request, response)
                return
            }

            val usuario = session.getAttribute("usuario") as? Usuario
            if (usuario == null) {
                response.sendRedirect("/login")
                return
            }

            // Create and save the record
            val record = Record(
                usuario = usuario,
                juego = juego,
                puntaje = score,
                fecha = Date()
            )

            recordsDAO.crear(record)

            // Redirect back to the game page
            response.sendRedirect("/game")
        } catch (e: Exception) {
            request.setAttribute("errorMessage", e.message ?: "Error saving score")
            request.getRequestDispatcher("/game.jsp").forward(request, response)
        }
    }
}