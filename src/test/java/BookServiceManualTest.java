import DAO.book.BookUserDAO;
import DAO.book.BookUserDAOImpl;
import DTO.book.BookDTO;
import DTO.book.BookInfoDTO;
import DTO.book.BookUserDTO;
import service.book.BookService;

import java.sql.Date;
import java.util.List;

public class BookServiceManualTest {
    public static void main(String[] args) throws Exception {
        BookService bookService = new BookService();
        BookUserDAO bookUserDAO = new BookUserDAOImpl();

        String isbn = "MANUAL-SVC-" + System.currentTimeMillis();
        String borrower = "test";

        BookDTO book = new BookDTO(
            isbn,
            "Manual Service Test Book",
            Date.valueOf("2026-04-15"),
            "created by BookServiceManualTest",
            1,
            "Simon Lu",
            1,
            "Kadokawa"
        );

        bookService.addBook(book);
        System.out.println("ADD BOOK OK");

        List<BookInfoDTO> copies = bookService.findCopiesByISBN(isbn);
        BookInfoDTO createdCopy = copies.isEmpty() ? null : copies.get(0);
        System.out.println("INITIAL COPY CREATED: " + (createdCopy != null));
        if (createdCopy == null) {
            return;
        }

        bookService.borrowBook(borrower, createdCopy.getBookID());
        BookInfoDTO borrowedCopy = bookService.findCopyByID(createdCopy.getBookID());
        System.out.println("BORROWED: " + (borrowedCopy.getStatus() == BookInfoDTO.STATUS_BORROWED));

        BookUserDTO borrowRecord = bookUserDAO.getAllBookUser().stream()
            .filter(r -> borrower.equals(r.getUsername()) && r.getBookID() == createdCopy.getBookID() && r.getReturnDate() == null)
            .findFirst()
            .orElse(null);
        System.out.println("BORROW RECORD CREATED: " + (borrowRecord != null));
        if (borrowRecord == null) {
            return;
        }

        bookService.returnBook(borrowRecord.getBookUserID());
        BookUserDTO returnedRecord = bookUserDAO.getBookUserByID(borrowRecord.getBookUserID());
        BookInfoDTO returnedCopy = bookService.findCopyByID(createdCopy.getBookID());
        System.out.println("RETURNED: " + (returnedRecord != null && returnedRecord.getReturnDate() != null));
        System.out.println("COPY AVAILABLE AGAIN: " + (returnedCopy.getStatus() == BookInfoDTO.STATUS_AVAILABLE));

        // Cleanup historical borrow record first because Book_User references Book_Info.
        bookUserDAO.deleteBookUser(borrowRecord.getBookUserID());
        bookService.deleteBook(isbn);
        boolean deleted = bookService.findAllBooks().stream().noneMatch(b -> isbn.equals(b.getIsbn()));
        System.out.println("BOOK CLEANUP OK: " + deleted);
    }
}
