<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-30
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.PublisherDTO" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Manage Publishers</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
  <nav class="navbar">
    <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
    <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
  </nav>

<div class="container">
  <div class="card">
    <div class="header-actions">
      <h2>Publisher Management</h2>
      <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/publishers">
        <input type="text" name="search" class="search-input" placeholder="Search..." value="${param.search}">
        <button type="submit" class="btn btn-search">Search</button>
        <a href="?" class="btn btn-secondary">Clear</a>
      </form>
      <div>
        <a href="${pageContext.request.contextPath}/admin/publishers?action=add" class="btn btn-add">Add New Publisher</a>
        <a href="${pageContext.request.contextPath}/admin/books" class="btn">Back to Book Management</a>
      </div>
    </div>

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error");
    if(error != null) { %>
    <%-- Inline banner show the server-side message --%>
      <div class="error"> <%= error %></div>
    <% } %>

    <table>
      <thead>
        <tr>
          <th>Publisher ID</th>
          <th>Publisher Name</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
      <%
        // Retrieve the list of publishers
        List<PublisherDTO> publishers = (List<PublisherDTO>) request.getAttribute("publisherList");
        if(publishers != null && !publishers.isEmpty()){
          for(PublisherDTO publisher : publishers){
      %>
      <tr>
        <td><%= publisher.getPublisherID() %></td>
        <td><%= publisher.getPublisherName() %></td>
        <td>
          <%-- Edit and Delete actions for publisher --%>
          <a href="${pageContext.request.contextPath}/admin/publishers?action=update&publisherID=<%= publisher.getPublisherID() %>" class="btn btn-edit">Edit</a>
          <a href="${pageContext.request.contextPath}/admin/publishers?action=delete&publisherID=<%= publisher.getPublisherID() %>" class="btn btn-delete" onclick="return confirm('Delete publisher: <%= publisher.getPublisherName().replace("'","\\'") %>?');">Delete</a>
        </td>
      </tr>
      <%
          }
        } else {
      %>
      <tr>
        <td colspan="3" style="text-align: center; color: #7f8c8d; padding: 40px;">No publisher found.</td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
