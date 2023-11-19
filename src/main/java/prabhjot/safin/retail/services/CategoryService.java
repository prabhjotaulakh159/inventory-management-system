package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Category;

public class CategoryService {
    private Connection connection;

    public CategoryService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new category in the database.
     *
     * @param category The Category object representing the category to be created.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public void createCategory(Category category) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(category.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Category"));
        String SQL = "{call category_pkg.create_category(?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setObject(1, category);
        callableStatement.execute();
        this.connection.commit();
    }

    /**
     * Updates an existing category in the database.
     *
     * @param categoryId The ID of the category to be updated.
     * @param category   The updated category name.
     * @throws SQLException If a database access error occurs.
     */
    public void updateCategory(int categoryId, String category) throws SQLException {
        String SQL = "{call category_pkg.update_category(?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setInt(1, categoryId);
        callableStatement.setString(2, category);
        callableStatement.execute();
        this.connection.commit();
    }

    /**
     * Deletes a category from the database based on its ID.
     *
     * @param categoryId The ID of the category to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteCategory(int categoryId) throws SQLException {
        String SQL = "{call category_pkg.delete_category(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setInt(1, categoryId);
        callableStatement.execute();
        this.connection.commit();
    }

    /**
     * Retrieves a category from the database based on its ID.
     *
     * @param categoryId The ID of the category to be retrieved.
     * @return The Category object representing the retrieved category.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Category getCategory(int categoryId) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("CATEGORY_TYPE", Class.forName("prabhjot.safin.retail.models.Category"));
        String SQL = "{? = call category_pkg.get_category(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.STRUCT, "CATEGORY_TYPE");
        callableStatement.setInt(2, categoryId);
        callableStatement.execute();
        Category category = (Category) callableStatement.getObject(1);
        return category;  
    }

    /**
     * Retrieves all categories from the database and returns them as a Map.
     *
     * @return A Map where keys are category IDs and values are Category objects.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Map<Integer, Category> getCategories() throws SQLException, ClassNotFoundException {
        Map<Integer, Category> categories = new HashMap<Integer, Category>();
        String SQL = "{? = call category_pkg.get_categories()}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            categories.put(resultSet.getInt(2), this.getCategory(resultSet.getInt(2)));
        }
        return categories;
    }
}