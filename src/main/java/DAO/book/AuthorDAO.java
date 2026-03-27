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
}
