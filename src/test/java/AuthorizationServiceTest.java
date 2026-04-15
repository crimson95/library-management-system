import DTO.user.Admin;
import DTO.user.Member;
import org.junit.jupiter.api.Test;
import service.user.AuthorizationService;
import service.user.Feature;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorizationServiceTest {
    private final AuthorizationService authorizationService = new AuthorizationService();

    @Test
    void adminHasAccessToAllFeatures() {
        Admin admin = new Admin("admin", "password");

        assertEquals(EnumSet.allOf(Feature.class), authorizationService.getAllowedFeatures(admin));
    }

    @Test
    void memberHasOnlyMemberFeatures() {
        Member member = new Member("member", "password");

        assertTrue(authorizationService.canAccess(member, Feature.BORROW_RETURN_BOOK));
        assertTrue(authorizationService.canAccess(member, Feature.SEARCH_BOOK));
        assertFalse(authorizationService.canAccess(member, Feature.MANAGE_USERS));
    }

    @Test
    void nullUserOrFeatureIsDenied() {
        Member member = new Member("member", "password");

        assertFalse(authorizationService.canAccess(null, Feature.SEARCH_BOOK));
        assertFalse(authorizationService.canAccess(member, null));
    }
}
