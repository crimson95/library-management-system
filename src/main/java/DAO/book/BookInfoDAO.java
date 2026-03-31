package DAO.book;

import DTO.book.BookInfoDTO;

import java.util.List;

/**
 * DAO for physical book copies.
 */
public interface BookInfoDAO {
    /**
     * Inserts a new book copy.
     *
     * @param bookInfoDTO book copy to insert
     */
    void addBookInfo(BookInfoDTO bookInfoDTO);

    /**
     * Updates a book copy.
     *
     * @param bookInfoDTO book copy to update
     */
    void updateBookInfo(BookInfoDTO bookInfoDTO);

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
