package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import prabhjot.safin.retail.models.Customer;

/**
 * Performs crud operations on the customers table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class CustomerService {
    public boolean login(Connection connection, Customer customer) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = connection.getTypeMap();
        connection.setTypeMap(map);
        map.put(customer.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Customer"));
        String SQL = "{? = call customer_pkg.login(?, ?)}";
        CallableStatement cs = connection.prepareCall(SQL);
        cs.registerOutParameter(1, OracleTypes.PLSQL_BOOLEAN);
        cs.setInt(2, customer.getId());
        cs.setString(3, customer.getPassword());
        cs.execute();
        return cs.getBoolean(1);
    } 
}
