package DAO;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * Central JDBC connection provider.
 * <p>
 * Implemented as enum singleton to guarantee one shared configuration loader.
 * Connection settings are loaded from {@code database.properties} on first use.
 */
public enum DataSource {
    INSTANCE;

    private String url;
    private String username;
    private String password;

    // Load JDBC driver once when class is initialized.
    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    /**
     * Loads database configuration from classpath properties file.
     *
     * @throws IOException when properties file is missing or unreadable
     */
    private void openFile() throws IOException {
        Properties props = new Properties();

        // Load resource from src/main/resources.
        try(InputStream in = DataSource.class.getClassLoader().getResourceAsStream("database.properties")){
            if(in == null){
                throw new IOException("database.properties not found on classpath");
            }
            props.load(in);
        }

        // Cache properties into instance fields for subsequent connection calls.
        this.url = props.getProperty("jdbc.url");
        this.username = props.getProperty("jdbc.username");
        this.password = props.getProperty("jdbc.password");
    }

    /**
     * Returns a new JDBC connection.
     *
     * @return connection instance
     * @throws SQLException when credentials are missing or connection fails
     * @throws IOException when properties cannot be loaded
     */
    public Connection getConnection() throws SQLException, IOException {
        // Lazy-load config only once.
        if(url == null){
            openFile();
        }

        if(username == null || password == null){
            throw new SQLException("DB credential not set.");
        }
        // Create and return a fresh JDBC connection.
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Verifies the connection can be established.
     *
     * @throws SQLException when connection fails
     * @throws IOException when properties cannot be loaded
     */
    public void testConnection() throws SQLException, IOException {
        // try-with-resources ensures test connection is always closed.
        try(Connection con = getConnection()){
            if (con == null || con.isClosed()) {
                throw new SQLException("Connection test failed.");
            }
        }
    }
}
