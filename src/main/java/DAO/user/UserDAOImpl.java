package DAO.user;

import DAO.DataSource;
import DTO.user.Admin;
import DTO.user.Member;
import DTO.user.UserDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link UserDAO}.
 * <p>
 * This class maps table {@code User} records into concrete DTO types:
 * - {@link Admin} when {@code is_admin = 1}
 * - {@link Member} otherwise
 */
public class UserDAOImpl implements UserDAO {
    /** List query for admin member-management page. */
    private static final String QUERY_USERS = "SELECT * FROM `User` ORDER BY username DESC";
    /** Single-user lookup query by username key. */
    private static final String QUERY_USERNAME = "SELECT * FROM `User` WHERE username = ?";
    /** Insert query for new user record. */
    private static final String INSERT_USER = "INSERT INTO `User` (username, password, first_name, last_name, email, phone, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?)";
    /** Update query for existing user record. */
    private static final String UPDATE_USER = "UPDATE `User` SET password = ?, first_name = ?, last_name = ?, email = ?, phone = ?, is_admin = ? WHERE username = ?";
    /** Delete query by username key. */
    private static final String DELETE_USER = "DELETE FROM `User` WHERE username = ?";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws IOException, SQLException {
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Inserts a new user.
     */
    @Override
    public void addUser(UserDTO userDTO) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_USER)) {
            // Bind DTO fields to SQL parameters in order.
            ps.setString(1, userDTO.getUsername());
            ps.setString(2, userDTO.getPassword());
            ps.setString(3, userDTO.getFirstName());
            ps.setString(4, userDTO.getLastName());
            ps.setString(5, userDTO.getEmail());
            ps.setString(6, userDTO.getPhone());
            ps.setInt(7, userDTO.isAdmin() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("addUser() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a user by username.
     */
    @Override
    public void updateUser(UserDTO userDTO) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_USER)) {
            // Update fields by username (primary key).
            ps.setString(1, userDTO.getPassword());
            ps.setString(2, userDTO.getFirstName());
            ps.setString(3, userDTO.getLastName());
            ps.setString(4, userDTO.getEmail());
            ps.setString(5, userDTO.getPhone());
            ps.setInt(6, userDTO.isAdmin() ? 1 : 0);
            ps.setString(7, userDTO.getUsername());
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("updateUser() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a user by username.
     */
    @Override
    public void deleteUser(String username) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_USER)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("deleteUser() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all users.
     */
    @Override
    public List<UserDTO> findAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_USERS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Map each row into a UserDTO (Admin or Member).
                users.add(mapUser(rs));
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("findAllUsers() failed: " + e.getMessage(), e);
        }
        return users;
    }

    /**
     * Finds a user by username.
     */
    @Override
    public UserDTO findByUsername(String username) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_USERNAME)) {
            // Parameterized query prevents SQL injection.
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Return the first matching user.
                    return mapUser(rs);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("findByUsername() failed: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Maps one SQL row into either Admin or Member DTO.
     *
     * @param rs current row cursor
     * @return mapped user DTO subtype
     * @throws SQLException when required columns cannot be read
     */
    private UserDTO mapUser(ResultSet rs) throws SQLException {
        int isAdmin = 0;
        try {
            isAdmin = rs.getInt("is_admin");
        } catch (SQLException ignored) {
            // Backward compatibility for old schema without is_admin.
        }

        if (isAdmin == 1) {
            // Admin user.
            return new Admin(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone")
            );
        }

        // Default to member user.
        return new Member(
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phone")
        );
    }
}
