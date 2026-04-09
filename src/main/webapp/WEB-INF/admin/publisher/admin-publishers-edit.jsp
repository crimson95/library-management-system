<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-30
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.book.PublisherDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Edit Publisher</title>
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
        <h2>Edit Publisher</h2>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
        if(error != null) { %>
        <%-- Inline alert + banner both show the same server-side message --%>
            <script>alert('<%= error %>');</script>
            <div class="error"> <%= error %></div>
        <% } %>

        <%-- Publisher object loaded by servlet for pre-filling the form --%>
        <% PublisherDTO publisher = (PublisherDTO) request.getAttribute("publisher"); %>

        <%-- Update form: publisherID is read-only key, other fields are editable --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/publishers">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="publisherID" value="<%= publisher != null ? publisher.getPublisherID() : "" %>" />

            <div class="form-row">
                <div class="form-group">
                    <label for="publisher">Publisher Name</label>
                    <input type="text" name="publisher" id="publisher" maxlength="30" value="<%= publisher != null && publisher.getPublisherName() != null ? publisher.getPublisherName() : "" %>" />
                </div>
            </div>

            <div class="form-action">
                <%-- Confirm submits update; Cancel returns to list page --%>
                <button class="btn" type="submit">Confirm update</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/publishers">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
