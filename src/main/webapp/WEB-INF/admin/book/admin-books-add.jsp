<%--
  Created by IntelliJ IDEA.
  User: cchao
  Date: 2026-03-20
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.book.AuthorDTO" %>
<%@ page import="DTO.book.PublisherDTO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Books</title>
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

    <div class="form-container">
        <h2>Add Books</h2>

        <%-- Add-book form: submits to AdminBooksServlet (POST + action=add-book) --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/books">
            <input type="hidden" name="action" value="add-book" />

            <div class="form-row">
                <div class="form-group">
                    <label for="isbn">ISBN</label>
                    <input type="text" name="isbn" id="isbn" maxlength="15" placeholder="book ISBN">
                </div>
                <div class="form-group">
                    <label for="date_acquired">Date acquired</label>
                    <input type="date" name="date_acquired" id="date_acquired">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="author">Author</label>
                    <select name="authorID" id="authorID" style="width: 100%; padding: 8px;">
                        <%
                            // authorList is populated by AdminBooksServlet doGet(action=add-book).
                            List<AuthorDTO> authors = (List<AuthorDTO>) request.getAttribute("authorList");
                            if(authors != null){
                                for(AuthorDTO author : authors){
                        %>
                        <option value="<%= author.getAuthorID() %>"><%= author.getFullName() %></option>
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
                            // publisherList is populated by AdminBooksServlet doGet(action=add-book).
                            List<PublisherDTO> publishers = (List<PublisherDTO>) request.getAttribute("publisherList");
                            if(publishers != null){
                                for(PublisherDTO publisher : publishers){
                        %>
                        <option value="<%= publisher.getPublisherID() %>"><%= publisher.getPublisherName() %></option>
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
                    <input type="text" name="title" id="title" maxlength="100" placeholder="book title">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea name="description" id="description" rows="5" placeholder="book description"></textarea>
                </div>
            </div>

            <div class="form-action">
                <%-- Submit creates a new book; Cancel returns to list page --%>
                <button class="btn" type="submit">Create Book</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/books">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>

