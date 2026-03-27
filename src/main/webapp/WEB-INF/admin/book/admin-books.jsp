<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-12
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.BookDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Management</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f4f7f6; }
        .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .search-bar { display: flex; gap: 8px; align-items: center; }
        .search-input { padding: 8px 10px; border: 1px solid #ddd; border-radius: 5px; width: 350px; }
        .btn-search { background: #333; }
        h2 { color: red; }

        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table th, table td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #333; }

        .action-group { display: flex; gap: 5px; align-items: center; flex-wrap: nowrap }
        .action-group .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            white-space: nowrap;
        }
        .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-add { background: #27ae60; }
        .btn-edit { background: #2980b9; margin-right: 5px; }
        .btn-copy { background: #8e44ad; }
        .btn-delete { background: #c0392b; }

        .status-tag { padding: 4px 8px; border-radius: 12px; font-size: 12px; background: #e0e0e0; }
    </style>
</head>
<body>
    <%-- Top navigation for admin area --%>
    <nav class="navbar">
        <div class="logo">Library Management System</div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1;">Logout</a></div>
    </nav>

    <div class="container">
        <div class="card">
            <%-- Page header and action buttons --%>
            <div class="header-actions">
                <h2>Book Management</h2>
                <%-- Search form sends GET query to the same servlet endpoint --%>
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/books">
                    <input type="text" name="search" class="search-input" placeholder="Search..." value="${param.search}">
                    <button type="submit" class="btn btn-search">Search</button>
                </form>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/books?action=add-book" class="btn btn-add">Add New Book</a>
                    <a href="${pageContext.request.contextPath}/admin" class="btn">Back to Dashboard</a>
                </div>
            </div>
        <%
            // Error is set by servlet when validation/business rule fails.
            String error = (String) request.getAttribute("error");
            if(error != null) { %>
        <%-- Inline alert + banner both show the same server-side message --%>
        <script>alert('<%= error %>');</script>
        <div class="error">
            <%= error %>
        </div>
        <%  } %>
            <%-- Book list table --%>
            <table>
                <thead>
                    <tr>
                        <th>ISBN</th>
                        <th>Title</th>
                        <th>Date Acquired</th>
                        <th>Description</th>
                        <th>Author</th>
                        <th>Publisher</th>
                        <th>Inventory (Avail/Total)</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    // Data is prepared by AdminBooksServlet as request attribute "bookList".
                    List<BookDTO> books = (List<BookDTO>) request.getAttribute("bookList");
                    if(books != null && !books.isEmpty()){
                        for (BookDTO book : books){
                %>
                <tr>
                    <td><%= book.getIsbn() %></td>
                    <td><%= book.getTitle() %></td>
                    <td><%= book.getDateAcquired() %></td>
                    <td><%= book.getDescription() %></td>
                    <td><%= book.getAuthorName() %></td>
                    <td><%= book.getPublisherName() %></td>
                    <td>
                        <span style="color: <%= book.getAvailableCopies() > 0 ? "green" : "red" %>; font-weight: bold;">
                            <%= book.getAvailableCopies() %>
                        </span>
                        / <%= book.getTotalCopies() %>
                    </td>
                    <td>
                        <div class="action-group">
                            <%-- Edit action loads edit form with selected ISBN --%>
                            <a href="${pageContext.request.contextPath}/admin/books?action=update&isbn=<%= book.getIsbn() %>" class="btn btn-edit">Edit</a>
                            <%
                                // Escape single quote for JavaScript confirm dialog.
                                String title = book.getTitle();
                                String safeTitle = title == null ? "" : title.replace("'", "\\'");
                                // Build inline confirm message and delete URL using ISBN key.
                                String confirmMsg = "return confirm('Are you sure to delete { " + safeTitle + " } ? (ISBN: " + book.getIsbn() + ")');";
                                String deleteUrl = request.getContextPath() + "/admin/books?action=delete&isbn=" + book.getIsbn();
                            %>
                            <a href="<%= deleteUrl %>" class="btn btn-delete" onclick="<%= confirmMsg %>">Delete</a>

                            <%-- Manage copies action loads manage form with selected ISBN --%>
                            <a href="${pageContext.request.contextPath}/admin/book-copies?isbn=<%= book.getIsbn() %>"
                               class="btn btn-copy">Manage Copies</a>
                        </div>
                    </td>
                </tr>
                <%
                        }
                    } else {
                        // Render empty-state row when there is no data.
                %>
                <tr>
                    <td colspan="8" style="text-align: center; color: #7f8c8d; padding: 40px;">No book info is found</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
