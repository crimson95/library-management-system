package DAO.book;

import DAO.DataSource;
import DTO.book.PublisherDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link PublisherDAO}.
 */
public class PublisherDAOImpl implements PublisherDAO {
    /** Query for loading publisher options in alphabetical order. */
    private static final String QUERY_ALL_PUBLISHERS = "SELECT * FROM Publisher ORDER BY publisherID ASC";
    /** Single-publisher lookup query by primary key. */
    private static final String QUERY_PUBLISHER_BY_ID = "SELECT * FROM Publisher WHERE publisherID = ?";
    /** Insert query for a new publisher record. */
    private static final String INSERT_PUBLISHER = "INSERT INTO Publisher (publisher_name) VALUES (?)";
    /** Update query for an existing publisher record. */
    private static final String UPDATE_PUBLISHER = "UPDATE Publisher SET publisher_name=? WHERE publisherID =?";
    /** Delete query for a publisher record by primary key. */
    private static final String DELETE_PUBLISHER = "DELETE FROM Publisher WHERE publisherID =?";
    /** Query for searching publishers by name. */
    private static final String SEARCH_PUBLISHERS = "SELECT * FROM Publisher WHERE LOWER(publisher_name) LIKE ? ORDER BY publisherID ASC";

    /**
     * Gets a JDBC connection from shared data source.
     */
    private Connection getConnection() throws SQLException, IOException {
        return DataSource.INSTANCE.getConnection();
    }

    /**
     * Fetches all publishers and maps each row into {@link PublisherDTO}.
     */
    @Override
    public List<PublisherDTO> findAllPublishers(){
        List<PublisherDTO> publishers = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(QUERY_ALL_PUBLISHERS);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                // Row-to-DTO mapping for one publisher record.
                PublisherDTO publisher = new PublisherDTO(rs.getInt("publisherID"), rs.getString("publisher_name"));
                publishers.add(publisher);
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("findAllPublishers() failed: " + e.getMessage(), e);
        }
        return publishers;
    }

    /**
     * Fetches one publisher by primary key.
     */
    @Override
    public PublisherDTO findPublisherByID(int publisherID){
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(QUERY_PUBLISHER_BY_ID)){
            // Bind requested publisher ID.
            ps.setInt(1, publisherID);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new PublisherDTO(
                            rs.getInt("publisherID"),
                            rs.getString("publisher_name")
                    );
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("findPublisherByID() failed: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Inserts a new publisher row.
     */
    @Override
    public void addPublisher(PublisherDTO publisher){
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(INSERT_PUBLISHER)){
            // Persist publisher name values from DTO.
            ps.setString(1, publisher.getPublisherName());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("addPublisher() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing publisher row by ID.
     */
    @Override
    public void updatePublisher(PublisherDTO publisher){
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(UPDATE_PUBLISHER)){
            // Update name columns for target publisher ID.
            ps.setString(1, publisher.getPublisherName());
            ps.setInt(2, publisher.getPublisherID());
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("updatePublisher() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a publisher row by ID.
     */
    @Override
    public void deletePublisher(int publisherID){
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(DELETE_PUBLISHER)){
            // Delete by publisherID (primary key).
            ps.setInt(1, publisherID);
            ps.executeUpdate();
        }catch (SQLException | IOException e){
            throw new RuntimeException("deletePublisher() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Searches the database for publishers matching a specific keyword in their name.
     *
     * @param keyword the search string to filter by
     * @return a list of {@link PublisherDTO} objects matching the search criteria
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public List<PublisherDTO> searchPublishers(String keyword){
        List<PublisherDTO> publishers = new ArrayList<>();
        String searchPattern = "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";

        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(SEARCH_PUBLISHERS)){
            ps.setString(1, searchPattern);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    publishers.add(new PublisherDTO(rs.getInt("publisherID"), rs.getString("publisher_name")));
                }
            }
        }catch (SQLException | IOException e){
            throw new RuntimeException("searchPublishers() failed: " + e.getMessage(), e);
        }
        return publishers;
    }
}
