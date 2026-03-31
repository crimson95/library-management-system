package service.user;

import DTO.user.UserDTO;
import service.BusinessValidationException;

/**
 * Authentication business layer.
 * <p>
 * Responsibilities:
 * - user login validation
 * - registration orchestration (delegates to {@link UserService})
 * <p>
 * This class does not directly access DAO.
 */
public class AuthService {
    /** Shared user business service used for lookup/create operations. */
    private final UserService userService;

    /**
     * Creates authentication service with default dependencies.
     */
    public AuthService() {
        this.userService = new UserService();
    }

    /**
     * Registers a new user after validation.
     *
     * @param userDTO user to register
     * @return the persisted user
     * @throws BusinessValidationException when validation fails
     */
    public UserDTO register(UserDTO userDTO) throws BusinessValidationException {
        // Step 1: Validate and save new user through UserService rules.
        userService.addUser(userDTO);
        // Step 2: Load latest persisted state and return to caller.
        return userService.findUserByUsername(userDTO.getUsername());
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username username credential
     * @param password password credential
     * @return authenticated user
     * @throws BusinessValidationException when credentials are invalid
     */
    public UserDTO login(String username, String password) throws BusinessValidationException {
        // Basic credential presence check before any DB access.
        if (isBlank(username) || isBlank(password)) {
            throw new BusinessValidationException("username and password are required");
        }

        // Lookup account by normalized username.
        UserDTO user = userService.findUserByUsername(username.trim());
        if (user == null) {
            throw new BusinessValidationException("account does not exist");
        }

        // Compare raw password input against account object authentication logic.
        if (!user.login(password.trim())) {
            throw new BusinessValidationException("incorrect password");
        }
        return user;
    }

    /**
     * Null-safe blank checker used by credential validation.
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
