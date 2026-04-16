<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.user.UserDTO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit User Information</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
    <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>

<div class="container">

    <div class="form-container">
        <h2>Edit User Information</h2>

        <%-- Show validation/business error from servlet --%>
        <% String error = (String) request.getAttribute("error");
        if (error != null) { %>
        <%-- Inline banner show the server-side message --%>
            <div class="error"> <%= error %></div>
        <% } %>

        <%-- User object loaded by servlet for pre-filling the form --%>
        <% UserDTO user = (UserDTO) request.getAttribute("user"); %>

        <%-- Update form: username is read-only key, other fields are editable --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/members">
            <input type="hidden" name="action" value="update" />

           <div class="form-row">
               <div class="form-group">
                   <label for="username">Username</label>
                   <input type="text" name="username" id="username" maxlength="20" value="<%= user != null ? user.getUsername() : "" %>" readonly>
               </div>
               <div class="form-group">
                   <label for="password">Password</label>
                   <input type="password" name="password" id="password" maxlength="20" value="">
               </div>
           </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="first_name">First Name</label>
                    <input type="text" name="first_name" id="first_name" maxlength="15" value="<%= user != null ? user.getFirstName() : "" %>">
                </div>
                <div class="form-group">
                    <label for="last_name">Last Name</label>
                    <input type="text" name="last_name" id="last_name" maxlength="15" value="<%= user != null ? user.getLastName() : "" %>">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" name="email" id="email" maxlength="50" value="<%= user != null ? user.getEmail() : "" %>">
                </div>
                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" name="phone" id="phone" maxlength="15" value="<%= user != null ? user.getPhone() : "" %>">
                </div>
            </div>

            <div class="form-action">
                <button class="btn" type="submit">Confirm</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/members">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
