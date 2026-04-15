import DAO.book.AuthorDAO;
import DAO.book.BookDAO;
import DAO.book.BookInfoDAO;
import DAO.book.BookUserDAO;
import DAO.book.PublisherDAO;
import DTO.book.AuthorDTO;
import DTO.book.BookDTO;
import DTO.book.BookInfoDTO;
import DTO.book.BookUserDTO;
import DTO.book.PublisherDTO;
import org.junit.jupiter.api.Test;
import service.BusinessValidationException;
import service.book.BookService;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookServiceTest {
    @Test
    void addBookCommitsAndCreatesInitialCopy() throws Exception {
        FakeBookDAO bookDAO = new FakeBookDAO();
        FakeBookInfoDAO bookInfoDAO = new FakeBookInfoDAO();
        ConnectionTracker tracker = new ConnectionTracker();
        TestableBookService service = createService(bookDAO, bookInfoDAO, new FakeBookUserDAO(), tracker);

        BookDTO book = sampleBook("ISBN-1");
        service.addBook(book);

        assertEquals(book, bookDAO.transactionAddedBook);
        assertNotNull(bookInfoDAO.transactionAddedCopy);
        assertEquals("ISBN-1", bookInfoDAO.transactionAddedCopy.getBookISBN());
        assertTrue(tracker.committed);
        assertFalse(tracker.rolledBack);
        assertTrue(tracker.closed);
    }

    @Test
    void addBookRollsBackWhenCopyInsertFails() {
        FakeBookDAO bookDAO = new FakeBookDAO();
        FakeBookInfoDAO bookInfoDAO = new FakeBookInfoDAO();
        bookInfoDAO.failOnTransactionalAdd = true;
        ConnectionTracker tracker = new ConnectionTracker();
        TestableBookService service = createService(bookDAO, bookInfoDAO, new FakeBookUserDAO(), tracker);

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> service.addBook(sampleBook("ISBN-2")));

        assertEquals("System error: Could not add book. Change rolled back.", ex.getMessage());
        assertTrue(tracker.rolledBack);
        assertFalse(tracker.committed);
    }

    @Test
    void borrowBookCommitsAndUpdatesCopyStatus() throws Exception {
        FakeBookDAO bookDAO = new FakeBookDAO();
        FakeBookInfoDAO bookInfoDAO = new FakeBookInfoDAO();
        bookInfoDAO.byId.put(101, new BookInfoDTO(101, "Good", BookInfoDTO.STATUS_AVAILABLE, "ISBN-3"));
        FakeBookUserDAO bookUserDAO = new FakeBookUserDAO();
        ConnectionTracker tracker = new ConnectionTracker();
        TestableBookService service = createService(bookDAO, bookInfoDAO, bookUserDAO, tracker);

        service.borrowBook("member1", 101);

        assertNotNull(bookUserDAO.transactionAddedRecord);
        assertEquals("member1", bookUserDAO.transactionAddedRecord.getUsername());
        assertEquals(101, bookUserDAO.transactionAddedRecord.getBookID());
        assertEquals(BookInfoDTO.STATUS_BORROWED, bookInfoDAO.byId.get(101).getStatus());
        assertTrue(tracker.committed);
    }

    @Test
    void returnBookCalculatesLateFeeAndMakesCopyAvailable() throws Exception {
        FakeBookInfoDAO bookInfoDAO = new FakeBookInfoDAO();
        bookInfoDAO.byId.put(101, new BookInfoDTO(101, "Good", BookInfoDTO.STATUS_BORROWED, "ISBN-4"));
        FakeBookUserDAO bookUserDAO = new FakeBookUserDAO();
        bookUserDAO.byId.put(77, new BookUserDTO(77, Date.valueOf(LocalDate.now().minusDays(20)), null, BigDecimal.ZERO, "member1", 101));
        ConnectionTracker tracker = new ConnectionTracker();
        TestableBookService service = createService(new FakeBookDAO(), bookInfoDAO, bookUserDAO, tracker);

        service.returnBook(77);

        assertNotNull(bookUserDAO.transactionUpdatedRecord);
        assertNotNull(bookUserDAO.transactionUpdatedRecord.getReturnDate());
        assertEquals(new BigDecimal("9.00"), bookUserDAO.transactionUpdatedRecord.getLateFee());
        assertEquals(BookInfoDTO.STATUS_AVAILABLE, bookInfoDAO.byId.get(101).getStatus());
        assertTrue(tracker.committed);
    }

    @Test
    void deleteBookRejectsBorrowedCopiesBeforeOpeningTransaction() {
        FakeBookDAO bookDAO = new FakeBookDAO();
        bookDAO.byIsbn.put("ISBN-5", sampleBook("ISBN-5"));
        FakeBookInfoDAO bookInfoDAO = new FakeBookInfoDAO();
        bookInfoDAO.byIsbn.put("ISBN-5", List.of(new BookInfoDTO(1, "Good", BookInfoDTO.STATUS_BORROWED, "ISBN-5")));
        ConnectionTracker tracker = new ConnectionTracker();
        TestableBookService service = createService(bookDAO, bookInfoDAO, new FakeBookUserDAO(), tracker);

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> service.deleteBook("ISBN-5"));

        assertEquals("Cannot delete: One or more copies are currently borrowed.", ex.getMessage());
        assertFalse(tracker.connectionRequested);
    }

    @Test
    void deleteBookDeletesCopiesAndBookInSingleTransaction() throws Exception {
        FakeBookDAO bookDAO = new FakeBookDAO();
        bookDAO.byIsbn.put("ISBN-6", sampleBook("ISBN-6"));
        FakeBookInfoDAO bookInfoDAO = new FakeBookInfoDAO();
        List<BookInfoDTO> copies = new ArrayList<>();
        copies.add(new BookInfoDTO(11, "Good", BookInfoDTO.STATUS_AVAILABLE, "ISBN-6"));
        copies.add(new BookInfoDTO(12, "New", BookInfoDTO.STATUS_AVAILABLE, "ISBN-6"));
        bookInfoDAO.byIsbn.put("ISBN-6", copies);
        ConnectionTracker tracker = new ConnectionTracker();
        TestableBookService service = createService(bookDAO, bookInfoDAO, new FakeBookUserDAO(), tracker);

        service.deleteBook("ISBN-6");

        assertEquals(List.of(11, 12), bookInfoDAO.transactionDeletedIds);
        assertEquals("ISBN-6", bookDAO.transactionDeletedIsbn);
        assertTrue(tracker.committed);
    }

    private TestableBookService createService(
        FakeBookDAO bookDAO,
        FakeBookInfoDAO bookInfoDAO,
        FakeBookUserDAO bookUserDAO,
        ConnectionTracker tracker
    ) {
        return new TestableBookService(
            bookDAO,
            new FakeAuthorDAO(),
            new FakePublisherDAO(),
            bookInfoDAO,
            bookUserDAO,
            tracker
        );
    }

    private BookDTO sampleBook(String isbn) {
        return new BookDTO(isbn, "Sample", Date.valueOf("2026-04-15"), "desc", 1, "Author", 1, "Publisher");
    }

    private static final class TestableBookService extends BookService {
        private final ConnectionTracker tracker;

        private TestableBookService(
            BookDAO bookDAO,
            AuthorDAO authorDAO,
            PublisherDAO publisherDAO,
            BookInfoDAO bookInfoDAO,
            BookUserDAO bookUserDAO,
            ConnectionTracker tracker
        ) {
            super(bookDAO, authorDAO, publisherDAO, bookInfoDAO, bookUserDAO);
            this.tracker = tracker;
        }

        @Override
        protected Connection openConnection() throws SQLException, IOException {
            tracker.connectionRequested = true;
            return tracker.createConnection();
        }
    }

    private static final class ConnectionTracker {
        private boolean connectionRequested;
        private boolean committed;
        private boolean rolledBack;
        private boolean closed;
        private final List<Boolean> autoCommitCalls = new ArrayList<>();

        private Connection createConnection() {
            return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "setAutoCommit" -> {
                            autoCommitCalls.add((Boolean) args[0]);
                            return null;
                        }
                        case "commit" -> {
                            committed = true;
                            return null;
                        }
                        case "rollback" -> {
                            rolledBack = true;
                            return null;
                        }
                        case "close" -> {
                            closed = true;
                            return null;
                        }
                        case "isClosed" -> {
                            return closed;
                        }
                        default -> {
                            Class<?> returnType = method.getReturnType();
                            if (returnType.equals(boolean.class)) {
                                return false;
                            }
                            if (returnType.equals(int.class)) {
                                return 0;
                            }
                            return null;
                        }
                    }
                }
            );
        }
    }

    private static final class FakeBookDAO implements BookDAO {
        private final Map<String, BookDTO> byIsbn = new HashMap<>();
        private BookDTO transactionAddedBook;
        private String transactionDeletedIsbn;

        @Override
        public void addBook(BookDTO bookDTO) {
            byIsbn.put(bookDTO.getIsbn(), bookDTO);
        }

        @Override
        public void addBook(Connection con, BookDTO bookDTO) {
            transactionAddedBook = bookDTO;
            byIsbn.put(bookDTO.getIsbn(), bookDTO);
        }

        @Override
        public void deleteBook(Connection con, String isbn) {
            transactionDeletedIsbn = isbn;
            byIsbn.remove(isbn);
        }

        @Override
        public void updateBook(BookDTO bookDTO) {
            byIsbn.put(bookDTO.getIsbn(), bookDTO);
        }

        @Override
        public void deleteBook(String isbn) {
            byIsbn.remove(isbn);
        }

        @Override
        public List<BookDTO> findAllBooks() {
            return new ArrayList<>(byIsbn.values());
        }

        @Override
        public BookDTO findByISBN(String isbn) {
            return byIsbn.get(isbn);
        }

        @Override
        public List<BookDTO> searchBooksByPage(String keyword, int offset, int limit) {
            return new ArrayList<>(byIsbn.values());
        }

        @Override
        public int countBooksBySearch(String keyword) {
            return byIsbn.size();
        }
    }

    private static final class FakeBookInfoDAO implements BookInfoDAO {
        private final Map<Integer, BookInfoDTO> byId = new HashMap<>();
        private final Map<String, List<BookInfoDTO>> byIsbn = new HashMap<>();
        private BookInfoDTO transactionAddedCopy;
        private final List<Integer> transactionDeletedIds = new ArrayList<>();
        private boolean failOnTransactionalAdd;

        @Override
        public void addBookInfo(BookInfoDTO bookInfoDTO) {
            byId.put(bookInfoDTO.getBookID(), bookInfoDTO);
        }

        @Override
        public void addBookInfo(Connection con, BookInfoDTO bookInfoDTO) throws SQLException {
            if (failOnTransactionalAdd) {
                throw new SQLException("copy insert failed");
            }
            transactionAddedCopy = bookInfoDTO;
            byIsbn.computeIfAbsent(bookInfoDTO.getBookISBN(), key -> new ArrayList<>()).add(bookInfoDTO);
        }

        @Override
        public void updateBookInfo(Connection con, BookInfoDTO bookInfoDTO) {
            byId.put(bookInfoDTO.getBookID(), bookInfoDTO);
        }

        @Override
        public void updateBookInfo(BookInfoDTO bookInfoDTO) {
            byId.put(bookInfoDTO.getBookID(), bookInfoDTO);
        }

        @Override
        public void deleteBookInfo(Connection con, int bookID) {
            transactionDeletedIds.add(bookID);
            byId.remove(bookID);
        }

        @Override
        public void deleteBookInfo(int bookID) {
            byId.remove(bookID);
        }

        @Override
        public List<BookInfoDTO> getAllBookInfo() {
            return new ArrayList<>(byId.values());
        }

        @Override
        public BookInfoDTO getBookInfoByID(int bookID) {
            return byId.get(bookID);
        }

        @Override
        public List<BookInfoDTO> getBookInfoByISBN(String isbn) {
            return byIsbn.getOrDefault(isbn, List.of());
        }

        @Override
        public int countTotalCopies() {
            return byId.size();
        }

        @Override
        public int countCopiesByStatus(int status) {
            return (int) byId.values().stream().filter(copy -> copy.getStatus() == status).count();
        }
    }

    private static final class FakeBookUserDAO implements BookUserDAO {
        private final Map<Integer, BookUserDTO> byId = new HashMap<>();
        private BookUserDTO transactionAddedRecord;
        private BookUserDTO transactionUpdatedRecord;

        @Override
        public void addBookUser(BookUserDTO bookUserDTO) {
            byId.put(bookUserDTO.getBookUserID(), bookUserDTO);
        }

        @Override
        public void addBookUser(Connection con, BookUserDTO bookUserDTO) {
            transactionAddedRecord = bookUserDTO;
            byId.put(bookUserDTO.getBookUserID(), bookUserDTO);
        }

        @Override
        public void updateBookUser(BookUserDTO bookUserDTO) {
            byId.put(bookUserDTO.getBookUserID(), bookUserDTO);
        }

        @Override
        public void updateBookUser(Connection con, BookUserDTO bookUserDTO) {
            transactionUpdatedRecord = bookUserDTO;
            byId.put(bookUserDTO.getBookUserID(), bookUserDTO);
        }

        @Override
        public void deleteBookUser(int bookUserID) {
            byId.remove(bookUserID);
        }

        @Override
        public List<BookUserDTO> getAllBookUser() {
            return new ArrayList<>(byId.values());
        }

        @Override
        public BookUserDTO getBookUserByID(int bookUserID) {
            return byId.get(bookUserID);
        }

        @Override
        public int countOverdueRecords() {
            return 0;
        }

        @Override
        public List<BookUserDTO> getBookUserByUsername(String username) {
            return byId.values().stream().filter(record -> username.equals(record.getUsername())).toList();
        }
    }

    private static final class FakeAuthorDAO implements AuthorDAO {
        @Override
        public List<AuthorDTO> findAllAuthors() {
            return List.of();
        }

        @Override
        public AuthorDTO findAuthorByID(int authorID) {
            return null;
        }

        @Override
        public void addAuthor(AuthorDTO author) {
        }

        @Override
        public void updateAuthor(AuthorDTO author) {
        }

        @Override
        public void deleteAuthor(int authorID) {
        }

        @Override
        public List<AuthorDTO> searchAuthors(String keyword) {
            return List.of();
        }
    }

    private static final class FakePublisherDAO implements PublisherDAO {
        @Override
        public List<PublisherDTO> findAllPublishers() {
            return List.of();
        }

        @Override
        public PublisherDTO findPublisherByID(int publisherID) {
            return null;
        }

        @Override
        public void addPublisher(PublisherDTO publisher) {
        }

        @Override
        public void updatePublisher(PublisherDTO publisher) {
        }

        @Override
        public void deletePublisher(int publisherID) {
        }

        @Override
        public List<PublisherDTO> searchPublishers(String keyword) {
            return List.of();
        }
    }
}
