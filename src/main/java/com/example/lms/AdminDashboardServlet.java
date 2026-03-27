package com.example.lms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.book.BookService;

import java.io.IOException;

/**
 * Entry servlet for the admin area.
 * <p>
 * This servlet only handles page navigation for now:
 * it forwards the request to the admin dashboard JSP.
 * Data widgets (summary cards) can be connected later.
 */
@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {

    private final BookService bookService = new BookService();

    /**
     * Renders the admin dashboard page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        try{
            request.setAttribute("totalBooks", bookService.getTotalBooksCount());
            request.setAttribute("availableCopies", bookService.getAvailableCopiesCount());
            request.setAttribute("borrowedCopies", bookService.getBorrowedCopiesCount());
            request.setAttribute("overdueRecords", bookService.getOverdueCopiesCount());
        }catch(Exception e){
            e.printStackTrace();
        }
        // Forward keeps the same request/response and hides WEB-INF JSP from direct URL access.
        request.getRequestDispatcher("/WEB-INF/admin/admin-dashboard.jsp").forward(request, response);
    }

    /**
     * Reuses GET behavior because this endpoint is currently read-only.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }
}
