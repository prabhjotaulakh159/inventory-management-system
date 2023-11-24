package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Warehouse;

/**
 * Peforms crud operations on the warehouse table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class WarehouseService {
    private Connection connection;
    
    /**
     * Constructor that takes a Connection as a parameter.
     * @param connection An active database connection.
     */
    public WarehouseService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new warehouse entry in a database using the provided Connection.
     * @param connection An active database connection.
     * @param warehouse The warehouse object to be created in the database.
     * @throws SQLException If an SQL exception occurs during the database operation.
     * @throws ClassNotFoundException If class cannot be found
     */
    public void create(Warehouse warehouse) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(warehouse.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Warehouse"));
        String SQL = "{call warehouse_pkg.create_warehouse(?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setObject(1, warehouse);
        callableStatement.execute();
        connection.commit();
        callableStatement.close();
    }

     /**
     * Updates an existing warehouse entry in a database using the provided Connection.
     * @param warehouseId Id of the warehouse to be updated in the database
     * @param warehouse The warehouse object to be updated in the database.
     * @throws SQLException If an SQL exception occurs during the database operation.
     * @throws ClassNotFoundException If class cannot be found
     */
    public void update(int warehouseId, Warehouse warehouse) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(warehouse.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Warehouse"));
        String SQL = "{call warehouse_pkg.update_warehouse(?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setInt(1, warehouseId);
        callableStatement.setObject(2, warehouse);
        callableStatement.execute();
        connection.commit();
        callableStatement.close();
    }

    /**
     * Deletes an existing warehouse entry from the database based on the provided ID.
     * @param connection An active database connection.
     * @param warehouseId The warehouseId of the warehouse to be deleted from the database.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void delete(int warehouseId) throws SQLException {
        String SQL = "{call warehouse_pkg.delete_warehouse(?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setInt(1, warehouseId);
        callableStatement.execute();
        connection.commit();
        callableStatement.close();
    }

    /**
     * Updates the stock quantity of a product in a specific warehouse in the database.
     * @param warehouseId The ID of the warehouse where the product stock is being updated.
     * @param productId The ID of the product whose stock is being updated.
     * @param quantity The quantity by which to update the stock (can be positive or negative).
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void updateStock(int warehouseId, int productId, int quantity) throws SQLException {
        String SQL = "{call warehouse_pkg.update_stock(?, ?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setInt(1, warehouseId);
        callableStatement.setInt(2, productId);
        callableStatement.setInt(3, quantity);
        callableStatement.execute();
        connection.commit();
        callableStatement.close();
    }

    /**
     * Inserts a product into a specific warehouse in the database.
     * @param warehouseId The ID of the warehouse where the product is being inserted.
     * @param productId The ID of the product to be inserted.
     * @param quantity The initial quantity of the product in the warehouse.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void insertProduct(int warehouseId, int productId, int quantity) throws SQLException {
        String SQL = "{call warehouse_pkg.insert_product_into_warehouse(?, ?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setInt(1, warehouseId);
        callableStatement.setInt(2, productId);
        callableStatement.setInt(3, quantity);
        callableStatement.execute();
        connection.commit();
        callableStatement.close();
    }

    /**
     * Retrieves a specific warehouse from the database based on its ID.
     * @param connection An active database connection.
     * @param warehouseId The ID of the warehouse to retrieve.
     * @return The Warehouse object corresponding to the given ID.
     * @throws SQLException If an SQL exception occurs during the database operation.
     * @throws ClassNotFoundException
     */
    public Warehouse getWarehouse(int warehouseId) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("WAREHOUSE_TYPE", Class.forName("prabhjot.safin.retail.models.Warehouse"));
        String SQL = "{? = call warehouse_pkg.get_warehouse(?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.STRUCT, "WAREHOUSE_TYPE");
        callableStatement.setInt(2, warehouseId);
        callableStatement.execute();
        Warehouse warehouse = (Warehouse) callableStatement.getObject(1);
        callableStatement.close();
        return warehouse;
    }

    /**
     * Retrieves a list of all warehouses from the database.
     * @param connection An active database connection.
     * @return A list of Warehouse objects representing all warehouses in the database.
     * @throws SQLException If an SQL exception occurs during the database operation.
     * @throws ClassNotFoundException
     */
    public Map<Integer, Warehouse> getWarehouses() throws SQLException, ClassNotFoundException {
        Map<Integer, Warehouse> warehouses = new HashMap<Integer, Warehouse>();
        String SQL = "{? = call warehouse_pkg.get_all_warehouses()}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            warehouses.put(resultSet.getInt(2), this.getWarehouse(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return warehouses;
    }

    /**
     * Retrieves the stock quantity of a specific product from the database.
     * @param connection An active database connection.
     * @param productId The ID of the product to get the stock quantity for.
     * @return The stock quantity of the specified product.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public int getProductStock(int productId) throws SQLException {
        String SQL = "{? = call warehouse_pkg.get_stock(?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.setInt(2, productId);
        callableStatement.execute();
        int stock = callableStatement.getInt(1);
        callableStatement.close();
        return stock;
    }
} 