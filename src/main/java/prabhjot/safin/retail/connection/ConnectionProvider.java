package prabhjot.safin.retail.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handle database connection
 * @author Prabhjot Aulakh, Safin Haque
 */
public class ConnectionProvider {
    private Connection connection;
    /**
     * Constructor
     * @param username Username for connection
     * @param password Password for connection
     */
    public ConnectionProvider() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:oracle:thin:@198.168.52.211:1521/pdbora19c.dawsoncollege.qc.ca", Credentials.USERNAME, Credentials.PASSWORD);
        this.connection.setAutoCommit(false);
    }

    /**
     * Accessor for connection
     * @return The connection instance
     */
    public Connection getConnection() {
        return this.connection;
    }
    
    /**
     * Kills the connection
     */
    public void kill() throws SQLException {
        this.connection.close();
    }

    /**
     * Rolls back changes
     * @throws SQLException
     */
    public void uncommit() throws SQLException {
        this.connection.rollback();
    }
}
