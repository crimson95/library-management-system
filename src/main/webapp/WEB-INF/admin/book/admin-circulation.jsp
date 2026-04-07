<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-31
  Time: 13:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.BookUserDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Circulation Desk</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .navbar { background: #960000; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); margin-bottom: 30px; }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        h2 { color: red; }

        /* Form Styles */
        .form-row { display: flex; gap: 15px; align-items: flex-end; margin-bottom: 15px; }
        .form-group { display: flex; flex-direction: column; flex: 1; }
        .form-group label { font-weight: bold; margin-bottom: 5px; color: #333; }
        .form-group input { padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 16px; }

        /* Table Styles */
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table th, table td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #333; }

        /* Button Styles */
        .btn { background: red; color: white; padding: 8px 15px; border: none; border-radius: 8px; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: bold; }
        .btn-borrow { background: #2980b9; height: 40px; }
        .btn-return { background: #27ae60; padding: 8px 15px; }

        .error { background: #ffeaa7; color: #d63031; padding: 12px; border-left: 5px solid #d63031; margin-bottom: 16px; font-weight: bold; border-radius: 8px; }
        .status-badge { padding: 8px 15px; border-radius: 8px; font-size: 14px; color: white; font-weight: bold; background: #e67e22; }
        .status-returned { background: #95a5a6; }
        .status-overdue { background: #c0392b; }
    </style>
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
    <div class="logo">Library Management System - Circulation Desk</div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>

<div class="container">
    <div class="header-actions">
        <h2>Circulation Desk</h2>
        <a href="${pageContext.request.contextPath}/admin" class="btn">Back to Dashboard</a>
    </div>

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error");
    if(error != null) { %>
    <%-- Inline alert + banner both show the same server-side message --%>
        <script>alert('<%= error %>');</script>
        <div class="error"> <%= error %></div>
    <% } %>

    <%-- Section 1: Borrow Book Form --%>
    <div class="card">
        <h3 style="margin-top: 0; color: #2980b9;">Checkout (Borrow)</h3>
        <form method="post" action="${pageContext.request.contextPath}/admin/circulation">
            <input type="hidden" name="action" value="borrow" />
            <div class="form-row">
                <div class="form-group">
                    <label for="username">Reader Username</label>
                    <input type="text" id="username" name="username" placeholder="enter username">
                </div>
                <div class="form-group">
                    <label for="bookID">Physical Copy ID</label>
                    <input type="number" id="bookID" name="bookID" placeholder="enter bookID">
                </div>
                <button type="submit" class="btn btn-borrow">Process Checkout</button>
            </div>
        </form>
    </div>

    <%-- Section 2: Borrowing Records Table --%>
    <div class="card">
        <h3 style="margin-top: 0; color: #2c3e50;">Circulation Records</h3>
        <table>
            <thead>
                <tr>
                    <th>Record ID</th>
                    <th>Book ID</th>
                    <th>Reader</th>
                    <th>Borrow Date</th>
                    <th>Return Date</th>
                    <th>Late Fee</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<BookUserDTO> borrowList = (List<BookUserDTO>) request.getAttribute("borrowList");
                if(borrowList != null && !borrowList.isEmpty()){
                    for(BookUserDTO record : borrowList){
                        boolean isReturned = (record.getReturnDate() != null);
                        boolean isOverdue = false;

                        // Calculate if the book is overdue using precise LocalDate math
                        if(!isReturned){
                            java.time.LocalDate today = java.time.LocalDate.now();
                            java.time.LocalDate startDate = record.getStartDate().toLocalDate();
                            java.time.LocalDate dueDate = startDate.plusDays(14);

                            if(today.isAfter(dueDate)) isOverdue = true;
                        }
            %>
                <tr>
                    <td>#<%= record.getBookUserID() %></td>
                    <td><strong><%= record.getBookID() %></strong></td>
                    <td><%= record.getUsername() %></td>
                    <td><%= record.getStartDate() %></td>

                    <%-- Display Return Date, Overdue, or Borrowing Badge --%>
                    <td>
                        <% if(isReturned) { %>
                            <%= record.getReturnDate() %>
                        <% } else if (isOverdue) { %>
                            <span class="status-badge status-overdue">Overdue</span>
                        <% } else { %>
                            <span class="status-badge">Borrowing</span>
                        <% } %>
                    </td>

                    <%-- Display Late Fee --%>
                    <td style="color: <%= record.getLateFee() != null && record.getLateFee().doubleValue() > 0 ? "red" : "inherit" %>">
                        <strong>$<%= record.getLateFee() != null ? record.getLateFee() : "0.00" %></strong>
                    </td>

                    <%-- Show Pay button only if there is a fee and the book is already returned --%>
                    <td>
                        <% if(!isReturned) { %>
                        <%-- Book is still out, show Return button --%>
                        <form method="post" style="margin: 0;" action="${pageContext.request.contextPath}/admin/circulation">
                            <input type="hidden" name="action" value="return" />
                            <input type="hidden" name="bookUserID" value="<%= record.getBookUserID() %>" />
                            <button type="submit" class="btn btn-return" onclick="return confirm('Process return for Record #<%= record.getBookUserID() %>?');">Return Book</button>
                        </form>

                        <% } else if(record.getLateFee() != null && record.getLateFee().doubleValue() > 0){ %>
                        <%-- Book returned but fee unpaid, show Pay button --%>
                        <form method="post" action="${pageContext.request.contextPath}/admin/circulation" style="margin: 0;">
                            <input type="hidden" name="action" value="pay" />
                            <input type="hidden" name="bookUserID" value="<%= record.getBookUserID() %>" />
                            <button type="submit" class="btn" style="background: #c0392b;"
                                    onclick="return confirm('Confirm payment of $<%= record.getLateFee() %> for Record #<%= record.getBookUserID() %>?');">Process Payment</button>
                        </form>
                        <% } else { %>
                        <%-- Book returned AND fee is zero, show Completed --%>
                        <span class="status-badge status-returned">Completed</span>
                        <% } %>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="7" style="text-align: center; color: #7f8c8d; padding: 40px;">No circulation records found.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
