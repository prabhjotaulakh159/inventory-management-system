package prabhjot.safin.retail;

import java.io.Console;
import java.io.IOError;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Admin;
import prabhjot.safin.retail.models.Category;
import prabhjot.safin.retail.services.AdminService;
import prabhjot.safin.retail.services.CategoryService;
import prabhjot.safin.retail.services.ProductService;
import prabhjot.safin.retail.services.ReviewService;
import prabhjot.safin.retail.services.StoreService;
import prabhjot.safin.retail.services.WarehouseService;

public class AdminApp {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            AdminService adminService = new AdminService(connectionProvider.getConnection());
            CategoryService categoryService = new CategoryService(connectionProvider.getConnection());
            ProductService productService = new ProductService(connectionProvider.getConnection());
            ReviewService reviewService = new ReviewService(connectionProvider.getConnection());
            StoreService storeService = new StoreService(connectionProvider.getConnection());
            WarehouseService warehouseService = new WarehouseService(connectionProvider.getConnection());

            login(adminService);

            while (true) {
                System.out.println("Here are your options: ");
                System.out.println("Press 1 for categories");
                System.out.println("Press 2 for products");
                System.out.println("Press 3 for reviews");
                System.out.println("Press 4 for warehouses");
                System.out.println("Press 5 to exit");
                
                int input = sc.nextInt();

                if (input == 1) {
                    categoryCrud(categoryService);
                } else if (input == 2) {
                    
                } else if (input == 3) {

                } else if (input == 4) {
                    
                } else if (input == 5) {
                    break;
                } else {
                    System.out.println("Not a valid option !");
                }
            }

            System.out.println("Goodbye !");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InputMismatchException e) {
            System.out.println("Not a valid option !");
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

    private static void categoryCrud(CategoryService categoryService) {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all categories");
                System.out.println("Enter 2 to get a category by id");
                System.out.println("Enter 3 to create a category");
                System.out.println("Enter 4 to update a category by id");
                System.out.println("Enter 5 to delete a category by id");
                System.out.println("Enter 6 to exit categories");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                if (input == 1) {
                    Map<Integer, Category> categories = categoryService.getCategories();
                    for (Integer id : categories.keySet()) System.out.println(id + ", " + categories.get(id).getCategory());
                } else if (input == 2) {
                    System.out.println("Enter id: ");
                    int id = sc.nextInt();
                    Category category = categoryService.getCategory(id);
                    System.out.println(category.getCategory());
                } else if (input == 3) {
                    System.out.println("Enter category name: ");
                    String name = sc.next();
                    categoryService.createCategory(new Category(name));
                    System.out.println("Successfully created category !");
                } else if (input == 4) {
                    System.out.println("Enter id of category to update: ");
                    int id = sc.nextInt();
                    System.out.println("Enter new category name: ");
                    String name = sc.next();
                    categoryService.updateCategory(id, name);
                    System.out.println("Successfully updated category !");
                } else if (input == 5) {
                    System.out.println("Enter id of category to delete: ");
                    int id = sc.nextInt();
                    categoryService.deleteCategory(id);
                    System.out.println("Successfully deleted category");
                } else if (input == 6) {
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
