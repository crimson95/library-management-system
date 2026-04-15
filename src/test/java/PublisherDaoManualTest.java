import DAO.book.PublisherDAO;
import DAO.book.PublisherDAOImpl;
import DTO.book.PublisherDTO;

import java.util.List;

public class PublisherDaoManualTest {
    public static void main(String[] args) {
        PublisherDAO publisherDAO = new PublisherDAOImpl();
        String publisherName = "ManualPublisher-" + System.currentTimeMillis();

        PublisherDTO publisher = new PublisherDTO(0, publisherName);
        publisherDAO.addPublisher(publisher);
        System.out.println("ADD OK");

        List<PublisherDTO> publishers = publisherDAO.searchPublishers(publisherName);
        PublisherDTO created = publishers.stream()
            .filter(p -> publisherName.equals(p.getPublisherName()))
            .findFirst()
            .orElse(null);
        System.out.println("FOUND CREATED: " + (created != null));
        if (created == null) {
            return;
        }

        PublisherDTO found = publisherDAO.findPublisherByID(created.getPublisherID());
        System.out.println("GET BY ID: " + (found != null));

        created.setPublisherName(publisherName + "-UPDATED");
        publisherDAO.updatePublisher(created);
        PublisherDTO updated = publisherDAO.findPublisherByID(created.getPublisherID());
        boolean isUpdated = updated != null && (publisherName + "-UPDATED").equals(updated.getPublisherName());
        System.out.println("UPDATED: " + isUpdated);

        publisherDAO.deletePublisher(created.getPublisherID());
        PublisherDTO deleted = publisherDAO.findPublisherByID(created.getPublisherID());
        System.out.println("DELETED: " + (deleted == null));
    }
}
