import DAO.user.UserDAO;
import DAO.user.UserDAOImpl;
import DTO.user.Member;
import DTO.user.UserDTO;

import java.util.List;

public class UserDaoManualTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        String username = "u_test_001";

        Member member = new Member(username, "pass123");
        member.setFirstName("Test");
        member.setLastName("User");
        member.setEmail("test.user@mail.com");
        member.setPhone("6135550000");

        userDAO.addUser(member);
        System.out.println("ADD OK");

        UserDTO found = userDAO.findByUsername(username);
        System.out.println("FOUND: " + (found != null));

        member.setEmail("updated.user@mail.com");
        userDAO.updateUser(member);
        UserDTO updated = userDAO.findByUsername(username);
        System.out.println("UPDATED FOUND: " + (updated != null));

        List<UserDTO> users = userDAO.findAllUsers();
        System.out.println("TOTAL USERS: " + users.size());

        userDAO.deleteUser(username);
        UserDTO deleted = userDAO.findByUsername(username);
        System.out.println("DELETED: " + (deleted == null));
    }
}
