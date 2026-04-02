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
    /** Query for all books with robust grouping and null-handling for aggregates. */
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
    /** Query for one book by ISBN with robust grouping and null-handling. */
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
    private static final String INSERT_BOOK = """
            INSERT INTO Book (ISBN, title, date_acquired, description, Author_authorID, Publisher_publisherID)
            VALUES (?, ?, ?, ?, ?, ?)""";
    private static final String UPDATE_BOOK = """
            UPDATE Book SET title = ?, date_acquired = ?, description = ?, Author_authorID = ?, Publisher_publisherID = ?
            WHERE ISBN = ?""";
    private static final String DELETE_BOOK = "DELETE FROM Book WHERE ISBN = ?";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws SQLException, IOException {
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Inserts a new book.
     */
    @Override
    public void addBook(BookDTO bookDTO) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_BOOK)){
            // Bind DTO fields to SQL parameters.
            ps.setString(1, bookDTO.getIsbn());
            ps.setString(2, bookDTO.getTitle());
            ps.setDate(3, bookDTO.getDateAcquired());
            ps.setString(4, bookDTO.getDescription());
            ps.setInt(5, bookDTO.getAuthorID());
            ps.setInt(6, bookDTO.getPublisherID());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("addBook() failed: " + e.getMessage(), e);
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
