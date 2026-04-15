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

    /**
     * Returns one publisher by ID.
     *
     * @param publisherID publisher primary key
     * @return matching publisher or null when not found
     */
    PublisherDTO findPublisherByID(int publisherID);

    /**
     * Inserts a new publisher.
     *
     * @param publisher publisher payload
     */
    void addPublisher(PublisherDTO publisher);

    /**
     * Updates an existing publisher.
     *
     * @param publisher publisher payload with existing ID
     */
    void updatePublisher(PublisherDTO publisher);

    /**
     * Deletes a publisher by ID.
     *
     * @param publisherID publisher primary key
     */
    void deletePublisher(int publisherID);

    /**
     * Retrieves a specific publisher matching the search keyword.
     *
     * @param keyword the search string
     * @return a list of publisher for the requested page
     */
    List<PublisherDTO> searchPublishers(String keyword);
}
