<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-09
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="utf-8">
    <title>Register</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; display: flex; justify-content: center; align-items: center; }
        h2 { color: red; display: block; text-align: center; }
        .card { width: 100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
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
    <h2>Register</h2>

    <% String error = (String) request.getAttribute("error");
    if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/register" accept-charset="UTF-8">
        <div class="row">
            <label for="username">Username: </label>
            <input type="text" name="username" id="username" value="${param.username}" placeholder="your username" maxlength="20">
        </div>
        <div class="row">
            <label for="password">Password: </label>
            <input type="password" name="password" id="password" placeholder="at least 8 characters, including alphabet and number" maxlength="20">
        </div>
        <div class="row">
            <label for="first_name">First name: </label>
            <input type="text" name="first_name" id="first_name" value="${param.first_name}" placeholder="your first name" maxlength="15">
        </div>
        <div class="row">
            <label for="last_name">Last name: </label>
            <input type="text" name="last_name" id="last_name" value="${param.last_name}" placeholder="your last name" maxlength="15">
        </div>
        <div class="row">
            <label for="phone">Phone: </label>
            <input type="text" name="phone" id="phone" value="${param.phone}" placeholder="(123)123-1234" maxlength="15">
        </div>
        <div class="row">
            <label for="email">Email: </label>
            <input type="text" name="email" id="email" value="${param.email}" placeholder="xxx@xxx.xxx" maxlength="50">
        </div>

        <div class="btn-row">
            <button type="submit">Register</button>
        </div>
    </form>

    <p style="margin-top: 20px;">
        Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a> here.
    </p>
</body>
</html>
