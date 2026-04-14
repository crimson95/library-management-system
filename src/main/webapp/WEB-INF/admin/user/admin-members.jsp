<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-13
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.user.UserDTO" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>User Management</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <%-- Top navigation for admin pages --%>
    <nav class="navbar">
        <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
    </nav>

    <div class="container">
        <% String successMsg = (String) request.getAttribute("successMessage");
            if (successMsg != null) { %>
        <div class="success"><%= successMsg %></div>
        <% } %>

        <%
            // Error is set by servlet when validation/business rule fails.
            String error = (String) request.getAttribute("error");
            if(error != null) { %>
        <%-- Inline banner show the server-side message --%>
        <div class="error"><%= error %></div>
        <%  } %>
        <div class="card">
            <%-- Page title + search + shortcut actions --%>
            <div class="header-actions">
                <h2>User Management</h2>
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/members">
                    <input type="text" name="search" class="search-input" placeholder="Search..." value="${searchKeyword != null ? searchKeyword : ''}">
                    <button type="submit" class="btn btn-search">Search</button>
                    <a href="?" class="btn btn-secondary">Clear</a>
                </form>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/members?action=add-admin" class="btn btn-add">Add Admin User</a>
                    <a href="${pageContext.request.contextPath}/admin" class="btn">Back to Dashboard</a>
                </div>
            </div>

            <%-- User list table --%>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    // AdminMembersServlet puts the list in request attribute "userList".
                    List<UserDTO> users = (List<UserDTO>) request.getAttribute("userList");
                    if(users != null && !users.isEmpty()){
                        for (UserDTO user : users){
                %>
                <tr>
                    <td><%= user.getUsername() %></td>
                    <td><%= user.getFirstName() %></td>
                    <td><%= user.getLastName() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= user.getPhone() %></td>
                    <td><%= user.isAdmin() ? "Administrator" : "Member" %></td>
                    <td>
                        <%-- Open edit form for selected username --%>
                        <a href="${pageContext.request.contextPath}/admin/members?action=update&username=<%= user.getUsername() %>" class="btn btn-edit">Edit</a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/members"
                              style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="username" value="<%= user.getUsername() %>">
                            <button type="submit" class="btn btn-delete"
                                    onclick="return confirm('Delete user: <%= user.getUsername() %>?');">Delete</button>
                        </form>
                    </td>
                </tr>
                <%      }
                    }else {
                        // Empty-state message when filter returns no matches or no user exists.
                %>
                <tr>
                    <td colspan="7" style="text-align: center; color: #7f8c8d; padding: 40px;">No users found</td>
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
