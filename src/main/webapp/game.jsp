<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="tc2.thing.hmk.models.Usuario" %>
<%@ page import="tc2.thing.hmk.models.Record" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>How Many Burgers!</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .user-info {
            text-align: right;
        }
        .game-area {
            text-align: center;
            margin: 30px 0;
        }
        .burger-container {
            font-size: 48px;
            margin: 20px 0;
        }
        .score-display {
            font-size: 24px;
            margin: 10px 0;
        }
        .controls {
            margin: 20px 0;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            margin: 5px;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
        .scores-section {
            display: flex;
            gap: 20px;
            margin-top: 30px;
        }
        .user-score, .top-scores {
            flex: 1;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .error {
            color: red;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>How Many Burgers!</h1>
        <div class="user-info">
            <p>Welcome, 
            <%
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                if (usuario != null) {
                    out.print(usuario.getLogin());
                }
            %>
            </p>
            <form method="post" action="logout" style="display: inline;">
                <button type="submit">Logout</button>
            </form>
        </div>
    </div>
    
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>
    
    <div class="game-area">
        <div class="score-display">Current Score: <span id="currentScore">0</span></div>
        <div class="burger-container">
            <span id="burgers">🍔</span>
        </div>
        <div class="controls">
            <button onclick="addBurger()">Add Burger</button>
            <button onclick="resetGame()">Reset Game</button>
            <button onclick="submitScore()">Submit Score</button>
        </div>
    </div>
    
    <div class="scores-section">
        <div class="user-score">
            <h3>Your Best Score</h3>
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
            <h3>Top Scores</h3>
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

    <script>
        let currentScore = 0;
        let burgers = [];
        
        function addBurger() {
            currentScore++;
            document.getElementById('currentScore').textContent = currentScore;
            
            // Add a burger to the display
            const burgerContainer = document.getElementById('burgers');
            const newBurger = document.createElement('span');
            newBurger.textContent = '🍔';
            newBurger.style.display = 'inline-block';
            newBurger.style.margin = '2px';
            burgerContainer.appendChild(newBurger);
            burgers.push(newBurger);
        }
        
        function resetGame() {
            currentScore = 0;
            document.getElementById('currentScore').textContent = currentScore;
            
            // Clear all burgers
            const burgerContainer = document.getElementById('burgers');
            burgerContainer.innerHTML = '';
            burgers = [];
            
            // Add the first burger back
            burgerContainer.textContent = '🍔';
        }
        
        function submitScore() {
            if (currentScore <= 0) {
                alert('Your score must be greater than 0 to submit!');
                return;
            }
            
            // Create form and submit the score
            const form = document.createElement('form');
            form.method = 'post';
            form.action = 'game';
            form.style.display = 'none';
            
            const scoreInput = document.createElement('input');
            scoreInput.type = 'hidden';
            scoreInput.name = 'score';
            scoreInput.value = currentScore;
            form.appendChild(scoreInput);
            
            document.body.appendChild(form);
            form.submit();
        }
    </script>
</body>
</html>