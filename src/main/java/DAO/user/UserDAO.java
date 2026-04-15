package DAO.user;

import DTO.user.UserDTO;

import java.util.List;

/**
 * DAO contract for user account persistence.
 * <p>
 * Service layer depends on this abstraction instead of JDBC details.
 */
public interface UserDAO {
    /**
     * Inserts a new user.
     *
     * @param userDTO user to insert
     */
    void addUser(UserDTO userDTO);

    /**
     * Updates a user.
     *
     * @param userDTO user to update
     */
    void updateUser(UserDTO userDTO);

    /**
     * Deletes a user by username.
     *
     * @param username username
     */
    void deleteUser(String username);

    /**
     * Returns all users.
     *
     * @return list of users
     */
    List<UserDTO> findAllUsers();

    /**
     * Finds a user by username.
     *
     * @param username username
     * @return matching user or null
     */
    UserDTO findByUsername(String username);

    /**
     * Retrieves a specific page of users matching the search keyword.
     *
     * @param keyword the search string (username, first_name, or email)
     * @param offset the starting row index
     * @param limit the maximum number of rows to return
     * @return a list of users for the requested page
     */
    List<UserDTO> searchUsersByPage(String keyword, int offset, int limit);

    /**
     * Counts the total number of users matching the search keyword.
     * Used to calculate the total number of pages.
     *
     * @param keyword the search string
     * @return total number of matching users
     */
    int countUsersBySearch(String keyword);
}
