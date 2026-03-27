<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - LMS</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; display: flex; justify-content: center; align-items: center; }
        h2 { color: red; display: block; text-align: center; }
        .card { width:100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
        .row { margin-bottom: 12px; }
        label { display: block; margin-bottom: 6px; }
        input { width: 100%; padding: 8px; box-sizing: border-box; }
        .error { color: #b00020; margin-bottom: 12px; }
        .btn-row { display:flex; justify-content:center; margin-top: 12px; }
        button { padding: 10px 15px; color:#fff; background-color: #e40a0a; border: 1px solid #ff0000; border-radius: 5px; }
    </style>
</head>
<body>
<div class="card">
    <h2>User Login</h2>

    <% String error = (String) request.getAttribute("error");
    if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="row">
            <label for="username">Username</label>
            <input id="username" name="username" type="text" required maxlength="20">
        </div>

        <div class="row">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" required maxlength="20">
        </div>

        <div class="btn-row">
            <button type="submit">Login</button>
        </div>
    </form>

    <p style="margin-top: 20px;">
        Not have an account? <a href="${pageContext.request.contextPath}/register.jsp">Register</a>
    </p>
</div>
</body>
</html>