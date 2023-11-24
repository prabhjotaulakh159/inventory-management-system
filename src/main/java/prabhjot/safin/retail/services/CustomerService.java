package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Customer;

/**
 * Performs crud operations on the customers table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class CustomerService {
    private Connection connection;

    /**
     * Constructor
     * @param connection Current database connection
     */
    public CustomerService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Authenticates a customer based on customer ID and password.
     * @param customerId The ID of the customer.
     * @param password   The password for authentication.
     * @return The Customer object if authentication is successful, otherwise null.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Customer login(int customerId, String password) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("CUSTOMER_TYPE", Class.forName("prabhjot.safin.retail.models.Customer"));
        String sql = "{? = call customer_pkg.login(?,?)}";
        CallableStatement callableStatement = this.connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.STRUCT, "CUSTOMER_TYPE");
        callableStatement.setInt(2, customerId);
        callableStatement.setString(3, password);
        callableStatement.execute();
        Customer customer = (Customer) callableStatement.getObject(1);
        callableStatement.close();
        return customer;
    }

    /**
     * Retrives a customer by id
     * @param id Id of the customer
     * @throws SQLException If database error occurs
     * @throws ClassNotFoundException If the mapping fails
     */
    public Customer getCustomer(int id) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("CUSTOMER_TYPE", Class.forName("prabhjot.safin.retail.models.Customer"));
        String SQL = "{? = call customer_pkg.get_customer_by_id(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.STRUCT, "CUSTOMER_TYPE");
        callableStatement.setInt(2, id);
        callableStatement.execute();
        Customer customer = (Customer) callableStatement.getObject(1);
        callableStatement.close();
        return customer;
    }
    
    /**
     * Retrives all customers from the database
     * @return Mapping of customers id and customer type
     * @throws SQLException If database error occurs
     * @throws ClassNotFoundException If mapping fails
     */
    public Map<Integer, Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        Map<Integer, Customer> customers = new HashMap<Integer, Customer>();
        String SQL = "{? = call customer_pkg.get_customers()}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            customers.put(resultSet.getInt(2), this.getCustomer(resultSet.getInt(2)));
        }
        resultSet.close();
        callableStatement.close();
        return customers;
    }
}
