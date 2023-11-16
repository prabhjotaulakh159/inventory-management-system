package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import prabhjot.safin.retail.models.Admin;

/**
 * Performs CRUD operations on the admin table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class AdminService {
    private Connection connection;

    public AdminService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Authenticates an administrator based on ID and password.
     *
     * @param admin Holds admin credentials
     * @return True if authentication is successful, otherwise false.
     * @throws SQLException            If a database access error occurs.
     */
    public boolean login(Admin admin) throws SQLException, ClassNotFoundException {
        String SQL = "{? = call admin_pkg.login(?, ?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, OracleTypes.BOOLEAN);
        callableStatement.setInt(2, admin.getId());
        callableStatement.setString(3, admin.getPassword());
        callableStatement.execute();
        boolean result = callableStatement.getBoolean(1);
        return result;
    }
}