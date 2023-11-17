package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
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
        String SQL = "{call order_pkg.delete_order(?)}"
        CallableStatement callableStatement = this.connection.prepareCall();
        callableStatement.setInt(1, orderId);
        callableStatement.execute();
        this.connection.commit();
    }
}
