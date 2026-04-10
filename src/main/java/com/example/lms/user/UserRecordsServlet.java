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
        UserDTO currentUser = (UserDTO)request.getSession(false).getAttribute("loginUser");

        try{
            // 1. Fetch borrowing history strictly for the currently logged-in user
            List<BookUserDTO> records = bookService.getUserBorrowingHistory(currentUser.getUsername());

            // 2. Attach data to request and forward to JSP
            request.setAttribute("recordList", records);
            request.getRequestDispatcher("/WEB-INF/user/user-records.jsp").forward(request, response);

        }catch(Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load borrowing history.");
        }
    }
}
