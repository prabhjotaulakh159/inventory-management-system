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

public abstract class Application {
    protected static Scanner sc;
    protected static ConnectionProvider connectionProvider;
    protected static AdminService adminService;
    protected static CategoryService categoryService;
    protected static ProductService productService;
    protected static ReviewService reviewService;
    protected static StoreService storeService;
    protected static WarehouseService warehouseService;
    protected static CustomerService customerService;
    protected static OrderService orderService;

    public Application() throws SQLException {
        sc = new Scanner(System.in);
        connectionProvider = new ConnectionProvider();
        adminService = new AdminService(connectionProvider.getConnection());
        categoryService = new CategoryService(connectionProvider.getConnection());
        productService = new ProductService(connectionProvider.getConnection());
        reviewService = new ReviewService(connectionProvider.getConnection());
        storeService = new StoreService(connectionProvider.getConnection());
        warehouseService = new WarehouseService(connectionProvider.getConnection());
        customerService = new CustomerService(connectionProvider.getConnection());
        orderService = new OrderService(connectionProvider.getConnection());
    }

    public abstract void run();

    public void printProducts() throws SQLException, ClassNotFoundException {
        Map<Integer, Product> products = productService.getProducts();
        for (Integer id : products.keySet()) {
            Category category = categoryService.getCategory(products.get(id).getCategory_id());
            System.out.println("Product Id: " + id + ", " + products.get(id) + ", " + category);
        }
    }
    
    public void getProductById() throws SQLException, ClassNotFoundException {
        System.out.println("Enter id of product: ");
        int id = sc.nextInt();
        sc.nextLine();
        Product product = productService.getProduct(id);
        Category category = categoryService.getCategory(product.getCategory_id());
        System.out.println(product + ", " + category);
    }

    public void printStores() throws ClassNotFoundException, SQLException {
        Map<Integer, Store> stores = storeService.getStores();
        for (Integer id : stores.keySet()) {
            System.out.println("Store Id: " + id + ", " + stores.get(id));
        }
    }

    public void printStoresWithProduct(int id) throws ClassNotFoundException, SQLException {
        Map<Integer, Store> stores = storeService.getStoresWithProduct(id);
        System.out.println("Available price for stores: ");
        for (Integer i : stores.keySet()) {
            System.out.println("Store Id: " + i + ", " + stores.get(i));
        }
    }

    public void getStoreById() throws SQLException, ClassNotFoundException {
        System.out.println("Enter store id: ");
        int id = sc.nextInt();
        sc.nextLine();
        Store store = storeService.getStore(id);
        System.out.println(store);
    }

    public void getPriceAtStore() throws SQLException, ClassNotFoundException {
        printProducts();
        System.out.println("Please choose product id from above: ");
        int productId = sc.nextInt();
        sc.nextLine();
        printStoresWithProduct(productId);
        System.out.println("Please choose store id from above: ");
        int storeId = sc.nextInt();
        sc.nextLine();
        int price = storeService.getProductPrice(productId, storeId);
        System.out.println("Price: " + price + "$");
    }


    public void getReviewForProduct() throws SQLException, ClassNotFoundException {
        printProducts();
        System.out.println("Enter product id from above to get reviews on: ");
        int id = sc.nextInt();
        sc.nextLine();
        Map<Integer, Review> reviews = reviewService.getReviewForProduct(id);
        for (Integer reviewId : reviews.keySet()) {
            System.out.println("Review Id: " + reviewId + ", " + reviews.get(reviewId));
        }
    }

    public void handleSQLException(String message){
        System.out.println(message.split("\n")[0]);
    }
}
