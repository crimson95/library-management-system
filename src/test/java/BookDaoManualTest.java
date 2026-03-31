import DAO.book.BookDAO;
import DAO.book.BookDAOImpl;
import DTO.book.BookDTO;

import java.sql.Date;
import java.util.List;

public class BookDaoManualTest {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAOImpl();
        String testIsbn = "TEST-BOOK-0001";

        BookDTO newBook = new BookDTO(
            testIsbn,
            "DAO Test Book",
            Date.valueOf("2026-02-27"),
            "created by BookDaoManualTest",
            1,
            "Yoji Sakamura",
            1,
            "Kadokawa"
        );

        bookDAO.addBook(newBook);
        System.out.println("ADD OK");

        BookDTO found = bookDAO.findByISBN(testIsbn);
        System.out.println("FOUND: " + (found != null));

        BookDTO updatedBook = new BookDTO(
            testIsbn,
            "DAO Test Book Updated",
            Date.valueOf("2026-03-01"),
            "updated by BookDaoManualTest",
            2,
            "Simon Lu",
            2,
            "TongLi"
        );
        bookDAO.updateBook(updatedBook);

        BookDTO updated = bookDAO.findByISBN(testIsbn);
        boolean isUpdated = updated != null
            && "DAO Test Book Updated".equals(updated.getTitle())
            && updated.getAuthorID() == 2
            && updated.getPublisherID() == 2;
        System.out.println("UPDATED: " + isUpdated);

        List<BookDTO> allBooks = bookDAO.findAllBooks();
        System.out.println("TOTAL BOOKS: " + allBooks.size());

        bookDAO.deleteBook(testIsbn);
        BookDTO deleted = bookDAO.findByISBN(testIsbn);
        System.out.println("DELETED: " + (deleted == null));
    }
}
