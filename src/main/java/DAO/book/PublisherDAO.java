package DAO.book;

import java.util.List;
import DTO.book.PublisherDTO;

/**
 * DAO contract for publisher master data.
 */
public interface PublisherDAO {
    /**
     * Returns all publishers sorted by name.
     *
     * @return list of publishers
     */
    List<PublisherDTO> findAllPublishers();
}
