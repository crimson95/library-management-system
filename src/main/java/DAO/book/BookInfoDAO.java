package DAO.book;

import DTO.book.BookInfoDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for physical book copies.
 */
public interface BookInfoDAO {
    /**
     * Adds a new physical book copy using its own connection lifecycle.
     *
     * @param bookInfoDTO The BookInfoDTO object representing the physical copy.
     */
    void addBookInfo(BookInfoDTO bookInfoDTO);

    /**
     * Adds a new physical book copy (inventory record) within an externally managed transaction.
     *
     * @param con The active database connection provided by the Service layer.
     * WARNING: Do NOT commit or close this connection within this method.
     * @param bookInfoDTO The BookInfoDTO object representing the physical copy.
     * @throws SQLException If a database error occurs, allowing the Service layer to trigger a rollback.
     */
    void addBookInfo(Connection con, BookInfoDTO bookInfoDTO) throws SQLException;

    /**
     * Updates a book copy within an externally managed transaction.
     *
     * @param con active transaction connection
     * @param bookInfoDTO book copy to update
     * @throws SQLException when update fails
     */
    void updateBookInfo(Connection con, BookInfoDTO bookInfoDTO) throws SQLException;

    /**
     * Updates a book copy.
     *
     * @param bookInfoDTO book copy to update
     */
    void updateBookInfo(BookInfoDTO bookInfoDTO);

    /**
     * Deletes a book copy by id within an externally managed transaction.
     *
     * @param con active transaction connection
     * @param bookID book copy id
     * @throws SQLException when delete fails
     */
    void deleteBookInfo(Connection con, int bookID) throws SQLException;

    /**
     * Deletes a book copy by id.
     *
     * @param bookID book copy id
     */
    void deleteBookInfo(int bookID);

    /**
     * Returns all book copies.
     *
     * @return list of book copies
     */
    List<BookInfoDTO> getAllBookInfo();

    /**
     * Returns a book copy by ID.
     *
     * @param bookID book copy ID
     * @return matching book copy or null
     */
    BookInfoDTO getBookInfoByID(int bookID);

    /**
     * Returns book info by ISBN.
     *
     * @param isbn book ISBN
     * @return matching book info or null
     */
    List<BookInfoDTO> getBookInfoByISBN(String isbn);

    /**
     * Counts all physical copies in inventory.
     *
     * @return total number of book copies
     */
    int countTotalCopies();

    /**
     * Counts physical copies by status value.
     *
     * @param status copy status code
     * @return number of copies in that status
     */
    int countCopiesByStatus(int status);
}
