package com.example.lms.user;


import DTO.user.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.user.AuthService;
import service.BusinessValidationException;

import java.io.IOException;

/**
 * Web entry point for member registration.
 * <p>
 * Flow:
 * - GET: show register form
 * - POST: validate/create member account, then redirect to login page
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    /** Authentication service that provides registration behavior. */
    private final AuthService authService = new AuthService();

    /**
     * Renders the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Render registration page.
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    /**
     * Creates a new member account and redirects to the login page on success.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Response content type for form submission.
        response.setContentType("text/html;charset=UTF-8");

        // Read form fields from request body.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstname = request.getParameter("first_name");
        String lastname = request.getParameter("last_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Build member DTO and delegate validation to service layer.
        Member member = new Member(username, password, firstname, lastname, email, phone);

        try{
            // On success, send user to login page.
            authService.register(member);
            response.sendRedirect(request.getContextPath() + "/login");

        } catch(BusinessValidationException e){
            // On validation failure, keep entered data and show error.
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", username);
            request.setAttribute("password", password);
            request.setAttribute("first_name", firstname);
            request.setAttribute("last_name", lastname);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);

            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
