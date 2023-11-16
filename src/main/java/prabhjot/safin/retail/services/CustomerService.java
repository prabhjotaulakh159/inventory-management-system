package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import prabhjot.safin.retail.models.Customer;

/**
 * Performs crud operations on the customers table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class CustomerService {
    private Connection connection;

    public CustomerService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Authenticates a customer based on customer ID and password.
     *
     * @param customerId The ID of the customer.
     * @param password   The password for authentication.
     * @return The Customer object if authentication is successful, otherwise null.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Customer login(int customerId, String password) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("CUSTOMER_TYPE", Class.forName("prabhjot.safin.retail.models.Customer"));
        String sql = "{? = call customer_pkg.login(?,?)}";
        CallableStatement callableStatement = this.connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.STRUCT, "CUSTOMER_TYPE");
        callableStatement.setInt(1, customerId);
        callableStatement.setString(2, password);
        callableStatement.execute();
        Customer customer = (Customer) callableStatement.getObject(1);
        return customer;
    }
}
