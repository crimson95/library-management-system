<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-18
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Admin User</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .navbar { background: #960000; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; display: flex; align-items: center; justify-content: center; }
        .card { width: 100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
        h2 { color: red; display: block; text-align: center; }
        .row { margin-bottom: 12px; }
        label { display: block; margin-bottom: 6px; }
        input { width: 100%; padding: 8px; box-sizing: border-box; }
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
            <h2>Add Admin User</h2>

            <%-- Show server-side validation error if present --%>
            <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
            <%-- Inline alert + banner both show the same server-side message --%>
                <script>alert('<%= error %>');</script>
                <div class="error"> <%= error %></div>
            <% } %>

            <%-- Add-admin form: submits to AdminMembersServlet (POST + action=add-admin) --%>
            <form method="post" action="${pageContext.request.contextPath}/admin/members">
                <input type="hidden" name="action" value="add-admin" />

                <div class="row">
                    <label for="username">Username</label>
                    <input type="text" name="username" id="username" maxlength="20" placeholder="your username">
                </div>
                <div class="row">
                    <label for="password">Password</label>
                    <input type="password" name="password" id="password" maxlength="20" placeholder="at least 8 characters, including alphabet and number">
                </div>
                <div class="row">
                    <label for="first_name">First Name</label>
                    <input type="text" name="first_name" id="first_name" maxlength="15" placeholder="your first name">
                </div>
                <div class="row">
                    <label for="last_name">Last Name</label>
                    <input type="text" name="last_name" id="last_name" maxlength="15" placeholder="your last name">
                </div>
                <div class="row">
                    <label for="email">Email</label>
                    <input type="email" name="email" id="email" maxlength="50" placeholder="xxx@xxx.xxx">
                </div>
                <div class="row">
                    <label for="phone">Phone</label>
                    <input type="text" name="phone" id="phone" maxlength="15" placeholder="(123)123-1234">
                </div>

                <div class="btn-row">
                    <button class="btn" type="submit">Create Admin</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/members">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
