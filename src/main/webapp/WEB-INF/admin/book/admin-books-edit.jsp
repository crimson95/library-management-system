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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<%-- Top navigation bar --%>
<nav class="navbar">
  <div class="logo"><a href="${pageContext.request.contextPath}/admin">Library Management System</a></div>
  <div class="logout"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
</nav>

<div class="container">

    <%-- Show validation/business error from servlet --%>
    <% String error = (String) request.getAttribute("error");
    if(error != null) { %>
    <%-- Inline banner show the server-side message --%>
      <div class="error"> <%= error %></div>
    <% } %>

    <%-- Book object loaded by servlet for pre-filling the form --%>
    <% BookDTO book = (BookDTO) request.getAttribute("book"); %>

    <div class="form-container">
      <h2>Edit Book Information</h2>

      <%-- Update form: ISBN is read-only key, other fields are editable --%>
      <form method="post" action="${pageContext.request.contextPath}/admin/books">
        <input type="hidden" name="action" value="update" />

        <div class="form-row">
          <div class="form-group">
            <label for="isbn">ISBN</label>
            <input type="text" name="isbn" id="isbn" maxlength="10" value="<%= book != null && book.getIsbn() != null ? book.getIsbn() : "" %>" readonly>
          </div>
          <div class="form-group">
            <label for="date_acquired">Date Acquired</label>
            <input type="date" name="date_acquired" id="date_acquired" value="<%= book != null && book.getDateAcquired() != null ? book.getDateAcquired() : "" %>">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
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
          <div class="form-group">
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
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="title">Title</label>
            <input type="text" name="title" id="title" maxlength="100" value="<%= book != null && book.getTitle() != null ? book.getTitle() : "" %>">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="description">Description</label>
            <textarea name="description" id="description" rows="5"><%= book != null && book.getDescription() != null ? book.getDescription() : "" %></textarea>
          </div>
        </div>

          <div class="form-action">
            <%-- Confirm submits update; Cancel returns to list page --%>
            <button class="btn" type="submit">Confirm update</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/books">Cancel</a>
          </div>
      </form>
    </div>
</div>
</body>
</html>
