package DAO.book;

import DTO.book.BookDTO;

import java.awt.print.Book;
import java.util.List;

/**
 * DAO contract for logical book title records.
 * <p>
 * One record here represents one title (ISBN-level), not per-copy inventory rows.
 */
public interface BookDAO {
    /**
     * Inserts a new book.
     *
     * @param bookDTO book to insert
     */
    void addBook(BookDTO bookDTO);

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
