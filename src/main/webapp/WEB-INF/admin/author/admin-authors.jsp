<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.AuthorDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Manage Authors</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <nav class="navbar">
        <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
    </nav>

    <div class="container">
        <% String successMsg = (String) request.getAttribute("successMessage");
            if (successMsg != null) { %>
        <div class="success"><%= successMsg %></div>
        <% } %>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
        <%-- Inline banner show the server-side message --%>
        <div class="error"><%= error %>
        </div>
        <% } %>

        <div class="card">
            <div class="header-actions">
                <h2>Author Management</h2>
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/authors">
                    <input type="text" name="search" class="search-input" placeholder="Search..."
                           value="${param.search}">
                    <button type="submit" class="btn btn-search">Search</button>
                    <a href="?" class="btn btn-secondary">Clear</a>
                </form>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/authors?action=add" class="btn btn-add">Add New
                        Author</a>
                    <a href="${pageContext.request.contextPath}/admin/books" class="btn">Back to Book Management</a>
                </div>
            </div>

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
                    if (authors != null && !authors.isEmpty()) {
                        for (AuthorDTO author : authors) {
                %>
                <tr>
                    <td><%= author.getAuthorID() %>
                    </td>
                    <td><%= author.getFirst_name() %>
                    </td>
                    <td><%= author.getLast_name() %>
                    </td>
                    <td><strong><%= author.getFullName() %>
                    </strong></td>
                    <td>
                        <%-- Edit and Delete actions for author --%>
                        <a href="${pageContext.request.contextPath}/admin/authors?action=update&authorID=<%= author.getAuthorID() %>"
                           class="btn btn-edit">Edit</a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/authors"
                              style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="authorID" value="<%= author.getAuthorID() %>">
                            <button type="submit" class="btn btn-delete"
                                    onclick="return confirm('Delete author: <%= author.getFullName() %>?');">Delete</button>
                        </form>
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
