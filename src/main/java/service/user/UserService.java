package service.user;

import DAO.user.UserDAO;
import DAO.user.UserDAOImpl;
import DTO.user.UserDTO;
import service.BusinessValidationException;

import java.util.List;

/**
 * User management business layer.
 * <p>
 * Responsibilities:
 * - validate user data
 * - enforce basic business rules (unique username, existence checks)
 * - delegate persistence to {@link UserDAO}
 */
public class UserService {
    /** DAO dependency for user table operations. */
    private final UserDAO userDAO;

    /**
     * Creates service with default DAO implementation.
     */
    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Returns all users.
     *
     * @return list of users
     */
    public List<UserDTO> findAllUsers() {
        // No special rule: return DAO result directly.
        return userDAO.findAllUsers();
    }

    /**
     * Finds a user by username.
     *
     * @param username username to search
     * @return matching user or null
     * @throws BusinessValidationException when username is blank
     */
    public UserDTO findUserByUsername(String username) throws BusinessValidationException {
        if (isBlank(username)) {
            throw new BusinessValidationException("Username cannot be blank.");
        }
        // Normalize lookup key.
        return userDAO.findByUsername(username.trim());
    }

    /**
     * Adds a new user after validation and duplicate check.
     *
     * @param userDTO user to add
     * @throws BusinessValidationException when validation fails or user exists
     */
    public void addUser(UserDTO userDTO) throws BusinessValidationException {
        // Step 1: validate field-level business rules.
        validateUser(userDTO, true);
        // Step 2: enforce unique username.
        UserDTO existing = userDAO.findByUsername(userDTO.getUsername().trim());
        if (existing != null) {
            throw new BusinessValidationException("Username already exists.");
        }
        // Step 3: persist user.
        userDAO.addUser(userDTO);
    }

    /**
     * Updates an existing user.
     *
     * @param userDTO user to update
     * @throws BusinessValidationException when validation fails or user missing
     */
    public void updateUser(UserDTO userDTO) throws BusinessValidationException {
        // Step 1: validate update payload.
        validateUser(userDTO, false);
        // Step 2: ensure target user still exists.
        UserDTO existing = userDAO.findByUsername(userDTO.getUsername().trim());
        if (existing == null) {
            throw new BusinessValidationException("User not found.");
        }
        // Step 3: persist updated data.
        userDAO.updateUser(userDTO);
    }

    /**
     * Deletes a user by username.
     *
     * @param username username to delete
     * @throws BusinessValidationException when username is blank or missing
     */
    public void deleteUser(String username) throws BusinessValidationException {
        if (isBlank(username)) {
            throw new BusinessValidationException("Username cannot be blank.");
        }
        // Ensure target exists before issuing delete.
        UserDTO existing = userDAO.findByUsername(username.trim());
        if (existing == null) {
            throw new BusinessValidationException("User not found.");
        }
        // Delete by unique username key.
        userDAO.deleteUser(username.trim());
    }

    /**
     * Validates user payload shared by add/update flows.
     *
     * @param userDTO user data object to validate
     * @param requireUsername true for add flow; false for update flow
     * @throws BusinessValidationException when any rule is violated
     */
    private void validateUser(UserDTO userDTO, boolean requireUsername) throws BusinessValidationException {
        if (userDTO == null) {
            throw new BusinessValidationException("User cannot be null.");
        }
        // Username is required for create flow.
        if (requireUsername && isBlank(userDTO.getUsername())) {
            throw new BusinessValidationException("Username is required.");
        }
        // Basic field validation rules.
        if (isBlank(userDTO.getPassword()) || userDTO.getPassword().length() < 8) {
            throw new BusinessValidationException("Password must be at least 8 characters");
        }
        if (isBlank(userDTO.getFirstName())) {
            throw new BusinessValidationException("First name is required");
        }
        if (isBlank(userDTO.getLastName())) {
            throw new BusinessValidationException("Last name is required");
        }
        if (isBlank(userDTO.getEmail()) || !userDTO.getEmail().contains("@")) {
            throw new BusinessValidationException("Email is invalid");
        }
        if (isBlank(userDTO.getPhone())) {
            throw new BusinessValidationException("Phone is required");
        }
    }

    /**
     * Null-safe blank check utility.
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
