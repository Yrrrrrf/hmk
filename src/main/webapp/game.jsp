<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="tc2.thing.hmk.models.Usuario" %>
<%@ page import="tc2.thing.hmk.models.Record" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>How Many Burgers!</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <div class="game-header">
            <h1><img src="${pageContext.request.contextPath}/images/hamburger.png" alt="Burger Logo" style="vertical-align: middle; margin-right: 10px; width: 30px; height: 30px;">How Many Burgers! <img src="${pageContext.request.contextPath}/images/hamburger.png" alt="Hamburger Icon" style="vertical-align: middle; margin-left: 10px; width: 30px; height: 30px;"></h1>
            <div class="game-user-info">
                <p>Welcome, 
                <%
                    Usuario usuario = (Usuario) session.getAttribute("usuario");
                    if (usuario != null) {
                        out.print(usuario.getLogin());
                    }
                %>
                </p>
                <%-- MODIFIED: Added context path to form action --%>
                <form method="post" action="${pageContext.request.contextPath}/logout" style="display: inline;">
                    <button type="submit">Logout</button>
                </form>
            </div>
        </div>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>
        
        <div class="game-area">
            <div class="game-score-display">Attempts: <span id="attempts">0</span></div>
            <div class="guessing-area">
                <input type="number" id="guessInput" min="1" max="100" placeholder="Enter your guess (1-100)">
                <button onclick="makeGuess()">Guess</button>
            </div>
            <div class="feedback">
                <div id="feedbackText">Initial guess (malumorado)</div>
                <img id="initialImage" src="${pageContext.request.contextPath}/images/malumorado.png" style="display: block; max-width: 150px; margin: 10px auto;" alt="Initial guess">
                <img id="winImage" src="${pageContext.request.contextPath}/images/gordo.png" style="display:none; max-width: 200px; margin: 10px auto; display: block;" alt="You Win!">
                <img id="lowImage" src="${pageContext.request.contextPath}/images/enojado.jpg" style="display:none; max-width: 150px; margin: 10px auto; display: block;" alt="Enojado">
                <img id="highImage" src="${pageContext.request.contextPath}/images/golpe.png" style="display:none; max-width: 150px; margin: 10px auto; display: block;" alt="Golpe">
            </div>
        </div>
        <div class="game-controls">
            <button onclick="startGame()">New Game</button>
        </div>
    </div>
        
        <div class="scores-section">
            <div class="user-score">
                <h3>Your Scores</h3>
                <%
                    Record userRecord = (Record) request.getAttribute("userBestScore");
                    if (userRecord != null) {
                        out.print("<p>Score: " + userRecord.getPuntaje() + "</p>");
                        out.print("<p>Date: " + userRecord.getFecha() + "</p>");
                    } else {
                        out.print("<p>No scores yet. Play the game and submit your score!</p>");
                    }
                %>
            </div>
            
            <div class="top-scores">
                <h3>Global Scores</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Player</th>
                            <th>Score</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Record> topScores = (List<Record>) request.getAttribute("topScores");
                        if (topScores != null && !topScores.isEmpty()) {
                            int rank = 1;
                            for (Record record : topScores) {
                                out.print("<tr>");
                                out.print("<td>" + rank + "</td>");
                                out.print("<td>" + record.getUsuario().getLogin() + "</td>");
                                out.print("<td>" + record.getPuntaje() + "</td>");
                                out.print("<td>" + record.getFecha() + "</td>");
                                out.print("</tr>");
                                rank++;
                            }
                        } else {
                            out.print("<tr><td colspan='4'>No scores yet</td></tr>");
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        // Game state variables
        let secretNumber;
        let attempts = 0;
        let gameIsOver = false;
        
        // Initialize the game when the page loads
        window.onload = function() {
            startGame();
        };
        
        // Start a new game
        function startGame() {
            attempts = 0;
            gameIsOver = false;
            secretNumber = Math.floor(Math.random() * 100) + 1;
            document.getElementById('attempts').textContent = attempts;
            document.getElementById('feedbackText').textContent = 'Initial guess (malumorado)';
            document.getElementById('feedbackText').style.display = 'block';
            document.getElementById('initialImage').style.display = 'block';
            document.getElementById('winImage').style.display = 'none';
            document.getElementById('lowImage').style.display = 'none';
            document.getElementById('highImage').style.display = 'none';
            document.getElementById('guessInput').value = '';
            document.getElementById('guessInput').disabled = false;
            document.getElementById('guessInput').focus();
        }
        
        // Handle a guess from the player
        function makeGuess() {
            if (gameIsOver) {
                return; // Don't allow more guesses once the game is won
            }
            
            const guessInput = document.getElementById('guessInput');
            const guessValue = parseInt(guessInput.value);
            
            // Validate the input
            if (isNaN(guessValue) || guessValue < 1 || guessValue > 100) {
                document.getElementById('feedbackText').style.display = 'block';
                document.getElementById('feedbackText').textContent = 'Please enter a number between 1 and 100!';
                document.getElementById('initialImage').style.display = 'none';
                document.getElementById('lowImage').style.display = 'none';
                document.getElementById('highImage').style.display = 'none';
                document.getElementById('winImage').style.display = 'none';
                return;
            }
            
            // Increment attempts and update display
            attempts++;
            document.getElementById('attempts').textContent = attempts;
            
            if (guessValue < secretNumber) {
                // Too low feedback - display the lowImage (which now shows enojado.jpg)
                document.getElementById('feedbackText').style.display = 'none';
                document.getElementById('initialImage').style.display = 'none';
                document.getElementById('lowImage').style.display = 'block';
                document.getElementById('highImage').style.display = 'none';
                document.getElementById('winImage').style.display = 'none';
            } else if (guessValue > secretNumber) {
                // Too high feedback - display the highImage (which now shows golpe.png)
                document.getElementById('feedbackText').style.display = 'none';
                document.getElementById('initialImage').style.display = 'none';
                document.getElementById('lowImage').style.display = 'none';
                document.getElementById('highImage').style.display = 'block';
                document.getElementById('winImage').style.display = 'none';
            } else {
                // Correct guess! - Show the winImage (which now shows gordo.png)
                document.getElementById('feedbackText').style.display = 'none';
                document.getElementById('initialImage').style.display = 'none';
                document.getElementById('lowImage').style.display = 'none';
                document.getElementById('highImage').style.display = 'none';
                document.getElementById('winImage').style.display = 'block';
                gameIsOver = true;
                
                // Disable input and guess button
                guessInput.disabled = true;
                
                // Immediately call submitScore to automate the process
                setTimeout(submitScore, 1000); // Wait 1 second before submitting to show the win image
            }
        }
        
        // Submit the score (number of attempts)
        function submitScore() {
            if (attempts <= 0 || !gameIsOver) {
                alert('You must win the game first to submit your score!');
                return;
            }
            
            const form = document.createElement('form');
            form.method = 'post';
            // MODIFIED: Added context path to the form action in JavaScript
            form.action = '${pageContext.request.contextPath}/game';
            form.style.display = 'none';
            
            const scoreInput = document.createElement('input');
            scoreInput.type = 'hidden';
            scoreInput.name = 'score';
            scoreInput.value = attempts;
            form.appendChild(scoreInput);
            
            document.body.appendChild(form);
            form.submit();
        }
        
        // Allow user to press Enter to make a guess
        document.getElementById('guessInput').addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                makeGuess();
            }
        });
    </script>
</body>
</html>