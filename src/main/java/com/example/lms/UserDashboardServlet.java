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
 * Servlet handling the reader's main dashboard and book catalog search.
 * <p>
 * Supports paginated viewing and keyword searching across the library catalog.
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

        // 2. Retrieve search keyword (if any)
        String searchKeyword = request.getParameter("search");
        if(searchKeyword == null) searchKeyword = "";

        // 3. Setup pagination parameters
        int page = 1;
        int recordsPerPage = 8; // Display 8 books per page

        String pageParam = request.getParameter("page");
        if(pageParam != null && !pageParam.isEmpty()){
          try{
              page = Integer.parseInt(pageParam);
          }  catch (NumberFormatException e){
              page = 1; // Fallback to page 1 if parameter is invalid
          }
        }

        // 4. Fetch paginated data and total pages from business layer
        List<BookDTO> books = bookService.searchBooksByPage(searchKeyword, page, recordsPerPage);
        int totalPages = bookService.getTotalPages(searchKeyword,recordsPerPage);

        // 5. Attach data to request to render in JSP
        request.setAttribute("bookList", books);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", searchKeyword);

        // 6. Forward to the view
        request.getRequestDispatcher("/WEB-INF/user/user-dashboard.jsp").forward(request, response);
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
