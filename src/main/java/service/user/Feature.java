package service.user;

/**
 * Feature constants used by {@link AuthorizationService}.
 * <p>
 * Keep this enum focused on business capabilities instead of screen names,
 * so authorization remains stable even when UI changes.
 */
public enum Feature {
    BORROW_RETURN_BOOK,
    SEARCH_BOOK,
    MANAGE_OWN_PROFILE,
    PAY_LATE_FEE,
    MANAGE_BOOKS,
    MANAGE_USERS
}
