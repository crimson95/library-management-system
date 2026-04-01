package com.example.lms;

import DTO.book.BookDTO;
import DTO.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.book.BookService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet handling the reader's main dashboard and library catalog view.
 * <p>
 * This servlet ensures the user is authenticated, fetches the complete
 * or filtered list of books, and forwards the data to the user dashboard JSP.
 */
@WebServlet("/user")
public class UserDashboardServlet extends HttpServlet {
    /** Service layer dependency for book-related operations. */
    private final BookService bookService = new  BookService();

    /**
     * Handles GET requests to load the library catalog.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Authentication check: ensure session and loginUser exist
        if(request.getSession(false) == null || request.getSession(false).getAttribute("loginUser") == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Retrieve the currently logged-in user to personalize the dashboard
        UserDTO currentUser = (UserDTO)request.getSession(false).getAttribute("loginUser");
        request.setAttribute("currentUser", currentUser);

        try{
            // 2. Fetch all books from the database via BookService
            List<BookDTO> books = bookService.findAllBooks();
            String search = request.getParameter("search");

            // 3. Handle optional search filtering
            if(search != null && !search.trim().isEmpty()){
                String query = search.trim().toLowerCase();
                List<BookDTO>filtered = new ArrayList<>();

                // Use Java Streams to filter books by Title, Author, or ISBN
                books = books.stream()
                        .filter(b -> containsIgnoreCase(b.getTitle(), query) ||
                                containsIgnoreCase(b.getAuthorName(), query) ||
                                containsIgnoreCase(b.getIsbn(), query))
                        .collect(Collectors.toList());

                /* different way:
                for(BookDTO book : books){
                    if(containsIgnoreCase(book.getTitle(), query) ||
                            containsIgnoreCase(book.getAuthorName(), query) ||
                            containsIgnoreCase(book.getIsbn(), query)){
                        filtered.add(book);
                    }
                }
                books = filtered; // Replace original list with the filtered results
                */
            }

            // 4. Pass the final book list to the view
            request.setAttribute("bookList", books);
            request.getRequestDispatcher("/WEB-INF/user/user-dashboard.jsp").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load the library catalog.");
        }
    }

    /**
     * Null-safe helper method for case-insensitive string matching.
     *
     * @param value the target string to search within
     * @param query the search keyword
     * @return true if the target string contains the keyword
     */
    private boolean containsIgnoreCase(String value, String query){
        if(value == null) return false;
        return value.toLowerCase().contains(query);
    }
}
