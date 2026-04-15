package DAO.book;

import DAO.DataSource;
import DTO.book.AuthorDTO;
import DTO.book.BookDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link BookDAO}.
 * <p>
 * Uses JOIN queries to enrich book rows with author/publisher display names
 * required by admin list screens.
 */
public class BookDAOImpl implements BookDAO{
    /** Query to retrieve all books with their author and publisher names, and aggregated copy counts. */
    private static final String QUERY_BOOK = """
            SELECT b.ISBN, b.title, b.date_acquired, b.description, b.Author_authorID AS authorID,
                   b.Publisher_publisherID AS publisherID, a.first_name AS author_first_name,
                   a.last_name AS author_last_name, p.publisher_name AS publisher_name,
                   COUNT(bi.bookID) AS total_copies,
                   COALESCE(SUM(CASE WHEN bi.status = 1 THEN 1 ELSE 0 END), 0) AS available_copies
            FROM Book as b JOIN Author a ON b.Author_authorID = a.authorID
            JOIN Publisher p ON b.Publisher_publisherID = p.publisherID
            LEFT JOIN Book_info bi ON b.ISBN = bi.Book_ISBN
            GROUP BY b.ISBN, b.title, b.date_acquired, b.description, b.Author_authorID,
                     b.Publisher_publisherID, a.first_name, a.last_name, p.publisher_name
            ORDER BY b.ISBN DESC""";
    /** Query to retrieve a single book by its ISBN, including aggregated copy counts. */
    private static final String QUERY_ISBN = """
            SELECT b.ISBN, b.title, b.date_acquired, b.description, b.Author_authorID AS authorID,
                   b.Publisher_publisherID AS publisherID, a.first_name AS author_first_name,
                   a.last_name AS author_last_name, p.publisher_name AS publisher_name,
                   COUNT(bi.bookID) AS total_copies,
                   COALESCE(SUM(CASE WHEN bi.status = 1 THEN 1 ELSE 0 END), 0) AS available_copies
            FROM Book AS b JOIN Author a ON b.Author_authorID = a.authorID
            JOIN Publisher p ON b.Publisher_publisherID = p.publisherID
            LEFT JOIN Book_info bi ON b.ISBN = bi.Book_ISBN
            WHERE b.ISBN = ?
            GROUP BY b.ISBN, b.title, b.date_acquired, b.description, b.Author_authorID,
                     b.Publisher_publisherID, a.first_name, a.last_name, p.publisher_name""";
    /** Insert query to add a new book record into the catalog. */
    private static final String INSERT_BOOK = """
            INSERT INTO Book (ISBN, title, date_acquired, description, Author_authorID, Publisher_publisherID)
            VALUES (?, ?, ?, ?, ?, ?)""";
    /** Update query to modify an existing book's details by its ISBN. */
    private static final String UPDATE_BOOK = """
            UPDATE Book SET title = ?, date_acquired = ?, description = ?, Author_authorID = ?, Publisher_publisherID = ?
            WHERE ISBN = ?""";
    /** Delete query to remove a book from the catalog by its ISBN. */
    private static final String DELETE_BOOK = "DELETE FROM Book WHERE ISBN = ?";
    /** Query to retrieve a paginated list of books based on a keyword search across title, author, and ISBN. */
    private static final String SEARCH_BOOKS_PAGED = """
           SELECT b.ISBN, b.title, b.date_acquired, b.description, b.Author_authorID AS authorID,
           b.Publisher_publisherID AS publisherID, a.first_name AS author_first_name,
           a.last_name AS author_last_name, p.publisher_name AS publisher_name,
           COUNT(bi.bookID) AS total_copies,
           COALESCE(SUM(CASE WHEN bi.status = 1 THEN 1 ELSE 0 END), 0) AS available_copies
           FROM Book AS b JOIN Author a ON b.Author_authorID = a.authorID
                JOIN Publisher p ON b.Publisher_publisherID = p.publisherID
                LEFT JOIN Book_info bi ON b.ISBN = bi.Book_ISBN
           WHERE LOWER(b.title) LIKE ? OR LOWER(a.first_name) LIKE ? OR LOWER(a.last_name) LIKE ? OR LOWER(b.ISBN) LIKE ?
           GROUP BY b.ISBN, b.title, b.date_acquired, b.description, b.Author_authorID, b.Publisher_publisherID,
                    a.first_name, a.last_name, p.publisher_name
           ORDER BY b.ISBN DESC LIMIT ? OFFSET ?""";
    /** Query to count the total number of distinct books matching a keyword search for pagination logic. */
    private static final String COUNT_SEARCH_BOOKS = """
            SELECT COUNT(DISTINCT b.ISBN) FROM Book AS b JOIN Author a ON b.Author_authorID = a.authorID
            WHERE LOWER(b.title) LIKE ? OR LOWER(a.first_name) LIKE ? OR LOWER(a.last_name) LIKE ? OR LOWER(b.ISBN) LIKE ?""";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws SQLException, IOException {
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Adds a new book record to the database using an existing transaction connection.
     * <p>
     * This method is part of a larger transaction managed by the Service layer.
     * It executes the INSERT statement for the book catalog but does NOT commit
     * or close the provided connection.
     * </p>
     *
     * @param con The active database connection provided by the calling service.
     * @param bookDTO The BookDTO containing the details of the book to insert.
     * @throws SQLException If a database access error occurs or the SQL statement fails.
     */
    @Override
    public void addBook(Connection con, BookDTO bookDTO) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_BOOK)) {
            // Bind DTO fields to SQL parameters.
            ps.setString(1, bookDTO.getIsbn());
            ps.setString(2, bookDTO.getTitle());
            ps.setDate(3, bookDTO.getDateAcquired());
            ps.setString(4, bookDTO.getDescription());
            ps.setInt(5, bookDTO.getAuthorID());
            ps.setInt(6, bookDTO.getPublisherID());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a book by ISBN using an existing transaction connection.
     */
    @Override
    public void deleteBook(Connection con, String isbn) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_BOOK)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        }
    }

    /**
     * Updates a book by ISBN.
     */
    @Override
    public void updateBook(BookDTO bookDTO) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(UPDATE_BOOK)){
            // Update book fields by ISBN.
            ps.setString(1, bookDTO.getTitle());
            ps.setDate(2, bookDTO.getDateAcquired());
            ps.setString(3, bookDTO.getDescription());
            ps.setInt(4, bookDTO.getAuthorID());
            ps.setInt(5, bookDTO.getPublisherID());
            ps.setString(6, bookDTO.getIsbn());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("updateBook() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a book by ISBN.
     */
    @Override
    public void deleteBook(String isbn) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_BOOK)){
            // Delete by ISBN (primary key).
            ps.setString(1, isbn);
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("deleteBook() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all books.
     */
    @Override
    public List<BookDTO> findAllBooks() {
        List<BookDTO> books = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(QUERY_BOOK);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                // Map each row to a BookDTO.
                books.add(mapBook(rs));
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("findAllBooks() failed: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * Finds a book by ISBN.
     */
    @Override
    public BookDTO findByISBN(String isbn) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(QUERY_ISBN)){
            // Parameterized lookup by unique ISBN key.
            ps.setString(1, isbn);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapBook(rs);
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("findByISBN() failed: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves a paginated list of books matching the search keyword.
     * <p>
     * Applies a case-insensitive LIKE filter across title, author names, and ISBN.
     * Limits the result set based on the provided offset and limit for pagination.
     *
     * @param keyword the search keyword to filter by (can be null or empty)
     * @param offset  the starting row index for the database query
     * @param limit   the maximum number of records to retrieve
     * @return a list of {@link BookDTO} objects representing the requested page
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public List<BookDTO>searchBooksByPage(String keyword, int offset, int limit){
        List<BookDTO> books = new ArrayList<>();
        // Formal keyword for SQL LIKE operator (e.g. "%java%")
        String searchPattern = "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(SEARCH_BOOKS_PAGED)){
            //Bind the search pattern to the 4 WHERE conditions
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setInt(5, limit);
            ps.setInt(6, offset);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    books.add(mapBook(rs));
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("searchBooksByPage() failed: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * Counts the total number of distinct books matching the search keyword.
     * <p>
     * This is used primarily to calculate the total number of available pages
     * for pagination logic in the service layer.
     *
     * @param keyword the search keyword to filter by (can be null or empty)
     * @return the total count of matching books, or 0 if an error occurs
     */
    @Override
    public int countBooksBySearch(String keyword){
        String searchPattern = "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";

        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(COUNT_SEARCH_BOOKS)){
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Maps one JOIN result row into {@link BookDTO}.
     *
     * @param rs current row cursor
     * @return mapped book DTO with both IDs and display names
     * @throws SQLException when required columns cannot be read
     */
    private BookDTO mapBook(ResultSet rs) throws SQLException {
        // Keep author first/last in AuthorDTO and expose full name to BookDTO.
        String first = rs.getString("author_first_name");
        String last = rs.getString("author_last_name");

        AuthorDTO authorDTO = new AuthorDTO(rs.getInt("authorID"), first, last);

        // Publisher name comes from JOIN alias.
        String authorName = authorDTO.getFullName();
        String publisherName = rs.getString("publisher_name");

        BookDTO book = new BookDTO(
            rs.getString("ISBN"),
            rs.getString("title"),
            rs.getDate("date_acquired"),
            rs.getString("description"),
            rs.getInt("authorID"),
            authorName,
            rs.getInt("publisherID"),
            publisherName
        );
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));

        return book;
    }
}
