package DTO.user;

/**
 * Base data transfer object for user accounts.
 */
public abstract class UserDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    /**
     * Creates a user DTO with full profile data.
     *
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @param email email
     * @param phone phone
     */
    public UserDTO(String username, String password, String firstName, String lastName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Creates a user DTO with minimal credentials.
     *
     * @param username username
     * @param password password
     */
    public UserDTO(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * Compares a raw password to the stored value.
     *
     * @param inputPassword password to compare
     * @return true if the password matches
     */
    public boolean login(String inputPassword){
        return inputPassword != null && inputPassword.equals(this.password);
    }

    /**
     * Returns whether this user is an administrator.
     *
     * @return true if admin
     */
    public abstract boolean isAdmin();
}
