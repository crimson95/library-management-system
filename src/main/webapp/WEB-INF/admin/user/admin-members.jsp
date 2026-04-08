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
        <div class="logo">Library Management System</div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
    </nav>

    <div class="container">
        <div class="card">
            <%-- Page title + search + shortcut actions --%>
            <div class="header-actions">
                <h2>User Management</h2>
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/members">
                    <input type="text" name="search" class="search-input" placeholder="Search..." value="${param.search}">
                    <button type="submit" class="btn btn-search">Search</button>
                </form>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/members?action=add-admin" class="btn btn-add">Add Admin User</a>
                    <a href="${pageContext.request.contextPath}/admin" class="btn">Back to Dashboard</a>
                </div>
            </div>
        <%
            // Error is set by servlet when validation/business rule fails.
            String error = (String) request.getAttribute("error");
            if(error != null) { %>
                <script>alert('<%= error %>');</script>
                <div class="error"><%= error %></div>
        <%  } %>
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
                        <%-- Trigger delete flow with simple confirm dialog --%>
                        <a href="${pageContext.request.contextPath}/admin/members?action=delete&username=<%= user.getUsername() %>" class="btn btn-delete"
                           onclick="return confirm('Are you sure to delete { <%= user.getUsername() %> }?');">Delete</a>
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
    </div>
</body>
</html>
