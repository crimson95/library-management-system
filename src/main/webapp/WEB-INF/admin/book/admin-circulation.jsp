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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
    <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
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
    <%-- Inline banner show the server-side message --%>
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
                <div class="form-group" style="flex: 0 0 auto;">
                    <label>&nbsp;</label>
                    <button type="submit" class="btn btn-borrow">Process Checkout</button>
                </div>
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
