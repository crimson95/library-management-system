package DTO.book;

/**
 * Data transfer object for physical book copy records.
 */
public class BookInfoDTO {
    private int bookID;
    private String condition;
    private int status;
    private String bookISBN;

    public static final int STATUS_BORROWED = 0;
    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_REPAIR = 2;

    /**
     * Creates a book copy DTO.
     *
     * @param bookID copy id
     * @param condition copy condition
     * @param status availability status
     * @param bookISBN parent book ISBN
     */
    public BookInfoDTO(int bookID, String condition, int status, String bookISBN) {
        this.bookID = bookID;
        this.condition = condition;
        this.status = status;
        this.bookISBN = bookISBN;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }
}
