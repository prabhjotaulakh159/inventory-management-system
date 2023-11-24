package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Order;

/**
 * Peforms crud operations on the orders table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class OrderService {
    private Connection connection;

    public OrderService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates an order
     * @param order Holds basic order information like customer and store
     * @param products A mapping, where the keys are the product id and values are the desired quantity
     * @return The order id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int createOrder(Order order, Map<Integer, Integer> products) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(order.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Order"));
        String SQL = "{call order_pkg.create_order(?, ?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setObject(1, order);
        callableStatement.registerOutParameter(2, Types.INTEGER);
        callableStatement.execute();
        int orderId = callableStatement.getInt(2);
        CallableStatement callableStatement2 = this.connection.prepareCall("{call order_pkg.insert_product_into_order(?, ?, ?)}");
        for (Integer product : products.keySet()) {
            callableStatement2.setInt(1, orderId);
            callableStatement2.setInt(2, product);
            callableStatement2.setInt(3, products.get(product));
            callableStatement2.execute();
        }
        this.connection.commit();
        return orderId;
    }

    /**
     * Deletes an order
     * @param orderId Id of the order to delete
     * @throws SQLException
     */
    public void deleteOrder(int orderId) throws SQLException {
        String SQL = "{call order_pkg.delete_order(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setInt(1, orderId);
        callableStatement.execute();
        this.connection.commit();
    }

    /**
     * Retrives information about an order
     * @param orderId Id of the order
     * @return Order object with customer id, store id and order date
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Order getOrder(int orderId) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("ORDER_TYPE", Class.forName("prabhjot.safin.retail.models.Order"));
        String SQL = "{? = call order_pkg.get_order(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.STRUCT, "ORDER_TYPE");
        callableStatement.setInt(2, orderId);
        callableStatement.execute();
        Order order = (Order) callableStatement.getObject(1);
        return order;
    }

    /**
     * Retrives the list of all orders made by a customer
     * @param customerId Customer whom orders we want to retrieve
     * @return Mapping of order id and order object
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<Integer, Order> getOrdersByCustomer(int customerId) throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = new HashMap<Integer, Order>();
        String SQL = "{? = call order_pkg.get_customer_orders(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(2, customerId);
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while(resultSet.next()) {
            orders.put(resultSet.getInt(2), this.getOrder(resultSet.getInt(2)));
        }
        return orders;
    }

    /**
     * Retrives a mapping of products names and their quantites for a particular order
     * @param orderId Id of the order
     * @return Mapping of products names and their quantites for a particular order
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<String, Integer> getOrderDetails(int orderId) throws SQLException, ClassNotFoundException {
        ProductService productService = new ProductService(this.connection);
        Map<String, Integer> productQuantityMapping = new HashMap<String, Integer>();
        String SQL = "{? = call order_pkg.get_order_products(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(2, orderId);
        callableStatement.execute();
        ResultSet products = callableStatement.getArray(1).getResultSet();
        while (products.next()) {
            String productName = productService.getProduct(products.getInt(2)).getName();
            String quantitySql = "{? = call order_pkg.get_order_product_quantity(?,?)}";
            CallableStatement quantityStatement = this.connection.prepareCall(quantitySql);
            quantityStatement.registerOutParameter(1, Types.INTEGER);
            quantityStatement.setInt(2, orderId);
            quantityStatement.setInt(3, products.getInt(1));
            quantityStatement.execute();
            int quantity = quantityStatement.getInt(1);
            productQuantityMapping.put(productName, quantity);
        }
        return productQuantityMapping;
    }

    /**
     * Gets total price of a order
     * @param orderId Order to get total of
     * @throws SQLException
     */
    public int getOrderTotal(int orderId) throws SQLException {
        CallableStatement callableStatement = this.connection.prepareCall("{? = call order_pkg.price_order(?)}");
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.setInt(2, orderId);
        callableStatement.execute();
        int price = callableStatement.getInt(1);
        return price;
    }
}
