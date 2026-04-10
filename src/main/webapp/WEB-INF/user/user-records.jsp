<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-04-02
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.BookUserDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>My Borrowing Records</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user.css?v=20260409">
</head>
<body>
    <nav class="navbar">
        <div class="logo"><a href="${pageContext.request.contextPath}/user">LMS Reader Portal</a></div>
        <div class="links">
            <a href="${pageContext.request.contextPath}/user/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/user/records">My Records</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h2>My Borrowing History</h2>

            <div class="card">
                <table>
                    <thead>
                    <tr>
                        <th>Record ID</th>
                        <th>Physical Book ID</th>
                        <th>Borrow Date</th>
                        <th>Return Date</th>
                        <th>Status</th>
                        <th>Late Fee</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<BookUserDTO> records = (List<BookUserDTO>) request.getAttribute("recordList");
                        if(records != null && ! records.isEmpty()){
                            for(BookUserDTO record : records){
                                boolean isReturned = (record.getReturnDate() != null);

                                // Simple logic to check if currently overdue (> 14 days without return)
                                boolean isOverdue = false;
                                if(!isReturned){
                                    // Use LocalDate for consistency with BookService logic
                                    java.time.LocalDate today = java.time.LocalDate.now();
                                    java.time.LocalDate startDate = record.getStartDate().toLocalDate();

                                    // Define the same 14-day rule
                                    java.time.LocalDate dueDate = startDate.plusDays(14);

                                    // Check if today is strictly after the due date
                                    if(today.isAfter(dueDate)){
                                        isOverdue = true;
                                    }
                                }
                    %>
                    <tr>
                        <td>#<%= record.getBookUserID() %></td>
                        <td><strong><%= record.getBookID() %></strong></td>
                        <td><%= record.getStartDate() %></td>
                        <td><%= isReturned ? record.getReturnDate() : "-" %></td>
                        <td>
                            <% if(isReturned){ %>
                            <span class="status-badge status-returned">Returned</span>
                            <% } else if(isOverdue){ %>
                            <span class="status-badge status-overdue">Overdue</span>
                            <% } else { %>
                            <span class="status-badge">Borrowing</span>
                            <% } %>
                        </td>
                        <td style="color: <%= record.getLateFee() != null && record.getLateFee().doubleValue() > 0 ? "red" : "inherit" %>">
                            $<%= record.getLateFee() != null ? record.getLateFee() : "0.00" %>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="6" style="text-align: center; padding: 50px; color: #7f8c8d;">
                            <h3>No borrowing history found.</h3>
                            <p>Head over to the Catalog to find your next great read!</p>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
