package DAO.book;

import DAO.DataSource;
import DTO.book.BookUserDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link BookUserDAO}.
 * <p>
 * Handles borrow/return transaction rows linking user and physical book copy.
 */
public class BookUserDAOImpl implements BookUserDAO {
    private static final String QUERY_BOOKUSER = "SELECT * FROM Book_User ORDER BY book_userID DESC";
    private static final String QUERY_BY_ID = "SELECT * FROM Book_User WHERE book_userID = ?";
    private static final String QUERY_BY_USERNAME = "SELECT * FROM Book_User WHERE User_username = ? ORDER BY start_date DESC";
    private static final String INSERT_BOOKUSER = "INSERT INTO Book_User (start_date, return_date, late_fee, User_username, Book_Info_bookID) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOKUSER = "UPDATE Book_User SET start_date = ?, return_date = ?, late_fee = ?, User_username = ?, Book_Info_bookID = ? WHERE book_userID = ?";
    private static final String DELETE_BOOKUSER = "DELETE FROM Book_User WHERE book_userID = ?";
    private static final String COUNT_RECORDS = "SELECT COUNT(*) FROM Book_User WHERE return_date IS NULL AND CURDATE() > DATE_ADD(start_date, INTERVAL 14 DAY)";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws IOException, SQLException {
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Inserts a new borrow record.
     */
    @Override
    public void addBookUser(BookUserDTO bookUserDTO) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_BOOKUSER)){
            // Bind DTO fields to SQL parameters.
            ps.setDate(1, bookUserDTO.getStartDate());
            ps.setDate(2, bookUserDTO.getReturnDate());
            ps.setBigDecimal(3, bookUserDTO.getLateFee());
            ps.setString(4, bookUserDTO.getUsername());
            ps.setInt(5, bookUserDTO.getBookID());
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("addBookUser() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a borrow record by id.
     */
    @Override
    public void updateBookUser(BookUserDTO bookUserDTO) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(UPDATE_BOOKUSER)){
            // Update record fields by book_userID.
            ps.setDate(1, bookUserDTO.getStartDate());
            ps.setDate(2, bookUserDTO.getReturnDate());
            ps.setBigDecimal(3, bookUserDTO.getLateFee());
            ps.setString(4, bookUserDTO.getUsername());
            ps.setInt(5, bookUserDTO.getBookID());
            ps.setInt(6, bookUserDTO.getBookUserID());
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("updateBookUser() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a borrow record by id.
     */
    @Override
    public void deleteBookUser(int bookUserID) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_BOOKUSER)){
            // Delete by book_userID (primary key).
            ps.setInt(1, bookUserID);
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("deleteBookUser() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all borrow records.
     */
    @Override
    public List<BookUserDTO> getAllBookUser() {
        List<BookUserDTO> bookUsers = new ArrayList<>();
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_BOOKUSER);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                // Map each row to a BookUserDTO.
                bookUsers.add(mapBookUser(rs));
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("getAllBookUser() failed: " + e.getMessage(), e);
        }
        return bookUsers;
    }

    /**
     * Returns a borrow record by id.
     */
    @Override
    public BookUserDTO getBookUserByID(int bookUserID) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_BY_ID)){
            // Lookup a single record by id.
            ps.setInt(1, bookUserID);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapBookUser(rs);
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("getBookUser() failed: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Counts borrow records that are overdue and not returned yet.
     */
    @Override
    public int countOverdueRecords() {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(COUNT_RECORDS);
        ResultSet rs = ps.executeQuery()){
            if(rs.next()) return rs.getInt(1);
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns all borrow records for a specific user.
     */
    @Override
    public List<BookUserDTO> getBookUserByUsername(String username) {
        List<BookUserDTO> userRecords = new ArrayList<>();
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_BY_USERNAME)){

            // Set the username parameter
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    userRecords.add(mapBookUser(rs));
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("getBookUserByUsername() failed: " + e.getMessage(), e);
        }
        return userRecords;
    }

    /**
     * Maps one SQL row into {@link BookUserDTO}.
     */
    private BookUserDTO mapBookUser(ResultSet rs) throws SQLException {
        return new BookUserDTO(
                rs.getInt("book_userID"),
                rs.getDate("start_date"),
                rs.getDate("return_date"),
                rs.getBigDecimal("late_fee"),
                rs.getString("User_username"),
                rs.getInt("Book_Info_bookID")
        );
    }
}
