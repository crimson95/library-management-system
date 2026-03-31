package DTO.book;

/**
 * Data transfer object for author records.
 */
public class AuthorDTO {
    private int authorID;
    private String first_name;
    private String last_name;

    /**
     * Creates an author DTO.
     *
     * @param authorID author id
     * @param first_name first name
     * @param last_name last name
     */
    public AuthorDTO(int authorID, String first_name, String last_name){
        this.authorID = authorID;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public int getAuthorID(){
        return authorID;
    }

    public void setAuthorID(int authorID){
        this.authorID = authorID;
    }

    public String getFirst_name(){
        return first_name;
    }

    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    public String getLast_name(){
        return last_name;
    }

    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    // Returns a display-friendly full name (handles missing parts safely).
    public String getFullName() {
        String first = first_name == null ? "" : first_name.trim();
        String last = last_name == null ? "" : last_name.trim();
        if (first.isEmpty()) return last;
        if (last.isEmpty()) return first;
        return first + " " + last;
    }
}
