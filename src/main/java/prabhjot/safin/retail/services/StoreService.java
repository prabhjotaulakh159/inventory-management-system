package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Store;

/**
 * Performs the CRUD operations on the store table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class StoreService {
    private Connection connection;
    
    /**
     * Constructor
     * @param connection Current database connection
     */
    public StoreService(Connection connection){
        this.connection= connection;
    }

    /**
     * Creates a store in the database
     * @param store The store to be created 
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the mapped class cannot be found
     */
    public void createStore(Store store) throws SQLException, ClassNotFoundException{
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(store.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Store"));
        String sql= "{call store_pkg.create_store(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setObject(1, store);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Updates a store
     * @param id Id of the store to be updated
     * @param name New name of the store
     * @throws SQLException If a database error occurs
     */
    public void updateStore(int id, String name) throws SQLException {
        String sql= "{call store_pkg.update_store(?, ?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, id);
        callableStatement.setString(2, name);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Deletes a store from the database
     * @param id Id of the store to be deleted
     * @throws SQLException If a database error occurs
     */
    public void deleteStore(int id) throws SQLException{
        String sql= "{call store_pkg.delete_store(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, id);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Creates a price of a product in a store
     * @param storeId Id of the store to insert the price
     * @param productId Id of the product to insert the price
     * @param price Price of the product
     * @throws SQLException If a database error occurs
     */
    public void createPrice(int storeId, int productId, int price) throws SQLException{
        String sql= "{call store_pkg.create_price(?, ?, ?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, storeId);
        callableStatement.setInt(2, productId);
        callableStatement.setInt(3, price);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Updates a price of a product in a store
     * @param storeId Id of the store to update the price
     * @param productId Id of the product to update
     * @param price New price of the product
     * @throws SQLException If a database error occurs
     */
    public void updatePrice(int storeId, int productId, int price) throws SQLException {
        String sql= "{call store_pkg.update_price(?, ?, ?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, storeId);
        callableStatement.setInt(2, productId);
        callableStatement.setInt(3, price);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Get all prices of a product in a different stores
     * @param id Id of the product
     * @return A map of prices to products
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the mapped class cannot be found
     */
    public int getProductPrice(int productId, int storeId) throws SQLException, ClassNotFoundException {
        String SQL = "{? = call store_pkg.get_price_of_product(?, ?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.setInt(2, productId);
        callableStatement.setInt(3, storeId);
        callableStatement.execute();
        int price = callableStatement.getInt(1);
        callableStatement.close();
        return price;
    }
    
    /**
     * Returns a store from the database
     * @param id Id of the store to be retrieved
     * @return The store with the specified id
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the mapped class cannot be found
     */
    public Store getStore(int id) throws SQLException, ClassNotFoundException{
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("STORE_TYPE", Class.forName("prabhjot.safin.retail.models.Store"));
        String sql= "{? = call store_pkg.get_store(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.STRUCT, "STORE_TYPE" );
        callableStatement.setInt(2, id);
        callableStatement.execute();
        Store store= (Store) callableStatement.getObject(1);
        callableStatement.close();
        return store;
    }

    /**
     * Gets all the stores in the database
     * @return List of stores mapped to their id
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the mapped class cannot be found
     */
    public Map<Integer, Store> getStores() throws SQLException, ClassNotFoundException{
        Map<Integer, Store> stores= new HashMap<Integer, Store>();
        String sql= "{?= call store_pkg.get_stores()}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while(resultSet.next()){
            stores.put(resultSet.getInt(2), this.getStore(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return stores;
    }

    /**
     * Gets all the stores in the database that have a product
     * @return List of stores mapped to their id that have the product
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the mapped class cannot be found
     */
    public Map<Integer, Store> getStoresWithProduct(int productId) throws SQLException, ClassNotFoundException{
        Map<Integer, Store> stores= new HashMap<Integer, Store>();
        String sql= "{?= call store_pkg.get_stores_with_product(?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(2, productId);
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while(resultSet.next()){
            stores.put(resultSet.getInt(2), this.getStore(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return stores;
    }
}
