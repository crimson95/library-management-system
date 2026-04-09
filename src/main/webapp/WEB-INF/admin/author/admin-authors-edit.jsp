<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-30
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.book.AuthorDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Edit Author</title>
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
        <h2>Edit Author</h2>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
        if(error != null) { %>
        <%-- Inline alert + banner both show the same server-side message --%>
            <script>alert('<%= error %>');</script>
            <div class="error"> <%= error %></div>
        <% } %>

        <%-- Author object loaded by servlet for pre-filling the form --%>
        <% AuthorDTO author = (AuthorDTO) request.getAttribute("author"); %>

        <%-- Update form: authorID is read-only key, other fields are editable --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/authors">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="authorID" value="<%= author != null ? author.getAuthorID() : "" %>" />

            <div class="form-row">
                <div class="form-group">
                    <label for="first_name">First Name</label>
                    <input type="text" name="first_name" id="first_name" maxlength="15" value="<%= author != null && author.getFirst_name() != null ? author.getFirst_name() : "" %>" />
                </div>
                <div class="form-group">
                    <label for="last_name">Last Name</label>
                    <input type="text" name="last_name" id="last_name" maxlength="15" value="<%= author != null && author.getLast_name() != null ? author.getLast_name() : "" %>">
                </div>
            </div>

            <div class="form-action">
                <%-- Confirm submits update; Cancel returns to list page --%>
                <button class="btn" type="submit">Confirm update</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/authors">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
