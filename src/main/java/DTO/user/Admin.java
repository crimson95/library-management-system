package DTO.user;

/**
 * Admin user type.
 */
public class Admin extends UserDTO {
    /**
     * Creates an admin with minimal credentials.
     *
     * @param username username
     * @param password password
     */
    public Admin(String username, String password){
        super(username, password);
    }

    /**
     * Creates an admin with full profile data.
     *
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @param email email
     * @param phone phone
     */
    public Admin(String username, String password, String firstName, String lastName, String email, String phone) {
        super(username, password, firstName, lastName, email, phone);
    }

    @Override
    public boolean isAdmin(){
        return true;
    }
}
