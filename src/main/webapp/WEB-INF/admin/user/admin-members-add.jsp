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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <%-- Top navigation bar --%>
    <nav class="navbar">
        <div class="logo">Library Management System</div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
    </nav>

    <div class="container">
        <div class="form-container">
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

                <div class="form-row">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" name="username" id="username" maxlength="20" placeholder="your username">
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" name="password" id="password" maxlength="20" placeholder="at least 8 digits, including alphabet and number">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="first_name">First Name</label>
                        <input type="text" name="first_name" id="first_name" maxlength="15" placeholder="your first name">
                    </div>
                    <div class="form-group">
                        <label for="last_name">Last Name</label>
                        <input type="text" name="last_name" id="last_name" maxlength="15" placeholder="your last name">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="text" name="phone" id="phone" maxlength="15" placeholder="(123)123-1234">
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" name="email" id="email" maxlength="50" placeholder="xxx@xxx.xxx">
                    </div>
                </div>

                <div class="form-action">
                    <button class="btn" type="submit">Create Admin</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/members">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
