<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-30
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Add Publisher</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .container { padding: 30px; display: flex; align-items: center; justify-content: center; }
        .card { width: 100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
        h2 { color: red; display: block; text-align: center; }
    </style>
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
    <div class="logo">Library Management System</div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>

<div class="container">
    <div class="card">
        <h2>Add Publisher</h2>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
        if(error != null) { %>
        <%-- Inline alert + banner both show the same server-side message --%>
            <script>alert('<%= error %>');</script>
            <div class="error"> <%= error %></div>
        <% } %>

        <%-- Add-publisher form: submits to AdminAuthorsServlet (POST + action=add) --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/publishers">
            <input type="hidden" name="action" value="add" />

            <div class="row">
               <label for="publisher">Publisher Name</label>
                <input type="text" name="publisher_name" id="publisher_name" maxlength="30" placeholder="Enter Publisher" />
            </div>

            <div class="btn-row">
                <%-- Submit creates a new publisher; Cancel returns to list page --%>
                    <button class="btn" type="submit">Create Publisher</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/publishers">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
