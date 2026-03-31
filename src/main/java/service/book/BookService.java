package service.book;

import DAO.book.*;
import DTO.book.*;
import service.BusinessValidationException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Business layer for book-related operations.
 * <p>
 * This layer validates input before delegating to DAO.
 * It also coordinates related lookup DAOs (author/publisher)
 * used by admin form pages.
 */
public class BookService {
    /** Data access dependency for book persistence/query operations. */
    private final BookDAO bookDAO;
    /** DAO used to load author options for add/edit pages. */
    private final AuthorDAO authorDAO;
    /** DAO used to load publisher options for add/edit pages. */
    private final PublisherDAO publisherDAO;
    /** DAO access dependency for book info presistence/query operations. */
    private final BookInfoDAO bookInfoDAO;
    /** DAO access dependency for book user presistence/query operations. */
    private final BookUserDAO bookUserDAO;

    /**
     * Creates service with default DAO implementation.
     */
    public BookService() {
        this.bookDAO = new BookDAOImpl();
        this.authorDAO = new AuthorDAOImpl();
        this.publisherDAO = new PublisherDAOImpl();
        this.bookInfoDAO = new BookInfoDAOImpl();
        this.bookUserDAO = new BookUserDAOImpl();
    }

    /**
     * Returns all books for admin list page.
     *
     * @return list of books
     */
    public List<BookDTO> findAllBooks() {
        return bookDAO.findAllBooks();
    }

    /**
     * Finds one book by ISBN after basic validation.
     *
     * @param isbn unique identifier of a book
     * @return matched book
     * @throws BusinessValidationException when ISBN is blank or record does not exist
     */
    public BookDTO findByISBN(String isbn) throws BusinessValidationException {
        if (isbn == null ||isbn.trim().isEmpty()) {
            throw new BusinessValidationException("ISBN cannot be empty");
        }
        // Use trimmed value to avoid lookup mismatch from leading/trailing spaces.
        BookDTO book = bookDAO.findByISBN(isbn.trim());

        if (book == null) {
            throw new BusinessValidationException("Book Not Found");
        }
        return book;
    }

    /**
     * Finds one book copy by bookID after basic validation.
     *
     * @param bookID unique identifier of a book copy
     * @return matched book
     * @throws BusinessValidationException when bookID is blank or record does not exist
     */
    public BookInfoDTO findCopyByID(int bookID) throws BusinessValidationException {
        if (bookID <= 0) {
            throw new BusinessValidationException("Invalid Book ID");
        }
        return bookInfoDAO.getBookInfoByID(bookID);
    }

    /**
     * Finds copies by ISBN after basic validation.
     *
     * @param isbn unique identifier of the same title of books
     * @return matched book
     * @throws BusinessValidationException when ISBN is blank or record does not exist
     */
    public List<BookInfoDTO> findCopiesByISBN(String isbn) throws BusinessValidationException {
        if (isbn == null ||isbn.trim().isEmpty()) {
            throw new BusinessValidationException("ISBN cannot be empty");
        }
        return bookInfoDAO.getBookInfoByISBN(isbn.trim());
    }

