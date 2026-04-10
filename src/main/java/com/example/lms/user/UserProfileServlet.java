package com.example.lms.user;

import DTO.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BusinessValidationException;
import service.user.UserService;

import java.io.IOException;

/**
 * Servlet handling the display and update of the user's personal profile.
 */
@WebServlet("/user/profile")
public class UserProfileServlet extends HttpServlet {

    /** Service layer dependency for user-related business rules. */
    private final UserService userService = new UserService();

    /**
     * Handles GET requests to render the user profile page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");

        UserDTO sessionUser = (UserDTO) request.getSession(false).getAttribute("loginUser");

        try{
            // 1. Fetch the most up-to-date user data from the database.
            UserDTO freshUser = userService.findUserByUsername(sessionUser.getUsername());
            request.setAttribute("profileUser", freshUser);

            // 2. Forward to the profile JSP view.
            request.getRequestDispatcher("/WEB-INF/user/user-profile.jsp").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load user profile");
        }
    }

    /**
     * Handles POST requests for updating user profile information.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        UserDTO sessionUser = (UserDTO) request.getSession(false).getAttribute("loginUser");

        try{
            // 1. Retrieve the existing user record as the baseline.
            UserDTO targetUser = userService.findUserByUsername(sessionUser.getUsername());

            // 2. Read updated fields from the form submission.
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            // 3. Update user properties (password is only updated if a new one is provided).
            if(password != null && !password.trim().isEmpty()){
                // Validate if both password fields match perfectly
                if(!password.equals(confirmPassword)){
                    throw new BusinessValidationException("Passwords do not match. Please try again.");
                }
                targetUser.setPassword(password);
            }
            targetUser.setFirstName(firstName);
            targetUser.setLastName(lastName);
            targetUser.setEmail(email);
            targetUser.setPhone(phone);

            // 4. Persist updates via the service layer.
            userService.updateUser(targetUser);

            // 5. Refresh the session user object to reflect changes in the UI immediately.
            request.getSession(false).setAttribute("loginUser", targetUser);

            // 6. Set success message and reload the profile page.
            request.setAttribute("successMessage", "Profile successfully updated.");
            request.setAttribute("profileUser", targetUser);
            request.getRequestDispatcher("/WEB-INF/user/user-profile.jsp").forward(request, response);
            }catch (BusinessValidationException e){
            // Return to the form with validation error messages.
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "System error while updating profile.");
            doGet(request, response);
        }
    }
}
