package service.user;

import DTO.user.UserDTO;

import java.util.EnumSet;
import java.util.Set;

/**
 * Authorization business layer.
 * <p>
 * Converts user role (admin/member) into a feature set and
 * answers whether a specific feature is allowed.
 */
public class AuthorizationService {

    /**
     * Checks whether a user can access a feature.
     *
     * @param user user to check
     * @param feature feature to check
     * @return true if allowed
     */
    public boolean canAccess(UserDTO user, Feature feature) {
        // If identity or feature is missing, deny by default.
        if (user == null || feature == null) {
            return false;
        }
        // Authorize by checking membership in computed permissions.
        return getAllowedFeatures(user).contains(feature);
    }

    /**
     * Returns the feature set allowed for the given user.
     *
     * @param user user to evaluate
     * @return allowed feature set
     */
    public Set<Feature> getAllowedFeatures(UserDTO user) {
        // No user -> no permissions.
        if (user == null) {
            return EnumSet.noneOf(Feature.class);
        }

        // Admin role has full access.
        if (user.isAdmin()) {
            return EnumSet.allOf(Feature.class);
        }

        // Member role has limited access.
        return EnumSet.of(
            Feature.BORROW_RETURN_BOOK,
            Feature.SEARCH_BOOK,
            Feature.MANAGE_OWN_PROFILE,
            Feature.PAY_LATE_FEE
        );
    }
}
