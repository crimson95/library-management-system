package com.github.crimson95.lms.book;

import DTO.book.AuthorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BusinessValidationException;
import service.book.BookService;

import java.io.IOException;
import java.util.List;

/**
 * Admin servlet for author management pages.
 * <p>
 * Supports list/add/update/delete actions and delegates business rules
 * to {@link BookService}.
 */
@WebServlet("/admin/authors")
public class AdminAuthorsServlet extends HttpServlet {
    private final BookService bookService = new BookService();

    /**
     * Handles author page routing and GET-based actions.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String success = request.getParameter("success");
        if(success != null) {
            switch(success) {
                case "add":{
                    request.setAttribute("successMessage", "Author add successfully");
                    break;
                }
                case "update":{
                    request.setAttribute("successMessage", "Author update successfully");
                    break;
                }
                case "delete":{
                    request.setAttribute("successMessage", "Author delete successfully");
                    break;
                }
            }
        }

        // Action-driven routing from query string.
        String action = request.getParameter("action");
        if (action == null) action = "";

        try{
            switch (action){
                case "add":{
                    // Open add form.
                    request.getRequestDispatcher("/WEB-INF/admin/author/admin-authors-add.jsp").forward(request, response);
                    return;
                }
                case "update":{
                    // Load selected author for edit form.
                    String authorIDRaw = request.getParameter("authorID");
                    if (authorIDRaw != null) {
                        int authorID = Integer.parseInt(authorIDRaw);
                        AuthorDTO author = bookService.findAuthorByID(authorID);
                        request.setAttribute("author", author);
                        request.getRequestDispatcher("/WEB-INF/admin/author/admin-authors-edit.jsp").forward(request,response);
                    }else{
                        response.sendRedirect(request.getContextPath() + "/admin/authors");
                    }
                    return;
                }

                case "delete":{
                    // Execute delete and return to list page.
                    String deleteIDRaw = request.getParameter("authorID");
                    if (deleteIDRaw != null){
                        bookService.deleteAuthor(Integer.parseInt(deleteIDRaw));
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/authors");
                    return;
                }

                default:{
                    // Fall back to default book list view.
                    break;
                }
            }
        }catch (BusinessValidationException e){
            request.setAttribute("error", e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "System error or this author has existing books.");
        }

        try{
            String search = request.getParameter("search");
            List<AuthorDTO> authors = bookService.findAllAuthors();

            if(search != null && !search.trim().isEmpty()){
                String query = search.trim().toLowerCase();
                List<AuthorDTO> filtered = new java.util.ArrayList<>();
                for(AuthorDTO author : authors){
                    // Search supports key fields shown in the table.
                    if(containsIgnoreCase(String.valueOf(author.getAuthorID()), query)
                    || containsIgnoreCase(author.getFirst_name(), query)
                    || containsIgnoreCase(author.getLast_name(), query)){
                        filtered.add(author);
                    }
                }
                authors = filtered;
            }
            request.setAttribute("authorList", authors);
            request.getRequestDispatcher("/WEB-INF/admin/author/admin-authors.jsp").forward(request, response);
        } catch (Exception e) {
            // If list cannot be loaded, return server error.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load the author list");
        }
    }

    /**
     * Handles form submissions for add/update author operations.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "";
        try{
            switch (action){
                case "add":{
                    // Create author from submitted form fields.
                    String firstName = request.getParameter("first_name");
                    String lastName = request.getParameter("last_name");

                    try{
                        AuthorDTO newAuthor = new AuthorDTO(0, firstName, lastName);
                        bookService.addAuthor(newAuthor);
                        response.sendRedirect(request.getContextPath() + "/admin/authors?success=add");
                        return;
                    }catch (BusinessValidationException e){
                        request.setAttribute("error", e.getMessage());
                        request.getRequestDispatcher("/WEB-INF/admin/author/admin-authors-add.jsp").forward(request, response);
                        return;
                    }
                }

                case "update":{
                    // Update existing author from submitted form fields.
                    String authorIDRaw = request.getParameter("authorID");
                    String firstName = request.getParameter("first_name");
                    String lastName = request.getParameter("last_name");

                    try{
                        int authorID = Integer.parseInt(authorIDRaw);
                        AuthorDTO updateAuthor = new AuthorDTO(authorID, firstName, lastName);
                        bookService.updateAuthor(updateAuthor);
                        response.sendRedirect(request.getContextPath() + "/admin/authors?success=update");
                        return;
                    }catch (BusinessValidationException e){
                        request.setAttribute("error", e.getMessage());
                        request.setAttribute("author", new AuthorDTO(Integer.parseInt(authorIDRaw), firstName, lastName));
                        request.getRequestDispatcher("/WEB-INF/admin/author/admin-authors-edit.jsp").forward(request, response);
                        return;
                    }
                }

                case "delete":{
                    int authorID = Integer.parseInt(request.getParameter("authorID"));
                    bookService.deleteAuthor(authorID);
                    response.sendRedirect(request.getContextPath() + "/admin/authors?success=delete");
                    return;
                }

                default:{
                    response.sendRedirect(request.getContextPath() + "/admin/authors");
                    break;
                }
            }
        }catch (BusinessValidationException e){
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }catch(Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Null-safe helper used by search filter.
     */
    private boolean containsIgnoreCase(String value, String query){
        if(value == null){
            return false;
        }
        return value.toLowerCase().contains(query);
    }
}
