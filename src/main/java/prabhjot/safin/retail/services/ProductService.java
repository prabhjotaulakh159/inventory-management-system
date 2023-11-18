package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Product;

/**
 * Performs crud operations on the products table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class ProductService {
    private Connection connection;
    
    public ProductService(Connection connection){
        this.connection = connection;
    }


    public void createProduct(Product product) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(product.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Product"));
        String sql = "{call product_pkg.create_product(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setObject(1, product);
        callableStatement.execute();
        connection.commit();
    }

    public void updateProduct(int id, Product product) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(product.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Product"));
        String sql= "{call product_pkg.update_product(?, ?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, id);
        callableStatement.setObject(2, product);
        callableStatement.execute();
        connection.commit();
    }

    public void deleteProduct(int id)throws SQLException {
        String sql = "{call product_pkg.detele_product(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, id);
        callableStatement.execute();
        connection.commit();
    }

    public Product getProduct(int id)throws SQLException, ClassNotFoundException{
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("PRODUCT_TYPE", Class.forName("prabhjot.safin.retail.models.Product"));
        String sql= "{? = call product_pkg.get_product(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.STRUCT , "PRODUCT_TYPE");
        callableStatement.setInt(2, id);
        callableStatement.execute();
        Product product = (Product) callableStatement.getObject(1);
        return product;
    }

    public Map<Integer, Product> getProducts() throws SQLException, ClassNotFoundException{
        Map<Integer, Product> products= new HashMap<Integer, Product>();
        String sql = "{? = call product_pkg.get_products()}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while(resultSet.next()){
            products.put(resultSet.getInt(1), this.getProduct(resultSet.getInt(1)));
        }

        return products;
    }
}
