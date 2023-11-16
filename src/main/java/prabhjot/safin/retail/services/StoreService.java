package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prabhjot.safin.retail.models.Product;
import prabhjot.safin.retail.models.Store;

public class StoreService {
  private Connection connection;
  
  public StoreService(Connection connection){
    this.connection= connection;
  }


  public void createStore(Store store) throws SQLException, ClassNotFoundException{
    Map<String, Class<?>> map = this.connection.getTypeMap();
    this.connection.setTypeMap(map);
    map.put(store.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Store"));
    String sql= "{call store_pkg.create_store(?)}";
    CallableStatement callableStatement= connection.prepareCall(sql);
    callableStatement.setObject(1, store);
    callableStatement.execute();
    connection.commit();
  }

  public void updateStore(int id, String name) throws SQLException {
    String sql= "{call store_pkg.create_store(?, ?)}";
    CallableStatement callableStatement= connection.prepareCall(sql);
    callableStatement.setInt(1, id);
    callableStatement.setString(2, name);
    callableStatement.execute();
    connection.commit();
  }

  public void deleteStore(int id) throws SQLException{
      String sql= "{call store_pkg.delete_store(?)}";
      CallableStatement callableStatement= connection.prepareCall(sql);
      callableStatement.setInt(1, id);
      callableStatement.execute();
      connection.commit();
  }

  public void createPrice(int storeId, int productId, int price) throws SQLException{
      String sql= "{call store_pkg.create_price(?, ?, ?)}";
      CallableStatement callableStatement= connection.prepareCall(sql);
      callableStatement.setInt(1, storeId);
      callableStatement.setInt(2, productId);
      callableStatement.setInt(3, price);
      callableStatement.execute();
      connection.commit();
  }

  public void updatePrice(int storeId, int productId, int price) throws SQLException {
    String sql= "{call store_pkg.update_price(?, ?, ?)}";
    CallableStatement callableStatement= connection.prepareCall(sql);
    callableStatement.setInt(1, storeId);
    callableStatement.setInt(2, productId);
    callableStatement.setInt(3, price);
    callableStatement.execute();
    connection.commit();
  }

  public Map<Integer, Store> getProductPrices(int id) throws SQLException, ClassNotFoundException {
    Map<Integer, Store> prices= new HashMap<Integer, Store>();
    String sql= "{? = call store_pkg.get_prices_of_product(?)}";
    CallableStatement callableStatement= connection.prepareCall(sql);
    callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
    callableStatement.setInt(2, id);
    callableStatement.execute();
    ResultSet resultSet= callableStatement.getArray(1).getResultSet();

    while(resultSet.next()){
      prices.put(resultSet.getInt(1), this.getStore(resultSet.getInt(1)));
    }

    return prices;
  }

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
    return store;
  }

  public Map<Integer, Store> getStores() throws SQLException, ClassNotFoundException{
    Map<Integer, Store> stores= new HashMap<Integer, Store>();
    String sql= "{?= call store_pkg.get_stores()}";
    CallableStatement callableStatement = connection.prepareCall(sql);
    callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
    callableStatement.execute();
    ResultSet resultSet = callableStatement.getArray(1).getResultSet();
    while(resultSet.next()){
      stores.put(resultSet.getInt(1), this.getStore(resultSet.getInt(1)));
    }

    return stores;
  }
}
