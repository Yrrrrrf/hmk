<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - How Many Burgers!</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container register-form">
        <h2>Register - How Many Burgers!</h2>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>
        
        <%-- MODIFIED: Added context path to form action --%>
        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="form-group">
                <label for="login">Username:</label>
                <input type="text" id="login" name="login" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <div class="form-group">
                <label for="correo">Email (optional):</label>
                <input type="email" id="correo" name="correo">
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