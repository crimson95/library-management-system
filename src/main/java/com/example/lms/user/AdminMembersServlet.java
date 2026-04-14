package com.example.lms.user;

import DTO.user.Admin;
import DTO.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BusinessValidationException;
import service.user.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Handles admin-side member management.
 * <p>
 * Supported GET actions:
 * - empty/default: show member list
 * - add-admin: open add-admin form page
 * - update: open edit form for target username
 * - delete: delete target username (with safety checks)
 * <p>
 * Supported POST actions:
 * - add-admin: create a new admin account
 * - update: update an existing user profile
 */
@WebServlet("/admin/members")
public class AdminMembersServlet extends HttpServlet {
    /** Service layer dependency for user-related business rules. */
    private final UserService userService = new UserService();

    /**
     * Handles page rendering and delete flow.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String success = request.getParameter("success");
        if(success != null) {
            switch(success) {
                case "add":{
                    request.setAttribute("successMessage", "User added successfully.");
                    break;
                }
                case "update":{
                    request.setAttribute("successMessage", "User updated successfully.");
                    break;
                }
                case "delete":{
                    request.setAttribute("successMessage", "User deleted successfully.");
                    break;
                }
            }
        }

        // Action selector; empty means "show list page".
        String action = request.getParameter("action");
        if (action == null) action = "";

        // Logged-in user from session, used for access/safety checks.
        UserDTO currentUser = null;
        if (request.getSession(false) != null) {
            currentUser = (UserDTO) request.getSession(false).getAttribute("loginUser");
        }

        try{
            switch (action) {
                case "add-admin":
                    // Render add-admin page.
                    request.getRequestDispatcher("/WEB-INF/admin/user/admin-members-add.jsp").forward(request, response);
                    return;
                case "update":
                    // Load selected user and open edit form.
                    String editUsername = request.getParameter("username");
                    if (editUsername == null || editUsername.trim().isEmpty()) {
                        // Invalid request, fallback to list page.
                        break;
                    }
                    UserDTO targetUser = userService.findUserByUsername(editUsername);
                    request.setAttribute("user", targetUser);
                    request.getRequestDispatcher("/WEB-INF/admin/user/admin-members-edit.jsp").forward(request, response);
                    return;
                case "delete":
                    // Deletion requires active login session.
                    if (currentUser == null) {
                        response.sendRedirect(request.getContextPath() + "/login");
                        return;
                    }

                    String username = request.getParameter("username");
                    UserDTO target = userService.findUserByUsername(username);

                    // Safety rule 1: user cannot delete own account.
                    if (currentUser.getUsername().equals(username)) {
                        request.setAttribute("error", "Cannot delete current user");
                    // Safety rule 2: admin account deletion is blocked.
                    } else if (target != null && target.isAdmin()) {
                        request.setAttribute("error", "Cannot delete admin user");
                    } else {
                        // Perform delete and redirect (PRG pattern).
                        userService.deleteUser(username);
                        response.sendRedirect(request.getContextPath() + "/admin/members");
                        return;
                    }
                    break;
                default:
                    // Fall back to default member list view.
                    break;
            }
        }catch (BusinessValidationException e){
            // Business validation message can be shown directly in page.
            request.setAttribute("error", e.getMessage());

        }catch (Exception e){
            // Unexpected exception (for example DB constraint failure).
            e.printStackTrace();
            request.setAttribute("error", "System error or the user has outstanding borrowing records; deletion is not possible.");
        }
        try{
            // 1. Retrieve search keyword and page parameter
            String search = request.getParameter("search");
            String pageParam = request.getParameter("page");

            int currentPage = 1;
            int recordsPerPage = 8;

            if(pageParam != null && !pageParam.isEmpty()){
                try{
                    currentPage = Integer.parseInt(pageParam);
                    if(currentPage < 1) currentPage = 1;
                }catch (NumberFormatException e){
                    currentPage = 1;
                }
            }

            // 2. Calculate database offset
            int offset = (currentPage - 1) * recordsPerPage;

            // 3. Fetch total count and calculate total pages
            int totalRecords = userService.countUsersBySearch(search);
            int totalPages = (int) Math.ceil((double)totalRecords / recordsPerPage);
            if(totalPages == 0) totalPages = 1; // Ensure at least 1 page exists

            // 4. Fetch paginated member list
            List<UserDTO> users= userService.searchUsersByPage(search, offset, recordsPerPage);

            // 5. Set attributes for JSP rendering
            request.setAttribute("userList", users);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("searchKeyword", search != null ? search : "");

            request.getRequestDispatcher("/WEB-INF/admin/user/admin-members.jsp").forward(request, response);

        }catch (Exception e){
            // If list cannot be loaded, return a proper server error.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Cannot load the user list.");
        }
    }

    /**
     * Handles form submissions for add-admin and update actions.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Action selector for submitted forms.
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try{
            switch (action) {
                case "add-admin": {
                    // Read form fields for new admin account creation.
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String firstName = request.getParameter("first_name");
                    String lastName = request.getParameter("last_name");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");

                    // Build DTO instance and delegate validation/persistence to service layer.
                    Admin admin = new Admin(username, password, firstName, lastName, email, phone);

                    try {
                        userService.addUser(admin);
                        response.sendRedirect(request.getContextPath() + "/admin/members?success=add");
                        return;
                    } catch (BusinessValidationException e) {
                        // Keep user on form page and show validation message.
                        request.setAttribute("error", e.getMessage());
                        request.getRequestDispatcher("/WEB-INF/admin/user/admin-members-add.jsp").forward(request, response);
                        return;
                    }
                }

                case "update": {
                    // Read editable fields from form submission.
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String firstName = request.getParameter("first_name");
                    String lastName = request.getParameter("last_name");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");

                    try{
                        UserDTO existingUser = userService.findUserByUsername(username);

                        if (existingUser == null) {
                            // Keep entered values so user does not lose form state.
                            request.setAttribute("error", "User not found");
                            request.setAttribute("user", new Admin(username, "", firstName, lastName, email, phone));
                            request.getRequestDispatcher("/WEB-INF/admin/user/admin-members-edit.jsp").forward(request, response);
                            return;
                        }

                        // Password is optional in edit form; only update when user entered a new one.
                        if (password != null && !password.trim().isEmpty()) {
                            existingUser.setPassword(password);
                        }
                        existingUser.setFirstName(firstName);
                        existingUser.setLastName(lastName);
                        existingUser.setEmail(email);
                        existingUser.setPhone(phone);

                        userService.updateUser(existingUser);
                        response.sendRedirect(request.getContextPath() + "/admin/members?success=update");
                        return;

                    }catch (BusinessValidationException e){
                        // Validation failed: reopen edit form with error message.
                        request.setAttribute("error", e.getMessage());
                        try{
                            UserDTO tempUser = userService.findUserByUsername(username);
                            request.setAttribute("user", tempUser);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        request.getRequestDispatcher("/WEB-INF/admin/user/admin-members-edit.jsp").forward(request, response);
                        return;
                    }
                }

                case "delete": {
                    String username = request.getParameter("username");
                    userService.deleteUser(username);
                    response.sendRedirect(request.getContextPath() + "/admin/members?success=delete");
                    return;
                }

                default: {
                    response.sendRedirect(request.getContextPath() + "/admin/members");
                    break;
                }
            }
        }catch(BusinessValidationException e){
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
