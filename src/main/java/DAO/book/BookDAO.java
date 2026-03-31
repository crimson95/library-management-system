package DAO.book;

import DTO.book.BookDTO;

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
}
