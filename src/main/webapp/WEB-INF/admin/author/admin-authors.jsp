<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-27
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.AuthorDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Manage Authors</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        h2 { color: red; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table th, table td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #333; }
        .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-add { background: #27ae60; }
        .btn-edit { background: #2980b9; margin-right: 5px; }
        .btn-delete { background: #c0392b; }
        .error { background: #ffeaa7; color: #d63031; padding: 12px; border-left: 5px solid #d63031; margin-bottom: 16px; font-weight: bold; border-radius: 4px; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo">Library Management System</div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1;">Logout</a></div>
    </nav>

<div class="container">
    <div class="card">
        <div class="header-actions">
            <h2>Author Management</h2>
            <div>
                <a href="${pageContext.request.contextPath}/admin/authors?action=add" class="btn btn-add">Add New Author</a>
                <a href="${pageContext.request.contextPath}/admin/books" class="btn">Back to Book Management</a>
            </div>
        </div>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
            if(error != null) { %>
        <%-- Inline alert + banner both show the same server-side message --%>
        <script>alert('<%= error %>');</script>
        <div class="error"> <%= error %></div>
        <% } %>

        <table>
            <thead>
                <tr>
                    <th>Author ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Full Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                // Retrieve the list of authors
                List<AuthorDTO> authors = (List<AuthorDTO>) request.getAttribute("authorList");
                if(authors != null && !authors.isEmpty()){
                    for(AuthorDTO author : authors){
            %>
            <tr>
                <td><%= author.getAuthorID() %></td>
                <td><%= author.getFirst_name() %></td>
                <td><%= author.getLast_name() %></td>
                <td><strong><%= author.getFullName() %></strong></td>
                <td>
                    <%-- Edit and Delete actions for author --%>
                    <a href="${pageContext.request.contextPath}/admin/authors?action=update&authorID=<%= author.getAuthorID() %>" class="btn btn-edit">Edit</a>
                    <a href="${pageContext.request.contextPath}/admin/authors?action=delete&authorID=<%= author.getAuthorID() %>" class="btn btn-delete"
                       onclick="return confirm('Delete author: <%= author.getFullName().replace("'","\\'") %>?');">Delete</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5" style="text-align: center; color: #7f8c8d; padding: 40px;">No authors found.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
