<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Add Publisher</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
    <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>

<div class="container">

    <div class="form-container">
        <h2>Add Publisher</h2>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
        if(error != null) { %>
        <%-- Inline banner show the server-side message --%>
            <div class="error"> <%= error %></div>
        <% } %>

        <%-- Add-publisher form: submits to AdminAuthorsServlet (POST + action=add) --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/publishers">
            <input type="hidden" name="action" value="add" />

            <div class="form-row">
                <div class="form-group">
                    <label for="publisher">Publisher Name</label>
                    <input type="text" name="publisher_name" id="publisher_name" maxlength="30" placeholder="Enter Publisher" />
                </div>
            </div>

            <div class="form-action">
                <%-- Submit creates a new publisher; Cancel returns to list page --%>
                    <button class="btn" type="submit">Create Publisher</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/publishers">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
