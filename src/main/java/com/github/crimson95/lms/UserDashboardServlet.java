package com.github.crimson95.lms;

import DTO.book.BookDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.book.BookService;

import java.io.IOException;
import java.util.List;

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
        // 1. Retrieve search keyword (if any)
        String searchKeyword = request.getParameter("search");
        String pageParam = request.getParameter("page");

        if(searchKeyword == null) searchKeyword = "";

        // 2. Setup pagination parameters
        int currentPage = 1;
        int recordsPerPage = 8; // Display 8 books per page

        if(pageParam != null && !pageParam.isEmpty()){
          try{
              currentPage = Integer.parseInt(pageParam);
          }  catch (NumberFormatException e){
              currentPage = 1; // Fallback to page 1 if parameter is invalid
          }
        }

        // 3. Fetch paginated data and total pages from business layer
        List<BookDTO> books = bookService.searchBooksByPage(searchKeyword, currentPage, recordsPerPage);
        int totalPages = bookService.getTotalPages(searchKeyword,recordsPerPage);

        // 4. Attach data to request to render in JSP
        request.setAttribute("bookList", books);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", searchKeyword);

        // 5. Forward to the view
        request.getRequestDispatcher("/WEB-INF/user/user-dashboard.jsp").forward(request, response);
    }
}
