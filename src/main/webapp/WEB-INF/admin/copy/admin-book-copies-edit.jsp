<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.book.BookDTO" %>
<%@ page import="DTO.book.BookInfoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Edit Book Copy</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<nav class="navbar">
    <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>
<div class="container">

    <%-- Book and BookInfo objects loaded by servlet for pre-filling the form --%>
    <%
        BookDTO book = (BookDTO) request.getAttribute("book");
        BookInfoDTO copy = (BookInfoDTO) request.getAttribute("copy");
    %>

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error");
        if(error != null) { %>
    <%-- Inline banner show the server-side message --%>
        <div class="error"> <%= error %></div>
    <% } %>

    <div class="form-container">
        <h2>Edit Book Copy</h2>
        <p>Editing physical copy for: <strong><%= book!= null ? book.getTitle() : "" %></strong>(ID: <%= copy != null ? copy.getBookID() : "" %>)</p>

        <form method="post" action="${pageContext.request.contextPath}/admin/book-copies">
            <%-- Hidden fields to maintain action state and identifiers --%>
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="isbn" value="<%= book != null ? book.getIsbn() : "" %>" />
            <input type="hidden" name="bookID" value="<%= copy != null ? copy.getBookID() : "" %>" />

            <div class="form-row">
                <div class="form-group">
                    <%-- Dropdown for updating copy condition --%>
                    <label for="condition">Condition</label>
                    <select name="condition" id="condition">
                        <option value="New" <%= copy != null && "New".equals(copy.getCondition()) ? "selected" : "" %>>New</option>
                        <option value="Good" <%= copy != null && "Good".equals(copy.getCondition()) ? "selected" : "" %>>Good</option>
                        <option value="Worn" <%= copy != null && "Worn".equals(copy.getCondition()) ? "selected" : "" %>>Worn</option>
                        <option value="Damaged" <%= copy != null && "Damaged".equals(copy.getCondition()) ? "selected" : "" %>>Damaged</option>
                    </select>
                </div>
                <div class="form-group">
                    <%-- Dropdown for updating copy status --%>
                    <label for="status">Status</label>
                    <select name="status" id="status">
                        <option value="1" <%= copy!= null && copy.getStatus() == 1 ? "selected" : "" %>>Available</option>
                        <option value="2" <%= copy!= null && copy.getStatus() == 2 ? "selected" : "" %>>Repairing</option>
                    </select>
                </div>
            </div>

            <div class="form-action">
                <button class="btn" type="submit">Confirm update</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/book-copies?isbn=<%= book != null ? book.getIsbn() : "" %>">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
