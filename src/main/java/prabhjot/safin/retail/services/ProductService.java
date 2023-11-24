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
    
    /**
     * Constructor
     * @param connection Current database connection
     */
    public ProductService(Connection connection){
        this.connection = connection;
    }

    /**
     * Creates a product
     * @param product Product to create
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void createProduct(Product product) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(product.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Product"));
        String sql = "{call product_pkg.create_product(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setObject(1, product);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Updates a product by id 
     * @param id Id of the product to update
     * @param product New product information
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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
        callableStatement.close();
    }

    /**
     * Deletes a product by id 
     * @param id Id of the product to delete
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void deleteProduct(int id) throws SQLException {
        String sql = "{call product_pkg.delete_product(?)}";
        CallableStatement callableStatement= connection.prepareCall(sql);
        callableStatement.setInt(1, id);
        callableStatement.execute();
        connection.commit();
        callableStatement.close();
    }

    /**
     * Retrives a product
     * @param id Id of product to retrieve
     * @return Product with id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Product getProduct(int id) throws SQLException, ClassNotFoundException{
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("PRODUCT_TYPE", Class.forName("prabhjot.safin.retail.models.Product"));
        String sql = "{? = call product_pkg.get_product(?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.STRUCT , "PRODUCT_TYPE");
        callableStatement.setInt(2, id);
        callableStatement.execute();
        Product product = (Product) callableStatement.getObject(1);
        callableStatement.close();
        return product;
    }

    /**
     * Gets all products
     * @return Mapping of product id and product object
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<Integer, Product> getProducts() throws SQLException, ClassNotFoundException{
        Map<Integer, Product> products= new HashMap<Integer, Product>();
        String sql = "{? = call product_pkg.get_products()}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while(resultSet.next()) {
            products.put(resultSet.getInt(2), this.getProduct(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return products;
    }
}
