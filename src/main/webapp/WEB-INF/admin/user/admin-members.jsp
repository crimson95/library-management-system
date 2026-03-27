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
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 30px; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }
        .error { background: #ffeaa7; color: #d63031; padding: 15px; border-left: 5px solid #d63031; margin-bottom: 20px; font-weight: bold; border-radius: 4px; }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .search-bar { display: flex; gap: 8px; align-items: center; }
        .search-input { padding: 8px 10px; border: 1px solid #ddd; border-radius: 5px; width: 350px; }
        .btn-search { background: #333; }
        h2 { color: red; }

        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table th, table td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
        table th { background: #f8f9fa; color: #333; }

        .action-group { display: flex; gap: 5px; align-items: center; flex-wrap: nowrap }
        .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-add { background: #27ae60; }
        .btn-edit { background: #2980b9; margin-right: 5px; }
        .btn-delete { background: #c0392b; }

        .status-tag { padding: 4px 8px; border-radius: 12px; font-size: 12px; background: #e0e0e0; }
    </style>
</head>
<body>
    <%-- Top navigation for admin pages --%>
    <nav class="navbar">
        <div class="logo">Library Management System</div>
        <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1">Logout</a></div>
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
                <div class="error">
                    <%= error %>
                </div>
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
                        <div class="action-group">
                            <%-- Open edit form for selected username --%>
                            <a href="${pageContext.request.contextPath}/admin/members?action=update&username=<%= user.getUsername() %>" class="btn btn-edit">Edit</a>
                            <%-- Trigger delete flow with simple confirm dialog --%>
                            <a href="${pageContext.request.contextPath}/admin/members?action=delete&username=<%= user.getUsername() %>" class="btn btn-delete"
                               onclick="return confirm('Are you sure to delete { <%= user.getUsername() %> } ?');">Delete</a>
                        </div>
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
