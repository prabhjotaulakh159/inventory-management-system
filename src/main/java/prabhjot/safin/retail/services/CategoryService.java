package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import prabhjot.safin.retail.models.Category;

public class CategoryService {
    private Connection connection;

    public CategoryService(Connection connection) {
        this.connection = connection;
    }

    public void createCategory(Category category) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(category.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Category"));
        String SQL = "{call category_pkg.create_category(?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setObject(1, category);
        callableStatement.execute();
        connection.commit();
    }

    public void updateCategory(int categoryId, String category) throws SQLException {
        String SQL = "{call category_pkg.update_category(?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(SQL);
        callableStatement.setInt(1, categoryId);
        callableStatement.setString(2, category);
        callableStatement.execute();
        connection.commit();
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String SQL = "{call category_pkg.delete_category(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setInt(1, categoryId);
        callableStatement.execute();
        this.connection.commit();
    }

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

    public List<Category> getCategories() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<Category>();
        String SQL = "{? = call category_pkg.get_categories()}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            categories.add(this.getCategory(resultSet.getInt(1)));
        }
        return categories;
    }
}
