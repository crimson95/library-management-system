package com.example.lms.book;

import DTO.book.BookDTO;
import DTO.book.BookInfoDTO;
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
 * Servlet handling administrative operations for book copies (BookInfo).
 * Supports viewing, adding, updating, and deleting physical copies of a specific book.
 */
@WebServlet("/admin/book-copies")
public class AdminBookCopiesServlet extends HttpServlet {
    private final BookService bookService = new BookService();

    /**
     * Handles GET requests to display the list of book copies, or to show the add/edit forms.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String success = request.getParameter("success");
        if(success != null) {
            switch(success) {
                case "add":{
                    request.setAttribute("successMessage", "Book copy add successfully");
                    break;
                }
                case "update":{
                    request.setAttribute("successMessage", "Book copy update successfully");
                    break;
                }
                case "delete":{
                    request.setAttribute("successMessage", "Book copy delete successfully");
                    break;
                }
            }
        }

        String isbn = request.getParameter("isbn");
        if(isbn == null || isbn.trim().isEmpty()) {
            // Redirect to books list if ISBN is missing
            response.sendRedirect(request.getContextPath() + "/admin/books");
            return;
        }

        String action = request.getParameter("action");
        if(action == null) action = "";

        try{
            switch (action) {
                case "add":{
                    // Forward to the add new book copy page
                    // get ISBN from the path
                    String addIsbn = request.getParameter("isbn");
                    BookDTO targetBook = bookService.findByISBN(addIsbn);
                    request.setAttribute("book", targetBook);
                    request.getRequestDispatcher("/WEB-INF/admin/copy/admin-book-copies-add.jsp").forward(request, response);
                    return;
                }

                case "update":{
                    // Forward to the edit book copy page with pre-loaded data
                    String updateIsbn = request.getParameter("isbn");
                    String updateCopyIDRaw = request.getParameter("bookID");

                    if(updateCopyIDRaw != null) {
                        BookDTO updateBook = bookService.findByISBN(updateIsbn);
                        BookInfoDTO updateCopy = bookService.findCopyByID(Integer.parseInt(updateCopyIDRaw));
                        request.setAttribute("book", updateBook);
                        request.setAttribute("copy", updateCopy);
                        request.getRequestDispatcher("/WEB-INF/admin/copy/admin-book-copies-edit.jsp").forward(request, response);
                    }else{
                        response.sendRedirect(request.getContextPath() + "/admin/book-copies?isbn=" + updateIsbn);
                    }
                    return;
                }

                case "delete":{
                    // Delete the specified book copy and redirect back to the copies list
                    String bookIDRaw = request.getParameter("bookID");
                    if(bookIDRaw != null) {
                        int bookID = Integer.parseInt(bookIDRaw);
                        bookService.deleteBookCopy(bookID);
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/book-copies?isbn=" + isbn);
                    return;
                }

                default:{
                    // Display the list of all copies for the specified book
                    BookDTO book = bookService.findByISBN(isbn);
                    if(book == null) {
                        response.sendRedirect(request.getContextPath() + "/admin/books");
                        return;
                    }
                    List<BookInfoDTO> copies = bookService.findCopiesByISBN(isbn);

                    request.setAttribute("book", book);
                    request.setAttribute("copiesList", copies);
                    request.getRequestDispatcher("/WEB-INF/admin/copy/admin-book-copies.jsp").forward(request, response);
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load the copy list.");
        }
    }

    /**
     * Handles POST requests to process form submissions for adding or updating book copies.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "";
        try{
            switch (action) {
                case "add":{
                    // Process the addition of a new book copy
                    String isbn = request.getParameter("isbn");
                    String condition = request.getParameter("condition");
                    String statusRaw = request.getParameter("status");

                    try{
                        int status = Integer.parseInt(statusRaw);
                        BookInfoDTO newCopy = new BookInfoDTO(0, condition, status, isbn);
                        bookService.addBookCopy(newCopy);

                        response.sendRedirect(request.getContextPath() + "/admin/book-copies?isbn=" + isbn + "&success=add");
                        return;
                    }catch (Exception e){
                        e.printStackTrace();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not add the copy.");
                    }
                }

                case "update":{
                    // Process the update of an existing book copy
                    String editIsbn = request.getParameter("isbn");
                    String editCondition = request.getParameter("condition");
                    String editStatusRaw = request.getParameter("status");
                    String editBookIDRaw = request.getParameter("bookID");

                    try{
                        int editStatus = Integer.parseInt(editStatusRaw);
                        int editBookID = Integer.parseInt(editBookIDRaw);

                        BookInfoDTO updateCopy = new BookInfoDTO(editBookID, editCondition, editStatus, editIsbn);
                        bookService.updateBookCopy(updateCopy);
                        response.sendRedirect(request.getContextPath() + "/admin/book-copies?isbn=" + editIsbn + "&success=update");
                        return;
                    }catch (Exception e){
                        e.printStackTrace();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not update the copy.");
                        return;
                    }
                }

                case "delete":{
                    int bookID = Integer.parseInt(request.getParameter("bookID"));
                    String editIsbn = request.getParameter("isbn");
                    bookService.deleteBookCopy(bookID);
                    response.sendRedirect(request.getContextPath() + "/admin/book-copies?isbn=" + editIsbn +  "&success=delete");
                    return;
                }

                default:{
                    response.sendRedirect(request.getContextPath() + "/admin/book-copies");
                    break;
                }
            }
        }catch (BusinessValidationException e){
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
