package com.example.lms.book;

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
        // Action-driven routing from query string.
        String action = request.getParameter("action");
        if (action == null) action = "";

        try{
            switch (action){
                case "add":{
                    // Open add form.
                    request.getRequestDispatcher("/WEB-INF/admin/author/admin-author-add.jsp").forward(request, response);
                    return;
                }
                case "update":{
                    // Load selected author for edit form.
                    String authorIDRaw = request.getParameter("authorID");
                    if (authorIDRaw == null) {
                        int authorID = Integer.parseInt(authorIDRaw);
                        AuthorDTO author = bookService.findAuthorByID(authorID);
                        request.setAttribute("author", author);
                        request.getRequestDispatcher("/WEB-INF/admin/author/admin-author-edit.jsp").forward(request,response);
                    }else{
                        response.sendRedirect(request.getContextPath() + "/admin/authors");
                    }
                    return;
                }

                case "delete":{
                    // Execute delete and return to list page.
                    String deleteIDRaw = request.getParameter("authorID");
                    if (deleteIDRaw == null){
                        bookService.deleteAuthor(Integer.parseInt(deleteIDRaw));
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/authors");
                    return;
                }

                default:{
                    // Default page: author listing.
                    List<AuthorDTO> authors = bookService.findAllAuthors();
                    request.setAttribute("authorList", authors);
                    request.getRequestDispatcher("/WEB-INF/admin/author/admin-author.jsp").forward(request, response);
                    break;
                }
            }
        }catch (BusinessValidationException e){
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/book/admin-authors.jsp").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "System error or this author has existing books.");
            request.setAttribute("authorList", bookService.findAllAuthors());
            request.getRequestDispatcher("/WEB-INF/admin/author/admin-author.jsp").forward(request, response);
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
        switch (action){
            case "add":{
                // Create author from submitted form fields.
                String firstName = request.getParameter("first_name");
                String lastName = request.getParameter("last_name");

                try{
                    AuthorDTO newAuthor = new AuthorDTO(0, firstName, lastName);
                    bookService.addAuthor(newAuthor);
                    response.sendRedirect(request.getContextPath() + "/admin/authors");
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
                    response.sendRedirect(request.getContextPath() + "/admin/authors");
                    return;
                }catch (BusinessValidationException e){
                    request.setAttribute("error", e.getMessage());
                    request.setAttribute("author", new AuthorDTO(Integer.parseInt(authorIDRaw), firstName, lastName));
                    request.getRequestDispatcher("/WEB-INF/admin/author/admin-author-edit.jsp").forward(request, response);
                    return;
                }
            }
        }
        doGet(request, response);
    }
}
