<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - How Many Burgers!</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container register-form">
        <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDMwIDMwIj48Y2lyY2xlIGN4PSIxNSIgY3k9IjE1IiByPSIxMCIgZmlsbD0iIzRDQUY1MCIvPjx0ZXh0IHg9IjE1IiB5PSIxNyIgZm9udC1zaXplPSIxMCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPkkiPC90ZXh0Pjwvc3ZnPg==" alt="Burger Logo" style="display: block; margin: 0 auto 15px auto;">
        <h2>Register - How Many Burgers!</h2>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>
        
        <%-- MODIFIED: Added context path to form action --%>
        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="form-group">
                <label for="login">Username:</label>
                <input type="text" id="login" name="login" placeholder="Enter your username" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm your password" required>
            </div>
            <div class="form-group">
                <label for="correo">Email (optional):</label>
                <input type="email" id="correo" name="correo" placeholder="Enter your email (optional)">
            </div>
            <button type="submit">Register</button>
        </form>
        
        <div class="link">
            <%-- MODIFIED: Added context path to link href --%>
            <a href="${pageContext.request.contextPath}/login">Already have an account? Login here</a>
        </div>
    </div>
</body>
</html>