package com.example.lms.book;

import DTO.book.AuthorDTO;
import DTO.book.BookDTO;
import DTO.book.PublisherDTO;
import DTO.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BusinessValidationException;
import service.book.BookService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Handles admin-side book management list page and simple actions.
 * <p>
 * Current actions:
 * - default: show all books
 * - delete: delete a book by ISBN, then redirect back to list
 */
@WebServlet("/admin/books")
public class AdminBooksServlet extends HttpServlet {
    /** Service layer entry point for book business logic. */
    private final BookService bookService = new BookService();

    /**
     * Ensures the request comes from an authenticated admin user.
     *
     * @return current login user when authorized; null when response has been handled
     * @throws IOException when redirect/sendError fails
     */
    private UserDTO requireAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // No session -> user must log in first.
        if(request.getSession(false) == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        // Session exists but no login identity.
        UserDTO currentUser = (UserDTO) request.getSession(false).getAttribute("loginUser");
        if(currentUser == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        // Authenticated but role is not admin.
        if(!currentUser.isAdmin()){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return null;
        }

        return currentUser;
    }

    /**
     * Loads book list or handles a delete action.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Query parameter controls which action this request performs.
        String action = request.getParameter("action");
        if(action == null) {
            action = "";
        }

        // Block access for unauthenticated/non-admin users.
        if(requireAdmin(request, response) == null) return;

        try{
            switch (action) {
                case "add-book":{
                    // Load dropdown options used by add-book form.
                    List<AuthorDTO> authors = bookService.findAllAuthors();
                    List<PublisherDTO> publishers = bookService.findAllPublishers();

                    // Keep attribute names aligned with JSP.
                    request.setAttribute("authorList", authors);
                    request.setAttribute("publisherList", publishers);

                    request.getRequestDispatcher("/WEB-INF/admin/book/admin-books-add.jsp").forward(request, response);
                    return;
                }

                case "update":{
                    // Load selected book and open edit form.
                    String editISBN = request.getParameter("isbn");
                    if(editISBN == null || editISBN.trim().isEmpty()) {
                        //Invalid request, fallback to list page.
                        break;
                    }
                    // Load current book data + dropdown options for edit page.
                    BookDTO targetBook = bookService.findByISBN(editISBN);
                    request.setAttribute("book", targetBook);

                    List<AuthorDTO> editAuthors = bookService.findAllAuthors();
                    List<PublisherDTO> editPublishers = bookService.findAllPublishers();
                    request.setAttribute("authorList", editAuthors);
                    request.setAttribute("publisherList", editPublishers);

                    request.getRequestDispatcher("/WEB-INF/admin/book/admin-books-edit.jsp").forward(request, response);
                    return;
                }

                case "delete":{
                    // Delete by unique identifier (ISBN), then PRG redirect to avoid duplicate delete on refresh.
                    String isbn = request.getParameter("isbn");
                    bookService.deleteBook(isbn);
                    response.sendRedirect(request.getContextPath() + "/admin/books");
                    return;
                }

                default:
                    // Fall back to default book list view.
                    break;
            }
        }catch (BusinessValidationException e){
            // Business rule error from service layer.
            request.setAttribute("error", e.getMessage());

        }catch (Exception e){
            // Fallback for unexpected runtime/database exceptions.
            e.printStackTrace();
            request.setAttribute("error", "System error or the book has borrowed");
        }

        try{
            // Default branch: show list with optional in-memory filtering.
            List<BookDTO> books = bookService.findAllBooks();
            String search = request.getParameter("search");

            if(search != null && !search.trim().isEmpty()) {
                String query = search.trim().toLowerCase();
                List<BookDTO> filtered = new java.util.ArrayList<>();
                for (BookDTO book : books) {
                    // Search supports key fields shown in the table.
                    if(containsIgnoreCase(book.getIsbn(), query)
                    || containsIgnoreCase(book.getTitle(), query)
                    || containsIgnoreCase(String.valueOf(book.getDateAcquired()), query)
                    || containsIgnoreCase(book.getDescription(), query)
                    || containsIgnoreCase(book.getAuthorName(), query)
                    || containsIgnoreCase(book.getPublisherName(), query)){
                        filtered.add(book);
                    }
                }
                books = filtered;
            }
            request.setAttribute("bookList", books);
            request.getRequestDispatcher("/WEB-INF/admin/book/admin-books.jsp").forward(request, response);
        }catch (Exception e){
            // If list cannot be loaded, return server error.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load the book list");
        }
    }

    /**
     * Handles add/update submissions from admin book forms.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Ensure UTF-8 decoding for form fields.
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        // Block access for unauthenticated/non-admin users.
        if(requireAdmin(request, response) == null) return;

        switch (action) {
            case "add-book": {
            // Read form fields for new book creation.
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            String dateAcquiredRaw = request.getParameter("date_acquired");
            String description = request.getParameter("description");
            String authorIDRaw = request.getParameter("authorID");
            String publisherIDRaw = request.getParameter("publisherID");

            try {
                int authorID = Integer.parseInt(authorIDRaw);
                int publisherID = Integer.parseInt(publisherIDRaw);
                Date dateAcquired = Date.valueOf(dateAcquiredRaw);

                BookDTO book = new BookDTO(
                    isbn,
                    title,
                    dateAcquired,
                    description,
                    authorID,
                    null,
                    publisherID,
                    null
                );

                // Service layer validates and persists new book.
                bookService.addBook(book);
                response.sendRedirect(request.getContextPath() + "/admin/books");
                return;

            } catch (BusinessValidationException e) {
                request.setAttribute("error", e.getMessage());
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid date or numeric value");
            } catch (Exception e) {
                request.setAttribute("error", "Unable to create book");
            }

            // Reload dropdown data before returning to add form.
            request.setAttribute("authorList", bookService.findAllAuthors());
            request.setAttribute("publisherList", bookService.findAllPublishers());
            request.getRequestDispatcher("/WEB-INF/admin/book/admin-books-add.jsp").forward(request, response);
            return;
            }

            case "update": {
                String isbn = request.getParameter("isbn");
                String title = request.getParameter("title");
                String dateAcquiredRaw = request.getParameter("date_acquired");
                String description = request.getParameter("description");
                String authorIDRaw = request.getParameter("authorID");
                String publisherIDRaw = request.getParameter("publisherID");

                try {
                    int authorID = Integer.parseInt(authorIDRaw);
                    int publisherID = Integer.parseInt(publisherIDRaw);
                    Date dateAcquired = Date.valueOf(dateAcquiredRaw);

                    BookDTO existingBook = bookService.findByISBN(isbn);

                    if(existingBook == null){
                        // Keep entered values so user does not lose form states.
                        request.setAttribute("error", "Book does not exist");
                        request.setAttribute("book", new BookDTO(
                            isbn,
                            title,
                            dateAcquired,
                            description,
                            authorID,
                            null,
                            publisherID,
                            null
                        ));
                        request.getRequestDispatcher("/WEB-INF/admin/book/admin-books-edit.jsp").forward(request, response);
                        return;
                    }

                    existingBook.setTitle(title);
                    existingBook.setDateAcquired(dateAcquired);
                    existingBook.setDescription(description);
                    existingBook.setAuthorID(authorID);
                    existingBook.setPublisherID(publisherID);

                    // Persist updates after mapping request fields to DTO.
                    bookService.updateBook(existingBook);
                    response.sendRedirect(request.getContextPath() + "/admin/books");
                    return;

                }catch (BusinessValidationException e){
                    e.printStackTrace();
                    request.setAttribute("error", "Update failed: " + e.getMessage());
                }catch (IllegalArgumentException e){
                    request.setAttribute("error", "Invalid date or numeric value");
                }
            }
        }

        // Fallback for unknown POST action: render GET flow.
        doGet(request, response);
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
