<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-04-09
  Time: 17:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.user.UserDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>My Profile</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user.css">
</head>
<body>
    <%-- Top Navigation Bar --%>
    <nav class="navbar">
        <div class="logo"><a href="${pageContext.request.contextPath}/user">LMS Reader Portal</a></div>
        <div class="links">
            <a href="${pageContext.request.contextPath}/user/profile">My Profile</a>
            <a href="${pageContext.request.contextPath}/user/records">My Records</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </nav>

    <div class="container">
        <%-- Display error messages --%>
        <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
            <%-- Inline banner show the server-side message --%>
            <div class="error"><%= error %></div>
        <% } %>

        <%-- Display success messages --%>
        <% String successMsg = (String) request.getAttribute("successMessage");
        if(successMsg != null) { %>
        <div class="success"><%= successMsg %></div>
        <% } %>

        <%-- Profile Form Card --%>
        <div class="form-container">

            <h2 style="color: #960000; text-align: center;">My Profile</h2>

            <% UserDTO user = (UserDTO) request.getAttribute("profileUser"); %>

            <form method="post" action="${pageContext.request.contextPath}/user/profile">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" value="<%= user != null ? user.getUsername() : "" %>" readonly>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" placeholder="Leave blank if no changed">
                    </div>
                    <div class="form-group">
                        <label>Confirm Password</label>
                        <input type="password" name="confirm_password" placeholder="Type new password again">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>First Name</label>
                        <input type="text" name="first_name" value="<%= user != null ? user.getFirstName() : "" %>">
                    </div>
                    <div class="form-group">
                        <label>Last Name</label>
                        <input type="text" name="last_name" value="<%= user != null ? user.getLastName() : "" %>">
                    </div>
                </div>

                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" name="phone" value="<%= user != null ? user.getPhone() : "" %>">
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="text" name="email" value="<%= user != null ? user.getEmail() : "" %>">
                </div>

                <div class="form-action">
                    <button type="submit" class="btn">Save Changes</button>
                    <a class="btn btn-clear" href="${pageContext.request.contextPath}/user">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
