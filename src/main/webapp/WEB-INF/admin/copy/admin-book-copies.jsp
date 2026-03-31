<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-23
  Time: 15:40
  To change this template use File | Settings | File Templates.

  Description: Admin dashboard view for managing copies of a specific book.
  Displays a list of all physical copies with their current condition and status.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.BookDTO" %>
<%@ page import="DTO.book.BookInfoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Manage Copies</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        h2 { margin: 0; color: red; }
        p { color: #666; margin-top: 5px; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table th, table td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #333; }
        .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-add { background: #27ae60; }
        .btn-edit { background: #2980b9; margin-right: 5px; }
        .btn-delete { background: #c0392b; }
        .status-badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; color: white; font-weight: bold; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo">Library Management System</div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1;">Logout</a></div>
    </nav>

    <div class="container">
        <%-- Retrieve the book details passed from the servlet --%>
        <% BookDTO book = (BookDTO) request.getAttribute("book"); %>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
            if(error != null) { %>
        <%-- Inline alert + banner both show the same server-side message --%>
        <script>alert('<%= error %>');</script>
        <div class="error"> <%= error %></div>
        <% } %>

        <div class="card">
            <div class="header-actions">
                <div>
                    <%-- Display book details --%>
                    <h2><%= book.getTitle() %></h2>
                    <p>ISBN: <%= book.getIsbn() %> | Author: <%= book.getAuthorName() %></p>
                </div>
                <div>
                    <%-- Action buttons for adding new copy or going back --%>
                    <a href="${pageContext.request.contextPath}/admin/book-copies?action=add&isbn=<%= book.getIsbn() %>" class="btn btn-add">Add New Copy</a>
                    <a href="${pageContext.request.contextPath}/admin/books" class="btn">Back to Book List</a>
                </div>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>Copy ID</th>
                        <th>Condition</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    // Retrieve the list of copies for the book
                    List<BookInfoDTO> copies = (List<BookInfoDTO>)request.getAttribute("copiesList");
                    if(copies != null && !copies.isEmpty()){
                        for(BookInfoDTO copy : copies){
                            // Determine status text and badge color based on BookInfoDTO constants
                            String statusText = "Unknown";
                            String statusColor = "#95a5a6";  // Default gray

                            switch (copy.getStatus()){
                                case BookInfoDTO.STATUS_AVAILABLE:
                                    statusText = "Available";
                                    statusColor = "#27ae60";  // green
                                    break;
                                case BookInfoDTO.STATUS_BORROWED:
                                    statusText = "Borrowed";
                                    statusColor = "#e67e22";  // orange
                                    break;
                                case BookInfoDTO.STATUS_REPAIR:
                                    statusText = "Repairing";
                                    statusColor = "#c0392b";  // red
                                    break;
                                default:
                                    // Keep default "Unknown" and gray color
                                    break;
                            }
                %>
                <tr>
                    <td><strong><%= copy.getBookID() %></strong></td>
                    <td><%= copy.getCondition() %></td>
                    <td><span class="status-badge" style="background: <%= statusColor %>;"><%= statusText %></span></td>
                    <td>
                        <%-- Edit and Delete actions for individual copy --%>
                        <a href="${pageContext.request.contextPath}/admin/book-copies?action=update&bookID=<%= copy.getBookID() %>&isbn=<%= book.getIsbn() %>" class="btn btn-edit">Edit</a>
                        <a href="${pageContext.request.contextPath}/admin/book-copies?action=delete&bookID=<%= copy.getBookID() %>&isbn=<%= book.getIsbn() %>"
                           class="btn btn-delete" onclick="return confirm('Are you sure to delete this copy ' +
                                '(ID: #<%= copy.getBookID() %>)?');">Delete</a>
                    </td>
                </tr>

                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4" style="text-align: center; color: #7f8c8d; padding: 40px;">No copies found for this book.</td>
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
