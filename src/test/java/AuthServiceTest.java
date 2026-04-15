import DAO.user.UserDAO;
import DTO.user.Member;
import DTO.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.BusinessValidationException;
import service.user.AuthService;
import service.user.UserService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest {
    @Test
    void registerReturnsPersistedUser() throws Exception {
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        AuthService authService = new AuthService(new UserService(userDAO));
        Member member = new Member("member1", "pass1234", "Test", "User", "test@mail.com", "6135550000");

        UserDTO created = authService.register(member);

        assertNotNull(created);
        assertEquals("member1", created.getUsername());
        assertNotNull(userDAO.findByUsername("member1"));
    }

    @Test
    void loginAcceptsBcryptPassword() throws Exception {
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        String hashed = BCrypt.hashpw("pass1234", BCrypt.gensalt());
        userDAO.addUser(new Member("member1", hashed, "Test", "User", "test@mail.com", "6135550000"));
        AuthService authService = new AuthService(new UserService(userDAO));

        UserDTO loggedIn = authService.login("member1", "pass1234");

        assertNotNull(loggedIn);
        assertEquals("member1", loggedIn.getUsername());
    }

    @Test
    void loginRejectsIncorrectPassword() {
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        String hashed = BCrypt.hashpw("pass1234", BCrypt.gensalt());
        userDAO.addUser(new Member("member1", hashed, "Test", "User", "test@mail.com", "6135550000"));
        AuthService authService = new AuthService(new UserService(userDAO));

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> authService.login("member1", "wrongpass"));
        assertEquals("Incorrect password.", ex.getMessage());
    }

    @Test
    void loginRejectsBlankCredentials() {
        AuthService authService = new AuthService(new UserService(new InMemoryUserDAO()));

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> authService.login(" ", " "));
        assertEquals("Username and password are required.", ex.getMessage());
    }

    private static final class InMemoryUserDAO implements UserDAO {
        private final Map<String, UserDTO> users = new LinkedHashMap<>();

        @Override
        public void addUser(UserDTO userDTO) {
            users.put(userDTO.getUsername(), new Member(userDTO.getUsername(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPhone()));
        }

        @Override
        public void updateUser(UserDTO userDTO) {
            addUser(userDTO);
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
            return users.get(username);
        }

        @Override
        public List<UserDTO> searchUsersByPage(String keyword, int offset, int limit) {
            return new ArrayList<>(users.values());
        }

        @Override
        public int countUsersBySearch(String keyword) {
            return users.size();
        }
    }
}
