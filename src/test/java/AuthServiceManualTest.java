import DTO.user.Admin;
import DTO.user.Member;
import DTO.user.UserDTO;
import service.user.AuthService;
import service.user.AuthorizationService;
import service.user.Feature;
import service.user.UserService;

public class AuthServiceManualTest {
    public static void main(String[] args) throws Exception {
        AuthService authService = new AuthService();
        AuthorizationService authorizationService = new AuthorizationService();
        UserService userService = new UserService();

        String memberUsername = "member_test_01";
        String adminUsername = "admin_test_01";

        Member member = new Member(
            memberUsername, "pass123",
            "Mem", "Ber", "member@test.com", "6131112222"
        );
        Admin admin = new Admin(
            adminUsername, "admin123",
            "Ad", "Min", "admin@test.com", "6139990000"
        );

        authService.register(member);
        authService.register(admin);

        UserDTO loggedInMember = authService.login(memberUsername, "pass123");
        UserDTO loggedInAdmin = authService.login(adminUsername, "admin123");

        System.out.println("MEMBER LOGIN: " + (loggedInMember != null));
        System.out.println("ADMIN LOGIN: " + (loggedInAdmin != null));
        System.out.println("MEMBER CAN BORROW: " + authorizationService.canAccess(loggedInMember, Feature.BORROW_RETURN_BOOK));
        System.out.println("MEMBER CAN MANAGE USERS: " + authorizationService.canAccess(loggedInMember, Feature.MANAGE_USERS));
        System.out.println("ADMIN CAN MANAGE USERS: " + authorizationService.canAccess(loggedInAdmin, Feature.MANAGE_USERS));

        userService.deleteUser(loggedInMember.getUsername());
        userService.deleteUser(loggedInAdmin.getUsername());
    }
}
