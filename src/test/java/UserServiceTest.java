import DAO.user.UserDAO;
import DTO.user.Admin;
import DTO.user.Member;
import DTO.user.UserDTO;
import org.junit.jupiter.api.Test;
import service.BusinessValidationException;
import service.user.UserService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {
    @Test
    void addUserHashesPasswordBeforePersisting() throws Exception {
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        UserService userService = new UserService(userDAO);
        Member member = new Member("member1", "pass1234", "Test", "User", "test@mail.com", "6135550000");

        userService.addUser(member);

        UserDTO stored = userDAO.findByUsername("member1");
        assertNotNull(stored);
        assertTrue(stored.getPassword().startsWith("$2a$"));
        assertTrue(member.getPassword().startsWith("$2a$"));
    }

    @Test
    void addUserRejectsDuplicateUsername() {
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        userDAO.addUser(new Member("member1", "hashed", "Old", "User", "old@mail.com", "6135550000"));
        UserService userService = new UserService(userDAO);

        Member member = new Member("member1", "pass1234", "Test", "User", "test@mail.com", "6135550000");

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> userService.addUser(member));
        assertEquals("Username already exists.", ex.getMessage());
    }

    @Test
    void updateUserHashesRawPasswordBeforePersisting() throws Exception {
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        userDAO.addUser(new Admin("admin1", "$2a$10$abcdefghijklmnopqrstuuuuuuuuuuuuuuuuuuuuuuuuuuuu", "Admin", "User", "admin@mail.com", "6135551111"));
        UserService userService = new UserService(userDAO);

        Admin admin = new Admin("admin1", "newpass123", "Admin", "User", "admin@mail.com", "6135551111");
        userService.updateUser(admin);

        UserDTO stored = userDAO.findByUsername("admin1");
        assertNotNull(stored);
        assertTrue(stored.getPassword().startsWith("$2a$"));
        assertTrue(!"newpass123".equals(stored.getPassword()));
    }

    @Test
    void deleteUserRejectsMissingUser() {
        UserService userService = new UserService(new InMemoryUserDAO());

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> userService.deleteUser("ghost"));
        assertEquals("User not found.", ex.getMessage());
    }

    private static final class InMemoryUserDAO implements UserDAO {
        private final Map<String, UserDTO> users = new LinkedHashMap<>();

        @Override
        public void addUser(UserDTO userDTO) {
            users.put(userDTO.getUsername(), cloneUser(userDTO));
        }

        @Override
        public void updateUser(UserDTO userDTO) {
            users.put(userDTO.getUsername(), cloneUser(userDTO));
        }

        @Override
        public void deleteUser(String username) {
            users.remove(username);
        }

        @Override
        public List<UserDTO> findAllUsers() {
            return new ArrayList<>(users.values());
        }

        @Override
        public UserDTO findByUsername(String username) {
            UserDTO user = users.get(username);
            return user == null ? null : cloneUser(user);
        }

        @Override
        public List<UserDTO> searchUsersByPage(String keyword, int offset, int limit) {
            return new ArrayList<>(users.values());
        }

        @Override
        public int countUsersBySearch(String keyword) {
            return users.size();
        }

        private UserDTO cloneUser(UserDTO user) {
            if (user.isAdmin()) {
                return new Admin(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
            }
            return new Member(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
        }
    }
}
