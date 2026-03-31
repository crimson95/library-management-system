<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-20
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DTO.book.BookDTO" %>
<%@ page import="DTO.book.AuthorDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.PublisherDTO" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Book Information</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 40px; }
    .navbar { background: #2c3e50; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
    .container { padding: 30px; display: flex; align-items: center; justify-content: center; }
    .card { width: 100%; max-width: 500px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }
    h2 { color: red; display: block; text-align: center; }
    .row { margin-bottom: 12px; }
    label { display: block; margin-bottom: 6px; }
    input { width: 100%; padding: 8px; box-sizing: border-box; }
    input[readonly] { background: #f0f0f0; color: #666; cursor: not-allowed; }
    .btn-row { display: flex; gap: 10px; margin-top: 16px; }
    .btn { background: red; color: white; padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; font-size: 14px; }
    .btn-secondary { background: #333; }
    .error { background: #ffeaa7; color: #d63031; padding: 12px; border-left: 5px solid #d63031; margin-bottom: 16px; font-weight: bold; border-radius: 4px; }
  </style>
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
  <div class="logo">Library Management System</div>
  <div class="logout"><a href="${pageContext.request.contextPath}/logout" style="color: #ecf0f1">Logout</a></div>
</nav>

<div class="container">
  <div class="card">
    <h2>Edit Book Information</h2>

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error");
    if(error != null) { %>
    <%-- Inline alert + banner both show the same server-side message --%>
      <script>alert('<%= error %>');</script>
      <div class="error"> <%= error %></div>
    <% } %>

    <%-- Book object loaded by servlet for pre-filling the form --%>
    <% BookDTO book = (BookDTO) request.getAttribute("book"); %>

    <%-- Update form: ISBN is read-only key, other fields are editable --%>
    <form method="post" action="${pageContext.request.contextPath}/admin/books">
      <input type="hidden" name="action" value="update" />

      <div class="row">
        <label for="isbn">ISBN</label>
        <input type="text" name="isbn" id="isbn" maxlength="10" value="<%= book != null ? book.getIsbn() : "" %>" readonly>
      </div>
      <div class="row">
        <label for="title">Title</label>
        <input type="text" name="title" id="title" value="<%= book != null ? book.getTitle() : "" %>">
      </div>
      <div class="row">
        <label for="date_acquired">Date Acquired</label>
        <input type="date" name="date_acquired" id="date_acquired" value="<%= book != null ? book.getDateAcquired() : "" %>">
      </div>
      <div class="row">
        <label for="description">Description</label>
        <input type="text" name="description" id="description" value="<%= book != null ? book.getDescription() : "" %>">
      </div>
      <div class="row">
        <label for="author">Author</label>
        <select name="authorID" id="authorID" style="width: 100%; padding: 8px;">
          <%
            // Dropdown options are loaded by servlet during edit flow.
            List<AuthorDTO> authors = (List<AuthorDTO>) request.getAttribute("authorList");
            if(authors != null){
              for(AuthorDTO author : authors){
                boolean isSelectedAuthor = (book != null && book.getAuthorID() == author.getAuthorID());
          %>
          <option value="<%= author.getAuthorID() %>" <%= isSelectedAuthor ? "selected" : "" %>>
            <%= author.getFullName() %>
          </option>
          <%
              }
            }
          %>
        </select>
      </div>
      <div class="row">
        <label for="publisher">Publisher</label>
        <select name="publisherID" id="publisherID" style="width: 100%; padding: 8px;">
          <%
            // Keep currently selected publisher based on book payload.
            List<PublisherDTO> publishers = (List<PublisherDTO>) request.getAttribute("publisherList");
            if(publishers != null){
              for(PublisherDTO publisher : publishers){
                boolean isSelectedPublisher = (book != null && book.getPublisherID() == publisher.getPublisherID());
          %>
          <option value="<%= publisher.getPublisherID() %>" <%= isSelectedPublisher ? "selected" : "" %>>
            <%= publisher.getPublisherName() %>
          </option>
          <%
              }
            }
          %>
        </select>
      </div>

      <div class="btn-row">
        <%-- Confirm submits update; Cancel returns to list page --%>
        <button class="btn" type="submit">Confirm update</button>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/books">Cancel</a>
      </div>
    </form>
  </div>
</div>
</body>
</html>
