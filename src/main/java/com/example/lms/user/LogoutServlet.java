package com.example.lms.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        // End current login session safely.
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        // Redirect to login so user can authenticate again.
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
