package DAO.book;

import DAO.DataSource;
import DTO.book.BookInfoDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link BookInfoDAO}.
 * <p>
 * Handles physical copy records (one row per copy) rather than logical titles.
 */
public class BookInfoDAOImpl implements BookInfoDAO {
    /** Query to retrieve all physical book copies, ordered by newest first. */
    private static final String QUERY_BOOKINFO = "SELECT * FROM Book_Info ORDER BY BookID DESC";
    /** Single-copy lookup query by primary key. */
    private static final String QUERY_BOOKINFO_ID = "SELECT * FROM Book_Info WHERE bookID = ?";
    /** Query to retrieve all physical copies associated with a specific book ISBN. */
    private static final String QUERY_BY_ISBN = "SELECT * FROM Book_Info WHERE book_ISBN = ? ORDER BY bookID DESC";
    /** Insert query for a new physical book copy. */
    private static final String INSERT_BOOKINFO = "INSERT INTO Book_Info (book_condition, status, Book_ISBN) VALUES (?, ?, ?)";
    /** Update query for the condition and status of an existing book copy. */
    private static final String UPDATE_BOOKINFO = "UPDATE Book_Info SET book_condition = ?, status = ? WHERE bookID = ?";
    /** Delete query for a book copy by primary key. */
    private static final String DELETE_BOOKINFO = "DELETE FROM Book_Info WHERE bookID = ?";
    /** Aggregate query to count the total number of physical copies in the library. */
    private static final String COUNT_COPY = "SELECT COUNT(*) FROM book_info";
    /** Aggregate query to count physical copies filtered by their current status. */
    private static final String COUNT_COPY_STATUS = "SELECT COUNT(*) FROM Book_Info WHERE status = ?";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws IOException, SQLException {
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Inserts a new book copy.
     */
    @Override
    public void addBookInfo(BookInfoDTO bookInfoDTO) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_BOOKINFO)){
            // Bind DTO fields to SQL parameters.
            ps.setString(1, bookInfoDTO.getCondition());
            ps.setInt(2, bookInfoDTO.getStatus());
            ps.setString(3, bookInfoDTO.getBookISBN());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("addBookInfo() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a book copy by id.
     */
    @Override
    public void updateBookInfo(BookInfoDTO bookInfoDTO) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(UPDATE_BOOKINFO)){
            // Update copy fields by bookID.
            ps.setString(1, bookInfoDTO.getCondition());
            ps.setInt(2, bookInfoDTO.getStatus());
            ps.setInt(3, bookInfoDTO.getBookID());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("updateBookInfo() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a book copy by id.
     */
    @Override
    public void deleteBookInfo(int bookID) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(DELETE_BOOKINFO)){
            // Delete by bookID (primary key).
            ps.setInt(1, bookID);
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("deleteBookInfo() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all book copies.
     */
    @Override
    public List<BookInfoDTO> getAllBookInfo() {
        List <BookInfoDTO> bookInfos = new ArrayList<>();
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_BOOKINFO);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                // Map each row to a BookInfoDTO.
                bookInfos.add(mapBookInfo(rs));
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("getAllBookInfo() failed: " + e.getMessage(), e);
        }
        return bookInfos;
    }

    /**
     * Returns a book copy by ID.
     */
    @Override
    public BookInfoDTO getBookInfoByID(int bookID) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_BOOKINFO_ID)) {
            // Lookup a single book copy by ID.
            ps.setInt(1, bookID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBookInfo(rs);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("getBookInfoByID() failed: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Returns book info by ISBN.
     */
    @Override
    public List<BookInfoDTO> getBookInfoByISBN(String isbn) {
        List<BookInfoDTO> copies = new ArrayList<>();
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_BY_ISBN)){
            //Lookup book info by ISBN.
            ps.setString(1, isbn);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    copies.add(mapBookInfo(rs));
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("getBookInfoByISBN() failed: " + e.getMessage(), e);
        }
        return copies;
    }

    /**
     * Counts all physical copies.
     */
    @Override
    public int countTotalCopies() {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(COUNT_COPY);
        ResultSet rs = ps.executeQuery()){
            if(rs.next()) return rs.getInt(1);
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Counts physical copies by status code.
     */
    @Override
    public int countCopiesByStatus(int status) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(COUNT_COPY_STATUS)){
            ps.setInt(1, status);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return rs.getInt(1);
            }
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Maps one SQL row into {@link BookInfoDTO}.
     */
    private BookInfoDTO mapBookInfo(ResultSet rs) throws SQLException {
        return new BookInfoDTO(
                rs.getInt("bookID"),
                rs.getString("book_condition"),
                rs.getInt("status"),
                rs.getString("Book_ISBN")
        );
    }
}
