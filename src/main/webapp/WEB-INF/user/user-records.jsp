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
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }

        /* Navigation Bar Styles */
        .navbar { background: #960000; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .navbar .links a { color: #fff; text-decoration: none; margin-left: 20px; font-weight: bold; }
        .navbar .links a:hover { color: #960000; }

        .container { padding: 30px; }

        /* Page Header Styles */
        .page-header { background: white; padding: 25px 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 30px; border-left: 6px solid #e67e22; }
        .page-header h2 { margin: 0; color: #532828; }

        /* Table Styles */
        .record-card { background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        table th, table td { padding: 16px 20px; text-align: left; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #532828; font-weight: bold; }
        table tr:hover { background: #fdfdfd; }

        /* Status Badge Styles */
        .status-badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; color: white; font-weight: bold; background: #960000; }
        .status-returned { background: #95a5a6; }
        .status-overdue { background: #e74c3c; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo" style="font-size: 20px; font-weight: bold;">📚 LMS Reader Portal</div>
        <div class="links">
            <a href="${pageContext.request.contextPath}/user">Catalog</a>
            <a href="${pageContext.request.contextPath}/user/records">My Records</a>
            <a href="${pageContext.request.contextPath}/logout" style="color: #e74c3c;">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h2>My Borrowing History</h2>
        </div>

        <div class="record-card">
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
</body>
</html>
