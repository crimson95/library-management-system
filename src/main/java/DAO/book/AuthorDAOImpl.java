package DAO.book;

import DAO.DataSource;
import DTO.book.AuthorDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link AuthorDAO}.
 */
public class AuthorDAOImpl implements AuthorDAO{
    /** Query for loading all author records used by dropdown/list screens. */
    private static final String QUERY_ALL_AUTHORS = "SELECT * FROM Author ORDER BY first_name ASC";
    private static final String QUERY_AUTHOR_BY_ID = "SELECT * FROM Author WHERE authorID = ?";
    private static final String INSERT_AUTHOR = "INSERT INTO Author (first_name, last_name) VALUES (?, ?)";
    private static final String UPDATE_AUTHOR = "UPDATE Author SET first_name = ?, last_name = ? WHERE authorID = ?";
    private static final String DELETE_AUTHOR = "DELETE FROM Author WHERE authorID = ?";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws SQLException, IOException{
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Fetches all authors and maps each row into {@link AuthorDTO}.
     */
    @Override
    public List<AuthorDTO> findAllAuthors() {
        List<AuthorDTO> authors = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(QUERY_ALL_AUTHORS);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                // Row-to-DTO mapping for one author record.
                AuthorDTO author = new AuthorDTO(rs.getInt("authorID"), rs.getString("first_name"),rs.getString("last_name"));
                authors.add(author);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("findAllAuthors() failed: " + e.getMessage(), e);
        }
        return authors;
    }

    /**
     * Fetches one author by primary key.
     */
    @Override
    public AuthorDTO findAuthorByID(int  authorID) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_AUTHOR_BY_ID)){
            // Bind requested author ID.
            ps.setInt(1, authorID);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new AuthorDTO(
                            rs.getInt("authorID"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    );
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("findAuthorByID() failed: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Inserts a new author row.
     */
    @Override
    public void addAuthor(AuthorDTO author) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(INSERT_AUTHOR)){
            // Persist first/last name values from DTO.
            ps.setString(1, author.getFirst_name());
            ps.setString(2, author.getLast_name());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("addAuthor() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing author row by ID.
     */
    @Override
    public void updateAuthor(AuthorDTO author) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(UPDATE_AUTHOR)){
            // Update name columns for target author ID.
            ps.setString(1, author.getFirst_name());
            ps.setString(2, author.getLast_name());
            ps.setInt(3, author.getAuthorID());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("updateAuthor() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an author row by ID.
     */
    @Override
    public void deleteAuthor(int authorID) {
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(DELETE_AUTHOR)){
            // Delete by authorID (primary key).
            ps.setInt(1, authorID);
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("deleteAuthor() failed: " + e.getMessage(), e);
        }
    }
}
