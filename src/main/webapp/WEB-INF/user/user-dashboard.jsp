<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-05
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.BookDTO" %>
<%@ page import="DTO.user.UserDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Library Catalog</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user.css">
</head>
<body>
    <%-- Top Navigation Bar for Readers --%>
    <nav class="navbar">
        <div class="logo" style="font-size: 20px; font-weight: bold;">LMS Reader Portal</div>
        <div class="links">
            <a href="${pageContext.request.contextPath}/user">Catalog</a>
            <a href="${pageContext.request.contextPath}/user/records">My Records</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </nav>

    <div class="container">
        <% UserDTO currentUser = (UserDTO) session.getAttribute("loginUser"); %>

        <%-- Welcome Section --%>
        <div class="page-header">
            <h2>Welcome back, <%= currentUser != null ? currentUser.getFirstName() : "Reader" %>!</h2>
            <p>Explore our library catalog and find your next great read.</p>
        </div>

        <%-- Search Section --%>
        <div class="search-bar">
            <form method="get" action="${pageContext.request.contextPath}/user" style="display: flex; width: 100%; gap: 10px; margin: 0;">
                <input type="text" name="search" placeholder="Search..." value="${searchKeyword != null ? searchKeyword : ''}">
                <button type="submit" class="btn-search">Search</button>
                <a href="${pageContext.request.contextPath}/user" class="btn-clear">Clear</a>
            </form>
        </div>

        <%-- Catalog Display Section --%>
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th style="width: 30%;">Book Information</th>
                        <th style="width: 20%;">Author & Publisher</th>
                        <th style="width: 40%;">Description</th>
                        <th style="width: 10%;">Copy Status</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<BookDTO> books = (List<BookDTO>) request.getAttribute("bookList");
                    if(books != null && !books.isEmpty()){
                        for(BookDTO book : books){
                %>
                    <tr>
                        <td>
                            <h4 class="book-title"><%= book.getTitle() %></h4>
                            <div class="book-meta">ISBN: <%= book.getIsbn() %></div>
                            <div class="book-meta">Acquired: <%= book.getDateAcquired() %></div>
                        </td>
                        <td>
                            <div style="font-weight: bold; color: #333;"><%= book.getAuthorName() %></div>
                            <div class="book-meta"><%= book.getPublisherName() %></div>
                        </td>
                        <td>
                            <div class="book-desc"><%= book.getDescription() != null ? book.getDescription() : "No description available." %></div>
                        </td>
                        <td>
                            <div style="margin-top: 10px;">
                                <% if(book.getAvailableCopies() > 0){ %>
                                <span style="background: #27ae60; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold;">
                                    Available (<%= book.getAvailableCopies() %>)
                                </span>
                                <% }else{ %>
                                <span style="background: #e74c3c; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold;">
                                    Out of Stock
                                </span>
                                <% } %>
                            </div>
                        </td>
                    </tr>

                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="4" style="text-align: center; padding: 50px; color: #7f8c8d;">
                            <h3>No books found.</h3>
                            <p>Try adjusting your search keywords.</p>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <div class="pagination">
            <%
                Integer currentPageObj = (Integer) request.getAttribute("currentPage");
                Integer totalPagesObj = (Integer) request.getAttribute("totalPages");
                String keyword = (String) request.getAttribute("searchKeyword");

                int currentPage = (currentPageObj != null) ? currentPageObj : 1;
                int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;

                // Keep the search parameter in the URL if it exists
                String searchParam = (keyword != null && !keyword.isEmpty()) ? "&search=" + keyword : "";

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
