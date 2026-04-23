package com.github.crimson95.lms.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Security filter that intercepts all HTTP requests to the user portal ("/user/*").
 * <p>
 * This filter ensures that only authenticated users (either Readers or Admins)
 * can access personal catalog views, borrowing records, and profile settings.
 * Unauthenticated requests are redirected to the login page.
 */
@WebFilter({"/user", "/user/*"})
public class UserSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{

        HttpServletRequest httpRequest = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Retrieve the current session, without creating a new one if it doesn't exist
        HttpSession session = httpRequest.getSession(false);

        // 1. Authentication Check: Ensure a user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("loginUser") != null);

        if(! isLoggedIn){
            // Redirect unauthenticated users to the login page
            httpResponse.sendRedirect(httpRequest.getContextPath()+"/login");
            return;  // Terminate filter chain
        }

        // 2. Prevent Browser Caching for Security (Prevents "Back Button" ghost logins)
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0); // Proxies

        // 3. Success: Pass the request along the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic
    }
}
