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

    public void deleteOrder(int orderId) throws SQLException {
        String SQL = "{call order_pkg.delete_order(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setInt(1, orderId);
        callableStatement.execute();
        this.connection.commit();
    }

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

    public Map<Integer, Order> getOrdersByCustomer(int customerId) throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = new HashMap<Integer, Order>();
        String SQL = "{? = call order_pkg.get_customer_orders(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(2, customerId);
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while(resultSet.next()) {
            orders.put(resultSet.getInt(1), this.getOrder(resultSet.getInt(1)));
        }
        return orders;
    }

    public Map<String, Integer> getOrderDetails(int orderId) throws SQLException, ClassNotFoundException {
        ProductService productService = new ProductService(this.connection);
        Map<String, Integer> productQuantityMapping = new HashMap<String, Integer>();
        String SQL = "{? = call order_pkg.get_order_products(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(1, orderId);
        callableStatement.execute();
        ResultSet products = callableStatement.getArray(1).getResultSet();
        while (products.next()) {
            String productName = productService.getProduct(products.getInt(1)).getName();
            String quantitySql = "{? = order_pkg.get_order_product_quantity(?,?)}";
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
}
