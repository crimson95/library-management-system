package service.user;

import DAO.user.UserDAO;
import DAO.user.UserDAOImpl;
import DTO.user.UserDTO;
import org.mindrot.jbcrypt.BCrypt;
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
     * Creates service with injected DAO for testing or custom wiring.
     */
    public UserService(UserDAO userDAO) {
        if (userDAO == null) {
            throw new IllegalArgumentException("userDAO cannot be null.");
        }
        this.userDAO = userDAO;
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
     * Retrieves a paginated list of members, optionally filtered by search keyword.
     *
     * @param keyword search keyword (can be null or empty)
     * @param offset database offset
     * @param limit maximum records per page
     * @return list of matching members
     */
    public List<UserDTO> searchUsersByPage(String keyword, int offset, int limit) {
        return userDAO.searchUsersByPage(keyword, offset, limit);
    }

    /**
     * Calculates the total number of members matching the search keyword.
     *
     * @param keyword search keyword (can be null or empty)
     * @return total count of matching members
     */
    public int countUsersBySearch(String keyword) {
        return userDAO.countUsersBySearch(keyword);
    }

    /**
     * Adds a new user after validation and duplicate check.
     *
     * @param userDTO user to add
     * @throws BusinessValidationException when validation fails or user exists
     */
    public void addUser(UserDTO userDTO) throws BusinessValidationException {
        // Step 1: validate field-level business rules (validates the raw password length).
        validateUser(userDTO, true);
        // Step 2: enforce unique username.
        UserDTO existing = userDAO.findByUsername(userDTO.getUsername().trim());
        if (existing != null) {
            throw new BusinessValidationException("Username already exists.");
        }
        // Step 3: Hash the password using BCrypt before persisting.
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(),  BCrypt.gensalt());
        userDTO.setPassword(hashedPassword);

        // Step 4: persist user.
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

        // Step 3: Hash password only if it was changed to a new raw password.
        // BCrypt hashes always start with "$2a$" and are 60 characters long.
        if(userDTO.getPassword() != null && !userDTO.getPassword().startsWith("$2a$")) {
            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(),  BCrypt.gensalt());
            userDTO.setPassword(hashedPassword);
        }

        // Step 4: persist updated data.
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
