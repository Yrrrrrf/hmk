<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - How Many Burgers!</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container login-form">
        <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDMwIDMwIj48Y2lyY2xlIGN4PSIxNSIgY3k9IjE1IiByPSIxMCIgZmlsbD0iIzRDQUY1MCIvPjx0ZXh0IHg9IjE1IiB5PSIxNyIgZm9udC1zaXplPSIxMCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPkkiPC90ZXh0Pjwvc3ZnPg==" alt="Burger Logo" style="display: block; margin: 0 auto 15px auto;">
        <h2>Login - How Many Burgers!</h2>
        
        <%-- MODIFIED: Check session for success message after registration redirect --%>
        <% if (session.getAttribute("successMessage") != null) { %>
            <div class="success"><%= session.getAttribute("successMessage") %></div>
            <% session.removeAttribute("successMessage"); %>
        <% } %>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>
        
        <%-- MODIFIED: Added context path to form action --%>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label for="login">Username:</label>
                <input type="text" id="login" name="login" placeholder="Enter your username" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit">Login</button>
        </form>
        
        <%-- REMOVED: Health check test section is gone --%>
        
        <div class="link">
            <%-- MODIFIED: Added context path to link href --%>
            <a href="${pageContext.request.contextPath}/register">Don't have an account? Register here</a>
        </div>
    </div>
</body>
</html>