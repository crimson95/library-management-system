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
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .navbar { background: #960000; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .navbar .links a { color: #fff; text-decoration: none; margin-left: 20px; font-weight: bold; }
        .navbar .links a:hover { color: #960000; }

        .container { padding: 30px; }

        .welcome-banner { background: white; padding: 25px 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 30px; border-left: 6px solid #960000; }
        .welcome-banner h2 { margin: 0; color: #532828; }
        .welcome-banner p { margin: 10px 0 0 0; color: #7f8c8d; }

        .search-bar { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 20px; display: flex; gap: 10px; }
        .search-bar input[type="text"] { flex: 1; padding: 10px 15px; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; }
        .btn-search { background: #960000; color: white; border: none; padding: 10px 20px; border-radius: 5px; font-size: 16px; cursor: pointer; font-weight: bold; }
        .btn-clear { background: #95a5a6; color: white; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-size: 16px; }

        .catalog-card { background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        table th, table td { padding: 16px 20px; text-align: left; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #532828; font-weight: bold; }
        table tr:hover { background: #fdfdfd; }
        .book-title { font-size: 18px; color: #960000; margin: 0 0 5px 0; }
        .book-meta { font-size: 13px; color: #7f8c8d; }
        .book-desc { font-size: 14px; color: #555; line-height: 1.5; margin-top: 8px; }
    </style>
</head>
<body>
    <%-- Top Navigation Bar for Readers --%>
    <nav class="navbar">
        <div class="logo" style="font-size: 20px; font-weight: bold;">LMS Reader Portal</div>
        <div class="links">
            <a href="${pageContext.request.contextPath}/user">Catalog</a>
            <a href="#">My Records</a>
            <a href="${pageContext.request.contextPath}/logout" style="color: #e74c3c">Logout</a>
        </div>
    </nav>

    <div class="container">
        <% UserDTO currentUser = (UserDTO) request.getAttribute("currentUser"); %>

        <%-- Welcome Section --%>
        <div class="welcome-banner">
            <h2>Welcome back, <%= currentUser != null ? currentUser.getFirstName() : "Reader" %>!</h2>
            <p>Explore our library catalog and find your next great read.</p>
        </div>

        <%-- Search Section --%>
        <div class="search-bar">
            <form method="get" action="${pageContext.request.contextPath}/user" style="display: flex; width: 100%; gap: 10px; margin: 0;">
                <input type="text" name="search" placeholder="Search..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                <button type="submit" class="btn-search">Search</button>
                <a href="${pageContext.request.contextPath}/user" class="btn-clear">Clear</a>
            </form>
        </div>

        <%-- Catalog Display Section --%>
        <div class="catalog-card">
            <table>
                <thead>
                    <tr>
                        <th style="width: 35%;">Book Information</th>
                        <th style="width: 20%;">Author & Publisher</th>
                        <th style="width: 65%;">Description</th>
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
                    </tr>

                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="3" style="text-align: center; padding: 50px; color: #7f8c8d;">
                            <h3>No books found.</h3>
                            <p>Try adjusting your search keywords.</p>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
