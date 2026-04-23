package com.github.crimson95.lms.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles user logout.
 * <p>
 * It invalidates the current session (if any) and redirects to login page.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Executes logout and redirects the user to login page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Retrieve the current session, but do not create a new one if it doesn't exist
        HttpSession session = request.getSession();

        // 2. If a session exists, invalidate it to clear the login state
        if(session != null) session.invalidate();

        // 3. Redirect the user to the landing page (index.jsp) instead of the login page
        response.sendRedirect(request.getContextPath() + "/");
    }

    /**
     * Delegates POST requests to the doGet method for unified logout processing.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
