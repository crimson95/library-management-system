<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-25
  Time: 16:32
  To change this template use File | Settings | File Templates.

  Description: Admin form view for adding a new book copy.
  Allows specifying the initial condition and status of the new physical copy.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.book.BookDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Add Book Copy</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<nav class="navbar">
    <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>
<div class="container">

    <%-- Book object loaded by servlet for pre-filling the form --%>
    <% BookDTO book = (BookDTO) request.getAttribute("book"); %>

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error");
    if(error != null) { %>
    <%-- Inline banner show the server-side message --%>
        <div class="error"> <%= error %></div>
    <% } %>

    <div class="form-container">
        <h2>Add Book Copy</h2>
        <p>Adding a new physical copy for: <strong><%= book != null && book.getTitle() != null ? book.getTitle() : "" %></strong></p>

        <form method="post" action="${pageContext.request.contextPath}/admin/book-copies">
            <%-- Hidden fields to maintain action state and book identifier --%>
            <input type="hidden" name="action" value="add" />
            <input type="hidden" name="isbn" value="<%= book != null && book.getIsbn() != null ? book.getIsbn() : "" %>" />

            <div class="form-row">
                <div class="form-group">
                    <%-- Dropdown for selecting initial copy condition --%>
                    <label for="condition">Condition</label>
                    <select name="condition" id="condition">
                        <option value="New">New</option>
                        <option value="Good">Good</option>
                        <option value="Worn">Worn</option>
                        <option value="Damaged">Damaged</option>
                    </select>
                </div>

                <div class="form-group">
                    <%-- Dropdown for selecting initial copy status --%>
                    <label for="status">Status</label>
                    <select name="status" id="status">
                        <option value="1">Available</option>
                        <option value="2">Repairing</option>
                    </select>
                </div>
            </div>

            <div class="form-action">
                <button class="btn" type="submit">Save Copy</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/book-copies?isbn=<%= book != null ? book.getIsbn() : "" %>">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
