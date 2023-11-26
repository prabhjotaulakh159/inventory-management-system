package prabhjot.safin.retail.apps;

import java.util.Map;
import java.util.Scanner;
import java.sql.SQLException;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Category;
import prabhjot.safin.retail.models.Product;
import prabhjot.safin.retail.models.Review;
import prabhjot.safin.retail.models.Store;
import prabhjot.safin.retail.services.AdminService;
import prabhjot.safin.retail.services.CategoryService;
import prabhjot.safin.retail.services.CustomerService;
import prabhjot.safin.retail.services.OrderService;
import prabhjot.safin.retail.services.ProductService;
import prabhjot.safin.retail.services.ReviewService;
import prabhjot.safin.retail.services.StoreService;
import prabhjot.safin.retail.services.WarehouseService;
import prabhjot.safin.retail.services.audit.AuditService;

/**
 * Defines common methods and functionalities between customers and admins
 * @author Prabhjot Aulakh, Safin Haque
 */
public abstract class Application {
    protected Scanner sc;
    protected ConnectionProvider connectionProvider;
    protected AdminService adminService;
    protected CategoryService categoryService;
    protected ProductService productService;
    protected ReviewService reviewService;
    protected StoreService storeService;
    protected WarehouseService warehouseService;
    protected CustomerService customerService;
    protected OrderService orderService;
    protected AuditService auditService;

    public Application() {
        try {
            this.connectionProvider = new ConnectionProvider();
        } catch (SQLException e) {
            this.handleSQLException(e);
        }
        this.sc = new Scanner(System.in);
        this.adminService = new AdminService(connectionProvider.getConnection());
        this.categoryService = new CategoryService(connectionProvider.getConnection());
        this.productService = new ProductService(connectionProvider.getConnection());
        this.reviewService = new ReviewService(connectionProvider.getConnection());
        this.storeService = new StoreService(connectionProvider.getConnection());
        this.warehouseService = new WarehouseService(connectionProvider.getConnection());
        this.customerService = new CustomerService(connectionProvider.getConnection());
        this.orderService = new OrderService(connectionProvider.getConnection());
        this.auditService = new AuditService(connectionProvider.getConnection());
    }

    /**
     * Entry point of the application
     */
    public abstract void run();

    /**
     * Prints products on the terminal
     */
    protected void printProducts() {
        try {
            Map<Integer, Product> products = productService.getProducts();
            System.out.println("Here are All the Products!");
            System.out.println("--------------------------------------");
            for (Integer id : products.keySet()) {
                Category category = categoryService.getCategory(products.get(id).getCategory_id());
                System.out.println("Product Id: " + id + ", " + products.get(id) + ", " + category);
            }
            System.out.println("--------------------------------------");
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------------------");
    }
    /**
     * Gets a product by it's id and prints on the terminal 
     */
    protected void getProductById() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Enter id of product: ");
                System.out.println("--------------------------------------");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                Product product = this.productService.getProduct(id);
                Category category = this.categoryService.getCategory(product.getCategory_id());
                System.out.println("--------------------------------------");
                System.out.println(product + ", " + category);
                System.out.println("--------------------------------------");
                System.out.println();
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }   
        }
    }

    /**
     * Prints all stores on the terminal
     */
    protected void printStores() {
        try {
            Map<Integer, Store> stores = this.storeService.getStores();
            for (Integer id : stores.keySet()) {
                System.out.println("Store Id: " + id + ", " + stores.get(id));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print stores whom have the price of a product
     * @param id Product id
     */
    protected void printStoresWithProduct(int id) {
        try {
            Map<Integer, Store> stores = storeService.getStoresWithProduct(id);
            System.out.println("Available price for stores: ");
            System.out.println("--------------------------------------");
            for (Integer i : stores.keySet()) {
                System.out.println("Store Id: " + i + ", " + stores.get(i));
            }
            System.out.println("--------------------------------------");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            this.handleSQLException(e);
        }
        System.out.println("--------------------------------------");
    }

    /**
     * Retrieves a store by id and prints it on the terminal
     */
    protected void getStoreById() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Enter store id: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                Store store = this.storeService.getStore(id);
                System.out.println(store);
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }   
        }
    }

    /**
     * Retrives a price from a store and prints it on the terminal
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    protected void getPriceAtStore()  {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                this.printProducts();
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Please choose product id from above: ");
                System.out.println("--------------------------------------");
                int productId = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(productId)) { 
                    return;
                }
                this.printStoresWithProduct(productId);
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Please choose store id from above: ");
                System.out.println("--------------------------------------");
                int storeId = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(storeId)) {
                    return;
                }
                int price = this.storeService.getProductPrice(productId, storeId);
                System.out.println("--------------------------------------");
                System.out.println("Price: " + price + "$");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
        System.out.println("--------------------------------------");
    }

    /**
     * Retrives reviews for a product and prints them on the terminal
     */
    protected void getReviewForProduct() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                this.printProducts();
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Enter product id from above to get reviews on: ");
                System.out.println("--------------------------------------");
                System.out.println("Enter product id from above to get reviews on: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(id)) { 
                    return;
                }
                Map<Integer, Review> reviews = reviewService.getReviewForProduct(id);
                System.out.println("--------------------------------------");
                for (Integer reviewId : reviews.keySet()) {
                    System.out.println("Review Id: " + reviewId + ", " + reviews.get(reviewId));
                }
                System.out.println("--------------------------------------");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Parses an SQL exception and prints the first line
     * @param e SQL error
     */
    protected void handleSQLException(SQLException e){
        System.out.println(e.getMessage().split("\n")[0]);
    }

    /**
     * Checks if a string (input) is a Q, meaning they choose to cancel an operation
     * @param Q String to check cancellation 
     * @return If it's equal to Q
     */
    protected boolean cancelStringOperation(String Q) {
        return Q.toUpperCase().equals("Q");
    }

    /**
     * Checks if an integer from input is equal to 0, in which case the user decided to cancel an operation
     * @param input User input as int
     * @return If input is equal to 0
     */
    protected boolean cancelIntegerOperation(int input) {
        return input == 0;
    }

    /**
     * Shows the user what to enter to cancel a integer-related operation
     */
    protected void showCancelInteger() {
        System.out.println("Enter 0 to quit");
    }

    /**
     * Shows the user what to enter to cancel a string-related operation
     */
    protected void showCancelString() {
        System.out.println("Enter Q to quit");
    }
}
