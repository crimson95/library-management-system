import DAO.book.AuthorDAO;
import DAO.book.AuthorDAOImpl;
import DTO.book.AuthorDTO;

import java.util.List;

public class AuthorDaoManualTest {
    public static void main(String[] args) {
        AuthorDAO authorDAO = new AuthorDAOImpl();
        String firstName = "Manual";
        String lastName = "Author" + System.currentTimeMillis();

        AuthorDTO author = new AuthorDTO(0, firstName, lastName);
        authorDAO.addAuthor(author);
        System.out.println("ADD OK");

        List<AuthorDTO> authors = authorDAO.searchAuthors(lastName);
        AuthorDTO created = authors.stream()
            .filter(a -> firstName.equals(a.getFirst_name()) && lastName.equals(a.getLast_name()))
            .findFirst()
            .orElse(null);
        System.out.println("FOUND CREATED: " + (created != null));
        if (created == null) {
            return;
        }

        AuthorDTO found = authorDAO.findAuthorByID(created.getAuthorID());
        System.out.println("GET BY ID: " + (found != null));

        created.setLast_name(lastName + "_UPDATED");
        authorDAO.updateAuthor(created);
        AuthorDTO updated = authorDAO.findAuthorByID(created.getAuthorID());
        boolean isUpdated = updated != null && (lastName + "_UPDATED").equals(updated.getLast_name());
        System.out.println("UPDATED: " + isUpdated);

        authorDAO.deleteAuthor(created.getAuthorID());
        AuthorDTO deleted = authorDAO.findAuthorByID(created.getAuthorID());
        System.out.println("DELETED: " + (deleted == null));
    }
}
