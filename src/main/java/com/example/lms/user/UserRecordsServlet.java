package com.example.lms.user;

import DTO.book.BookUserDTO;
import DTO.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.book.BookService;

import java.io.IOException;
import java.util.List;

/**
 * Servlet handling the reader's personal borrowing history view.
 */
@WebServlet("/user/records")
public class UserRecordsServlet extends HttpServlet {
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 1. Authentication check: ensure user is logged in
        if(request.getSession(false) == null || request.getSession(false).getAttribute("loginUser") == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        UserDTO currentUser = (UserDTO)request.getSession(false).getAttribute("loginUser");

        try{
            // 2. Fetch borrowing history strictly for the currently logged-in user
            List<BookUserDTO> records = bookService.getUserBorrowingHistory(currentUser.getUsername());

            // 3. Attach data to request and forward to JSP
            request.setAttribute("recordsList", records);
            request.getRequestDispatcher("/WEB-INF/user/user-records.jsp").forward(request, response);

        }catch(Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load borrowing history.");
        }
    }
}
