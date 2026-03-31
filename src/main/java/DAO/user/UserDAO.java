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
}
