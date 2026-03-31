package DTO.book;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Data transfer object for borrow records.
 */
public class BookUserDTO {
    private int bookUserID;
    private Date startDate;
    private Date returnDate;
    private BigDecimal lateFee;
    private String username;
    private int bookID;

    /**
     * Creates a borrow record DTO.
     *
     * @param bookUserID borrow record id
     * @param startDate borrow start date
     * @param returnDate return date (nullable)
     * @param lateFee late fee
     * @param username borrower username
     * @param bookID book copy id
     */
    public BookUserDTO(int bookUserID, Date startDate, Date returnDate, BigDecimal lateFee, String username, int bookID) {
        this.bookUserID = bookUserID;
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.lateFee = lateFee;
        this.username = username;
        this.bookID = bookID;
    }

    public int getBookUserID() {
        return bookUserID;
    }
    public void setBookUserID(int bookUserID) {
        this.bookUserID = bookUserID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}
