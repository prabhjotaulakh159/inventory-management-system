package prabhjot.safin.retail;

import java.sql.SQLException;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Category;
import prabhjot.safin.retail.services.CategoryService;

public class AdminApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            Category c1 = new Category("Lops");
            CategoryService service = new CategoryService(connectionProvider.getConnection());
            service.updateCategory(1, "lops2");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
