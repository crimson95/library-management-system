<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-26
  Time: 12:33
  To change this template use File | Settings | File Templates.

  Description: Admin form view for editing an existing book copy.
  Allows changing the condition and status of a specific physical copy.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.book.BookDTO" %>
<%@ page import="DTO.book.BookInfoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Edit Book Copy</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; display: flex; align-items: center; justify-content: center; }
        .card { width: 100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
        h2 { color: red; display: block; text-align: center; }
        p { color: #666; margin-top: 5px; }
        select { width: 100%; padding: 8px; }
        .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-secondary { background: #333; }
    </style>
</head>
<body>
<nav class="navbar">
    <div class="logo">Library Management System</div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1;">Logout</a></div>
</nav>
<div class="container">
    <%-- Book and BookInfo objects loaded by servlet for pre-filling the form --%>
    <%
        BookDTO book = (BookDTO) request.getAttribute("book");
        BookInfoDTO copy = (BookInfoDTO) request.getAttribute("copy");
    %>

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <div class="error"><%= error %>></div>
    <% } %>

    <div class="card">
        <h2>Edit Book Copy</h2>
        <p>Editing physical copy for: <strong><%= book!= null ? book.getTitle() : "" %></strong>(ID: <%= copy != null ? copy.getBookID() : "" %>)</p>

        <form method="post" action="${pageContext.request.contextPath}/admin/book-copies">
            <%-- Hidden fields to maintain action state and identifiers --%>
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="isbn" value="<%= book != null ? book.getIsbn() : "" %>" />
            <input type="hidden" name="bookID" value="<%= copy != null ? copy.getBookID() : "" %>" />

            <div class="row">
                <%-- Dropdown for updating copy condition --%>
                <label for="condition">Condition</label>
                <select name="condition" id="condition">
                    <option value="New" <%= copy != null && "New".equals(copy.getCondition()) ? "selected" : "" %>>New</option>
                    <option value="Good" <%= copy != null && "Good".equals(copy.getCondition()) ? "selected" : "" %>>Good</option>
                    <option value="Worn" <%= copy != null && "Worn".equals(copy.getCondition()) ? "selected" : "" %>>Worn</option>
                    <option value="Damaged" <%= copy != null && "Damaged".equals(copy.getCondition()) ? "selected" : "" %>>Damaged</option>
                </select>
            </div>

            <div class="row">
                <%-- Dropdown for updating copy status --%>
                <label for="status">Status</label>
                <select name="status" id="status">
                    <option value="1" <%= copy!= null && copy.getStatus() == 1 ? "selected" : "" %>>Available</option>
                    <option value="2" <%= copy!= null && copy.getStatus() == 2 ? "selected" : "" %>>Repairing</option>
                </select>
            </div>

            <div class="btn-row" style="margin-top: 20px;">
                <button class="btn" type="submit">Update Copy</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/book-copies?isbn=<%= book != null ? book.getIsbn() : "" %>">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
