package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import prabhjot.safin.retail.models.Admin;

/**
 * Logs in admins
 * @author Prabhjot Aulakh, Safin Haque
 */
public class AdminService {
    /**
     * Logs in an admin
     * @param connection Connection to db
     * @param admin Admin type 
     */
    public boolean login(Connection connection, Admin admin) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = connection.getTypeMap();
        connection.setTypeMap(map);
        map.put(admin.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Admin"));
        String SQL = "{? = call admin_pkg.login(?, ?)}";
        CallableStatement cs = connection.prepareCall(SQL);
        cs.registerOutParameter(1, OracleTypes.PLSQL_BOOLEAN);
        cs.setInt(2, admin.getId());
        cs.setString(3, admin.getPassword());
        cs.execute();
        return cs.getBoolean(1);
    }
}
