<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-18
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.user.UserDTO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit User Information</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; display: flex; align-items: center; justify-content: center; }
        .card { width: 100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
        h2 { color: red; display: block; text-align: center; }
        .row { margin-bottom: 12px; }
        label { display: block; margin-bottom: 6px; }
        input { width: 100%; padding: 8px; box-sizing: border-box; }
        input[readonly] { background: #f0f0f0; color: #666; cursor: not-allowed; }
        .btn-row { display: flex; gap: 10px; margin-top: 16px; }
        .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-secondary { background: #333; }
        .error { background: #ffeaa7; color: #d63031; padding: 12px; border-left: 5px solid #d63031; margin-bottom: 16px; font-weight: bold; border-radius: 4px; }
    </style>
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
    <div class="logo">Library Management System</div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1">Logout</a></div>
</nav>

<div class="container">
    <div class="card">
        <h2>Edit User Information</h2>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
        <div class="error"><%= error %></div>
        <% } %>

        <%-- User object loaded by servlet for pre-filling the form --%>
        <% UserDTO user = (UserDTO) request.getAttribute("user"); %>

        <%-- Update form: username is read-only key, other fields are editable --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/members">
            <input type="hidden" name="action" value="update" />

            <div class="row">
                <label for="username">Username</label>
                <input type="text" name="username" id="username" maxlength="20" value="<%= user != null ? user.getUsername() : "" %>" readonly>
            </div>
            <div class="row">
                <label for="password">Password</label>
                <input type="password" name="password" id="password" maxlength="20" value="">
            </div>
            <div class="row">
                <label for="first_name">First Name</label>
                <input type="text" name="first_name" id="first_name" maxlength="15" value="<%= user != null ? user.getFirstName() : "" %>">
            </div>
            <div class="row">
                <label for="last_name">Last Name</label>
                <input type="text" name="last_name" id="last_name" maxlength="15" value="<%= user != null ? user.getLastName() : "" %>">
            </div>
            <div class="row">
                <label for="email">Email</label>
                <input type="email" name="email" id="email" maxlength="50" value="<%= user != null ? user.getEmail() : "" %>">
            </div>
            <div class="row">
                <label for="phone">Phone</label>
                <input type="text" name="phone" id="phone" maxlength="15" value="<%= user != null ? user.getPhone() : "" %>">
            </div>

            <div class="btn-row">
                <button class="btn" type="submit">Confirm</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/members">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
