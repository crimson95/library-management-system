package DAO.book;

import DTO.book.BookDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO contract for logical book title records.
 * <p>
 * One record here represents one title (ISBN-level), not per-copy inventory rows.
 */
public interface BookDAO {
    /**
     * Adds a new book using its own connection lifecycle.
     *
     * @param bookDTO The BookDTO object containing the new book's details.
     */
    void addBook(BookDTO bookDTO);

    /**
     * Adds a new book to the database within an externally managed transaction.
     * @param con The database connection provided by the Service layer.
     * WARNING: Do NOT call con.close() within this method.
     * @param bookDTO The BookDTO object containing the new book's details.
     * @throws SQLException If a database access error occurs, allowing the Service layer to trigger a rollback.
     */
    void addBook(Connection con, BookDTO bookDTO) throws SQLException;

    /**
     * Deletes a book by ISBN within an externally managed transaction.
     *
     * @param con active transaction connection
     * @param isbn book ISBN
     * @throws SQLException when delete fails
     */
    void deleteBook(Connection con, String isbn) throws SQLException;

    /**
     * Updates an existing book.
     *
     * @param bookDTO book to update
     */
    void updateBook(BookDTO bookDTO);

    /**
     * Deletes a book by ISBN.
     *
     * @param isbn book ISBN
     */
    void deleteBook(String isbn);

    /**
     * Returns all books.
     *
     * @return list of books
     */
    List<BookDTO> findAllBooks();

    /**
     * Finds a book by ISBN.
     *
     * @param isbn book ISBN
     * @return matching book or null
     */
    BookDTO findByISBN(String isbn);

    /**
     * Retrieves a specific page of books matching the search keyword.
     *
     * @param keyword the search string (title, author, or ISBN)
     * @param offset the starting row index
     * @param limit the maximum number of rows to return
     * @return a list of books for the requested page
     */
    List<BookDTO> searchBooksByPage(String keyword, int offset, int limit);

    /**
     * Counts the total number of books matching the search keyword.
     * Used to calculate the total number of pages.
     *
     * @param keyword the search string
     * @return total number of matching books
     */
    int countBooksBySearch(String keyword);
}
