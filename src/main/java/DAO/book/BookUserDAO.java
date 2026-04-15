package DAO.book;

import DTO.book.BookUserDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for borrow/return records.
 */
public interface BookUserDAO {
    /**
     * Inserts a new borrow record.
     *
     * @param bookUserDTO borrow record to insert
     */
    void addBookUser(BookUserDTO bookUserDTO);

    /**
     * Inserts a new borrow record within an externally managed transaction.
     *
     * @param con active transaction connection
     * @param bookUserDTO borrow record to insert
     * @throws SQLException when insert fails
     */
    void addBookUser(Connection con, BookUserDTO bookUserDTO) throws SQLException;

    /**
     * Updates a borrow record.
     *
     * @param bookUserDTO borrow record to update
     */
    void updateBookUser(BookUserDTO bookUserDTO);

    /**
     * Updates a borrow record within an externally managed transaction.
     *
     * @param con active transaction connection
     * @param bookUserDTO borrow record to update
     * @throws SQLException when update fails
     */
    void updateBookUser(Connection con, BookUserDTO bookUserDTO) throws SQLException;

    /**
     * Deletes a borrow record by id.
     *
     * @param bookUserID borrow record id
     */
    void deleteBookUser(int bookUserID);

    /**
     * Returns all borrow records.
     *
     * @return list of borrow records
     */
    List<BookUserDTO> getAllBookUser();

    /**
     * Returns a borrow record by id.
     *
     * @param bookUserID borrow record id
     * @return matching record or null
     */
    BookUserDTO getBookUserByID(int bookUserID);

    /**
     * Counts active borrow records that are already overdue.
     *
     * @return overdue record count
     */
    int countOverdueRecords();

    /**
     * Returns all borrow records for a specific user.
     *
     * @param username the reader's username
     * @return list of borrow records belonging to the user
     */
    List<BookUserDTO> getBookUserByUsername(String username);
}
