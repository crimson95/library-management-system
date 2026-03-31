package DTO.user;

/**
 * Member user type.
 */
public class Member extends UserDTO {
    private int borrowedCount;

    /**
     * Creates a member with minimal credentials.
     *
     * @param username username
     * @param password password
     */
    public Member(String username, String password){
        super(username,password);
        this.borrowedCount = 0;
    }

    /**
     * Creates a member with full profile data.
     *
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @param email email
     * @param phone phone
     */
    public Member(String username, String password, String firstName, String lastName, String email, String phone) {
        super(username, password, firstName, lastName, email, phone);
        this.borrowedCount = 0;
    }

    public int getBorrowedCount(){
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    @Override
    public boolean isAdmin(){
        return false;
    }
}
