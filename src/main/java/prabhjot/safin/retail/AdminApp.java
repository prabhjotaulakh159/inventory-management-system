package prabhjot.safin.retail;

import java.io.Console;
import java.io.IOError;
import java.sql.SQLException;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Admin;
import prabhjot.safin.retail.services.AdminService;
import prabhjot.safin.retail.services.CategoryService;
import prabhjot.safin.retail.services.ProductService;
import prabhjot.safin.retail.services.ReviewService;
import prabhjot.safin.retail.services.StoreService;

public class AdminApp {
    public static void main(String[] args) {
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            AdminService adminService = new AdminService(connectionProvider.getConnection());
            CategoryService categoryService = new CategoryService(connectionProvider.getConnection());
            ProductService productService = new ProductService(connectionProvider.getConnection());
            ReviewService reviewService = new ReviewService(connectionProvider.getConnection());
            StoreService storeService = new StoreService(connectionProvider.getConnection());

            login(adminService);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void login(AdminService adminService) {
        while (true) {
            try {
                Console console = System.console();
                System.out.println("Enter your admin id: ");
                int id = Integer.parseInt(console.readLine());
                System.out.println("Enter your password: ");
                String password = "";
                char[] passwordInput = console.readPassword();
                for (char c : passwordInput) password += c;
                Admin admin = new Admin(id, password);
                int status = adminService.login(admin);
                if (status == 0) {
                    System.out.println("Hello admin #" + admin.getId());
                    break;
                } else {
                    System.out.println("Login failed !");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } catch (IOError e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                System.out.println("Internal Server error");
            }
        }
    }
}
