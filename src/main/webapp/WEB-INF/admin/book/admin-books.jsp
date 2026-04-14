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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <%-- Top navigation for admin area --%>
    <nav class="navbar">
        <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
    </nav>

    <div class="container">
        <% String successMsg = (String) request.getAttribute("successMessage");
        if(successMsg != null) { %>
        <div class="success"><%= successMsg %></div>
        <% } %>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
            if(error != null) { %>
        <%-- Inline banner show the same server-side message --%>
        <div class="error"> <%= error %></div>
        <% } %>

        <div class="card">
            <%-- Page header and action buttons --%>
            <div class="header-actions">
                <h2>Book Management</h2>
                <%-- Search form sends GET query to the same servlet endpoint --%>
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/books">
                    <input type="text" name="search" class="search-input" placeholder="Search..." value="${param.search}">
                    <button type="submit" class="btn btn-search">Search</button>
                    <a href="?" class="btn btn-secondary">Clear</a>
                </form>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/books?action=add-book" class="btn btn-add">Add New Book</a>
                    <a href="${pageContext.request.contextPath}/admin/authors" class="btn" style="background: #8e44ad;">Manage Authors</a>
                    <a href="${pageContext.request.contextPath}/admin/publishers" class="btn" style="background: #f39c12;">Manage Publisher</a>
                    <a href="${pageContext.request.contextPath}/admin" class="btn">Back to Dashboard</a>
                </div>
            </div>

            <%-- Book list table --%>
            <table>
                <thead>
                    <tr>
                        <th style="width: 10%;">ISBN</th>
                        <th style="width: 20%;">Title</th>
                        <th style="width: 10%;">Date Acquired</th>
                        <th style="width: 30%;">Description</th>
                        <th style="width: 10%;">Author</th>
                        <th style="width: 5%;">Publisher</th>
                        <th style="width: 5%;">Inventory (Avail/Total)</th>
                        <th style="width: 10%;">Action</th>
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
                            <%-- Top row: Edit and Delete buttons --%>
                            <div class="action-row">
                                <a href="${pageContext.request.contextPath}/admin/books?action=update&isbn=<%= book.getIsbn() %>" class="btn btn-edit">Edit</a>
                                <%
                                    // Escape single quote for JavaScript confirm dialog.
                                    String title = book.getTitle();
                                    String safeTitle = title == null ? "" : title.replace("'", "\\'");
                                    // Build inline confirm message and delete URL using ISBN key.
                                    String confirmMsg = "return confirm('Are you sure to delete { " + safeTitle + " }? (ISBN: " + book.getIsbn() + ")');";
                                    String deleteUrl = request.getContextPath() + "/admin/books?action=delete&isbn=" + book.getIsbn();
                                %>
                                <form method="post" action="${pageContext.request.contextPath}/admin/books" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
                                    <button type="submit" class="btn btn-delete" onclick="<%= confirmMsg %>">Delete</button>
                                </form>
                            </div>

                            <%-- Bottom row: Manage Copies button (Full width) --%>
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

        <%-- Pagination Controls --%>
        <div class="pagination">
            <%
                Integer currentPageObj = (Integer) request.getAttribute("currentPage");
                Integer totalPagesObj = (Integer) request.getAttribute("totalPages");
                String keyword = (String) request.getAttribute("searchKeyword");

                int currentPage = (currentPageObj != null) ? currentPageObj : 1;
                int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;

                // Keep the search parameter in the URL if it exists
                String searchParam = "";
                if(keyword != null && !keyword.isEmpty()) {
                    searchParam = "&search=" +java.net.URLEncoder.encode(keyword, "UTF-8");
                }

                // Show 'Previous' button if not on the first page
                if(currentPage > 1){
            %>
                <a href="?page=<%= currentPage - 1 %><%= searchParam %>">&laquo; Prev</a>
            <% } %>
            <%  // Loop through and display page numbers
                for(int i = 1; i <= totalPages; i++){
            %>
                <a href="?page=<%= i %><%= searchParam %>" class="<%= (i==currentPage) ? "active" : "" %>"><%= i %></a>
            <% } %>
            <%  // Show 'Next' button if not on the last page
                if(currentPage < totalPages){
            %>
                <a href="?page=<%= currentPage + 1 %><%= searchParam %>">Next &raquo;</a>
            <% } %>
        </div>
    </div>
</body>
</html>