    /**
     * Creates a new book title record.
     *
     * @param bookDTO new book payload
     * @throws BusinessValidationException when required fields are invalid
     */
    public void addBook(BookDTO bookDTO) throws BusinessValidationException {
        // Validate required fields before insert.
        if (bookDTO == null) {
            throw new BusinessValidationException("Book cannot be null");
        }
        if (bookDTO.getIsbn() == null || bookDTO.getIsbn().trim().isEmpty()) {
            throw new BusinessValidationException("ISBN cannot be empty");
        }
        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            throw new BusinessValidationException("Title cannot be empty");
        }
        if (bookDTO.getDateAcquired() == null) {
            throw new BusinessValidationException("Date acquired is required");
        }
        if (bookDTO.getAuthorID() <= 0) {
            throw new BusinessValidationException("Author is required");
        }
        if (bookDTO.getPublisherID() <= 0) {
            throw new BusinessValidationException("Publisher is required");
        }
        // Persist after validation passes.
        bookDAO.addBook(bookDTO);
    }

    /**
     * Add a new book copy
     *
     * @param bookInfoDTO new book copy payload
     * @throws BusinessValidationException when required fields are invalid
     */
    public void addBookCopy(BookInfoDTO bookInfoDTO) throws BusinessValidationException {
        if (bookInfoDTO == null) {
            throw new BusinessValidationException("Book copy Info cannot be null");
        }
        // Invoke DAO to write to the database.
        bookInfoDAO.addBookInfo(bookInfoDTO);
    }

    /**
     * Update book copy
     *
     * @param bookInfoDTO book copy payload
     * @throws BusinessValidationException when required fields are invalid
     */
    public void updateBookCopy(BookInfoDTO bookInfoDTO) throws BusinessValidationException {
        if (bookInfoDTO == null) {
            throw new BusinessValidationException("Book copy Info cannot be null");
        }
        // Invoke DAO to write to the database.
        bookInfoDAO.updateBookInfo(bookInfoDTO);
    }

    /**
     * Updates an existing book record.
     *
     * @param bookDTO edited book payload
     * @throws BusinessValidationException when payload is invalid or record does not exist
     */
    public void updateBook(BookDTO bookDTO) throws BusinessValidationException {
        // Reuse common field validation logic.
        validateBook(bookDTO, false);
        // Ensure target record exists before issuing update.
        BookDTO existing = bookDAO.findByISBN(bookDTO.getIsbn().trim());
        if (existing == null) {
            throw new BusinessValidationException("Book Not Found");
        }
        // Persist updated fields.
        bookDAO.updateBook(bookDTO);
    }

    /**
     * Deletes a book by ISBN.
     *
     * @param isbn unique identifier of a book
     * @throws BusinessValidationException when ISBN is blank
     */
    public void deleteBook(String isbn) throws BusinessValidationException {
        if (isbn == null ||isbn.trim().isEmpty()) {
            throw new BusinessValidationException("ISBN cannot be empty");
        }
        // Delete by ISBN key.
        bookDAO.deleteBook(isbn.trim());
    }

    /**
     * Deletes a book copy by bookID.
     *
     * @param bookID unique identifier of a book copy
     * @throws BusinessValidationException when bookID is blank
     */
    public void deleteBookCopy(int bookID) throws BusinessValidationException {
        if(bookID <= 0) {
            throw new BusinessValidationException("Invalid Book ID");
        }
        // Delete by bookID key.
        bookInfoDAO.deleteBookInfo(bookID);
    }

    /**
     * Returns all authors for form dropdown options.
     *
     * @return author list sorted by DAO query
     */
    public List<AuthorDTO> findAllAuthors() {
        return authorDAO.findAllAuthors();
    }

    /**
     * Finds one author by ID after basic validation.
     *
     * @param authorID unique identifier of an author
     * @return matched author
     * @throws BusinessValidationException when author ID is invalid
     */
    public AuthorDTO findAuthorByID(int authorID) throws BusinessValidationException {
        if (authorID <= 0) {
            throw new BusinessValidationException("Invalid Author ID");
        }
        return authorDAO.findAuthorByID(authorID);
    }

    /**
     * Creates a new author record.
     *
     * @param author new author payload
     * @throws BusinessValidationException when required fields are invalid
     */
    public void addAuthor(AuthorDTO author) throws BusinessValidationException {
        if (author == null || isBlank(author.getFirst_name()) || isBlank(author.getLast_name())) {
            throw new BusinessValidationException("Author first name and last name cannot be empty");
        }
        authorDAO.addAuthor(author);
    }

    /**
     * Updates an existing author record.
     *
     * @param author author payload with existing ID
     * @throws BusinessValidationException when required fields are invalid
     */
    public void updateAuthor(AuthorDTO author) throws BusinessValidationException {
        if (author == null || isBlank(author.getFirst_name()) || isBlank(author.getLast_name())) {
            throw new BusinessValidationException("Author first name and last name cannot be empty");
        }
        authorDAO.updateAuthor(author);
    }

    /**
     * Deletes an author by ID.
     *
     * @param authorID unique identifier of an author
     * @throws BusinessValidationException when author ID is invalid
     */
    public void deleteAuthor(int authorID) throws BusinessValidationException {
        if (authorID <= 0) {
            throw new BusinessValidationException("Invalid Author ID");
        }
        authorDAO.deleteAuthor(authorID);
    }

    /**
     * Returns all publishers for form dropdown options.
     *
     * @return publisher list sorted by DAO query
     */
    public List<PublisherDTO> findAllPublishers() {
        return publisherDAO.findAllPublishers();
    }

    /**
     * Finds one publisher by ID after basic validation.
     *
     * @param publisherID unique identifier of a publisher
     * @return matched publisher
     * @throws BusinessValidationException when publisher ID is invalid
     */
    public PublisherDTO findPublisherByID(int publisherID) throws BusinessValidationException {
        if (publisherID <= 0) {
            throw new BusinessValidationException("Invalid Publisher ID");
        }
        return publisherDAO.findPublisherByID(publisherID);
    }

    /**
     * Creates a new publisher record.
     *
     * @param publisher new publisher payload
     * @throws BusinessValidationException when required fields are invalid
     */
    public void addPublisher(PublisherDTO publisher) throws BusinessValidationException {
        if (publisher == null || isBlank(publisher.getPublisherName())) {
            throw new BusinessValidationException("Publisher name cannot be empty");
        }
        publisherDAO.addPublisher(publisher);
    }

    /**
     * Updates an existing publisher record.
     *
     * @param publisher publisher payload with existing ID
     * @throws BusinessValidationException when required fields are invalid
     */
    public void updatePublisher(PublisherDTO publisher) throws BusinessValidationException {
        if (publisher == null || isBlank(publisher.getPublisherName())) {
            throw new BusinessValidationException("Publisher name cannot be empty");
        }
        publisherDAO.updatePublisher(publisher);
    }

    /**
     * Deletes a publisher by ID.
     *
     * @param publisherID unique identifier of a publisher
     * @throws BusinessValidationException when publisher ID is invalid
     */
    public void deletePublisher(int publisherID) throws BusinessValidationException {
        if (publisherID <= 0) {
            throw new BusinessValidationException("Invalid Publisher ID");
        }
        publisherDAO.deletePublisher(publisherID);
    }

    /**
     * Returns total number of physical book copies.
     */
    public int getTotalBooksCount() {
        return bookInfoDAO.countTotalCopies();
    }

    /**
     * Returns number of copies currently available to borrow.
     */
    public int getAvailableCopiesCount() {
        return bookInfoDAO.countCopiesByStatus(BookInfoDTO.STATUS_AVAILABLE);
    }

    /**
     * Returns number of copies currently marked as borrowed.
     */
    public int getBorrowedCopiesCount(){
        return bookInfoDAO.countCopiesByStatus(BookInfoDTO.STATUS_BORROWED);
    }

    /**
     * Returns number of overdue active borrow records.
     */
    public int getOverdueCopiesCount(){
        return bookUserDAO.countOverdueRecords();
    }

    /**
     * Shared validation used by add/update flows.
     *
     * @param bookDTO book payload
     * @param requireISBN true when create flow requires explicit ISBN check
     * @throws BusinessValidationException when field rules are violated
     */
    private void validateBook(BookDTO bookDTO, boolean requireISBN) throws BusinessValidationException {
        if (bookDTO == null) {
            throw new BusinessValidationException("Book cannot be null");
        }
        // ISBN is required for create flow.
        if (requireISBN && isBlank(bookDTO.getIsbn())) {
            throw new BusinessValidationException("ISBN is required");
        }
        // Basic field validation rules.
        if (isBlank(bookDTO.getTitle())){
            throw new BusinessValidationException("Title is required");
        }
        if (isBlank(bookDTO.getDescription())){
            throw new BusinessValidationException("Date acquired is required");
        }
    }

    /**
     * Null-safe blank checker used by validation rules.
     */
    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }

    /**
     * Processes a book borrowing transaction.
     *
     * @param username the username of the member borrowing the book
     * @param bookID   the ID of the physical book copy being borrowed
     * @throws BusinessValidationException if the copy is not found or not available
     */
    public void borrowBook(String username, int bookID) throws BusinessValidationException {
        // 1. Check if the physical copy exists and is currently available
        BookInfoDTO book = bookInfoDAO.getBookInfoByID(bookID);
        if (book == null) {
            throw new BusinessValidationException("Cannot find this book (Book ID: " + bookID + ")");
        }
        if (book.getStatus() != BookInfoDTO.STATUS_AVAILABLE) {
            throw new BusinessValidationException("This book is currently not available (Status code: " + book.getStatus() + ")");
        }

        // 2. Create a new borrowing record (Default borrowing period is 14 days)
        long millis = System.currentTimeMillis();
        java.sql.Date startDate = new java.sql.Date(millis);

        // Map to exact BookUserDTO constructor: (bookUserID, startDate, returnDate, lateFee, username, bookID)
        BookUserDTO record = new BookUserDTO(0, startDate, null, java.math.BigDecimal.ZERO, username, bookID);

        // 3. Action A: Insert the borrowing record into the database
        bookUserDAO.addBookUser(record);

        // 4. Action B: Update the physical book copy status to "Borrowed" (0)
        book.setStatus(BookInfoDTO.STATUS_BORROWED);
        bookInfoDAO.updateBookInfo(book);
    }

    /**
     * Processes a book return transaction and calculates potential late fees.
     *
     * @param bookUserID the primary key ID of the borrowing record
     * @throws BusinessValidationException if the record is not found or already returned
     */
    public void returnBook(int bookUserID) throws BusinessValidationException {
        // 1. Find the existing borrowing record
        BookUserDTO record = bookUserDAO.getBookUserByID(bookUserID);
        if(record == null) {
            throw new BusinessValidationException("Cannot find the borrowing record (Record ID: " + bookUserID + ")");
        }
        if(record.getReturnDate() != null) {
            throw new BusinessValidationException("This book has already been returned");
        }

        // 2. Set the actual return date to today using java.time.LocalDate
        java.time.LocalDate today = java.time.LocalDate.now();
        record.setReturnDate(java.sql.Date.valueOf(today));

        // 3. Late Fee Calculation Logic
        // Convert SQL Date to LocalDate for easier math operations
        java.time.LocalDate startDate = record.getStartDate().toLocalDate();

        // Define library rules: 14 days borrowing period, $1.50 per overdue day
        java.time.LocalDate dueDate = startDate.plusDays(14);
        long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);

        java.math.BigDecimal lateFee = java.math.BigDecimal.ZERO;
        if(overdueDays > 0) {
            java.math.BigDecimal dailyRate = new java.math.BigDecimal("1.50");
            lateFee = new java.math.BigDecimal(overdueDays).multiply(dailyRate);
        }
        record.setLateFee(lateFee);

        // 4. Update the borrowing record in the database
        bookUserDAO.updateBookUser(record);

        // 5. Find the physical book copy and update its status back to "Available" (1)
        BookInfoDTO book = bookInfoDAO.getBookInfoByID(record.getBookID());
        if(book != null){
            book.setStatus(BookInfoDTO.STATUS_AVAILABLE);
            bookInfoDAO.updateBookInfo(book);
        }
    }

    /**
     * Retrieves all borrow records for the circulation dashboard.
     *
     * @return a list of all BookUserDTO records
     */
    public List<BookUserDTO> getAllBorrowRecords(){
        return bookUserDAO.getAllBookUser();
    }
}
