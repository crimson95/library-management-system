package DAO.book;

import java.util.List;
import DTO.book.AuthorDTO;

/**
 * DAO contract for author master data.
 */
public interface AuthorDAO {
    /**
     * Returns all authors sorted by first name.
     *
     * @return list of authors
     */
    List<AuthorDTO> findAllAuthors();

    /**
     * Returns one author by ID.
     *
     * @param authorID author primary key
     * @return matching author or null when not found
     */
    AuthorDTO findAuthorByID(int authorID);

    /**
     * Inserts a new author.
     *
     * @param author author payload
     */
    void addAuthor(AuthorDTO author);

    /**
     * Updates an existing author.
     *
     * @param author author payload with existing ID
     */
    void updateAuthor(AuthorDTO author);

    /**
     * Deletes an author by ID.
     *
     * @param authorID author primary key
     */
    void deleteAuthor(int authorID);

    /**
     * Retrieves a specific author matching the search keyword.
     *
     * @param keyword the search string
     * @return a list of author for the requested page
     */
    List<AuthorDTO> searchAuthors(String keyword);
}
