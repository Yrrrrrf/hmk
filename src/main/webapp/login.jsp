<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="tc2.thing.hmk.models.Usuario" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - How Many Burgers!</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            cursor: pointer;
            width: 100%;
        }
        button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
        .success {
            color: green;
            margin-bottom: 15px;
        }
        .link {
            text-align: center;
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <h2>Login - How Many Burgers!</h2>
    
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>
    
    <% if (request.getAttribute("successMessage") != null) { %>
        <div class="success"><%= request.getAttribute("successMessage") %></div>
    <% } %>
    
    <form method="post" action="login">
        <div class="form-group">
            <label for="login">Username:</label>
            <input type="text" id="login" name="login" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>
    
    <div class="link">
        <a href="register">Don't have an account? Register here</a>
    </div>
</body>
</html>