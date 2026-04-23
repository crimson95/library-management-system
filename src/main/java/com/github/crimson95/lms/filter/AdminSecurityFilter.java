package com.github.crimson95.lms.filter;

import DTO.user.UserDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Security filter that intercepts all HTTP requests to the admin panel ("/admin/*").
 * <p>
 * This filter enforces access control by verifying that:
 * 1. A valid user session exists.
 * 2. The authenticated user possesses administrator privileges.
 * Unauthorized requests are redirected to the login page or rejected with a 403 Forbidden error.
 */
@WebFilter({"/admin", "/admin/*"})
public class AdminSecurityFilter implements Filter {

    /**
     * Initializes the filter.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic can be placed here if needed in the future.
    }

    /**
     * Performs the authorization check before allowing the request to proceed.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Cast to HTTP-specific request and response objects
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Retrieve the current session, without creating a new one if it doesn't exist
        HttpSession session = httpRequest.getSession(false);

        // 1. Authentication Check: Ensure the user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("loginUser") != null);

        if (!isLoggedIn) {
            // Redirect unauthenticated users to the login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;  // Terminate filter chain execution
        }

        // 2. Authorization Check: Ensure the logged-in user is an Admin
        UserDTO currentUser = (UserDTO) session.getAttribute("loginUser");

        if(!currentUser.isAdmin()) {
            // Reject authenticated but unauthorized users (e.g., normal readers trying to access admin URLs)
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator privileges are required.");
            return; // Terminate filter chain execution
        }

        // 3. Prevent Browser Caching for Security
        // Instructs the browser NOT to cache any secured admin pages.
        // This prevents the "Back button" from revealing sensitive data after logout.
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");  // HTTP 1.1.
        httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        httpResponse.setDateHeader("Expires", 0); // Proxies

        // 4. Success: Pass the request along the filter chain to the intended Servlet
        chain.doFilter(request, response);
    }

    /**
     * Cleans up resources when the filter is destroyed.
     */
    @Override
    public void destroy() {
        // Cleanup logic can be placed here if needed.
    }
}
