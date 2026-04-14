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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <nav class="navbar">
        <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
    </nav>

    <div class="container">
        <%-- Retrieve the book details passed from the servlet --%>
        <% BookDTO book = (BookDTO) request.getAttribute("book"); %>

        <% String successMsg = (String) request.getAttribute("successMessage");
            if(successMsg != null) { %>
        <div class="success"><%= successMsg %></div>
        <% } %>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
        if(error != null) { %>
        <%-- Inline banner show the server-side message --%>
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
                        <form method="post" action="${pageContext.request.contextPath}/admin/book-copies"
                              style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="bookID" value="<%= copy.getBookID() %>">
                            <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
                            <button type="submit" class="btn btn-delete"
                                    onclick="return confirm('Delete book copy: <%= copy.getBookID() %>?');">Delete</button>
                        </form>
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
