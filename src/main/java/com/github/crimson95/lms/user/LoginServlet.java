package com.github.crimson95.lms.user;

import DTO.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.user.AuthService;
import service.BusinessValidationException;

import java.io.IOException;

/**
 * Web entry point for login.
 * <p>
 * Flow:
 * - GET: show login form
 * - POST: authenticate credentials and create session
 * - redirect based on role (admin/member)
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    /** Authentication service for login validation. */
    private final AuthService authService = new AuthService();

    /**
     * Renders the login page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to JSP view in web root.
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Authenticates credentials and redirects to the role-specific home page.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ensure request body is interpreted correctly.
        request.setCharacterEncoding("UTF-8");

        // Read login credentials from submitted form.
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try{
            // Step 1: authenticate user.
            UserDTO user = authService.login(username, password);
            // Step 2: save authenticated identity into HTTP session.
            request.getSession().setAttribute("loginUser", user);

            if(user.isAdmin()){
                // Step 3a: admin entry page.
                response.sendRedirect(request.getContextPath() + "/admin");
            }else{
                // Step 3b: member entry page.
                response.sendRedirect(request.getContextPath() + "/user");
            }
        }catch(BusinessValidationException e){
            // Authentication failed: keep user on login page and display message.
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
