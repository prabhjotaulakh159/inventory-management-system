package prabhjot.safin.retail.apps;

import java.io.Console;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Map;

import prabhjot.safin.retail.models.Admin;
import prabhjot.safin.retail.models.Category;
import prabhjot.safin.retail.models.Customer;
import prabhjot.safin.retail.models.Product;
import prabhjot.safin.retail.models.Review;
import prabhjot.safin.retail.models.Store;
import prabhjot.safin.retail.models.Warehouse;

public class AdminApp extends Application {
    public AdminApp() throws SQLException {
        super();
    }

    @Override
    public void run() {
        try {
            login();
            while (true) {
                System.out.println("--------------------------------------");
                System.out.println("Here are your options: ");
                System.out.println("Press 1 for categories");
                System.out.println("Press 2 for products");
                System.out.println("Press 3 for reviews");
                System.out.println("Press 4 for stores");
                System.out.println("Press 5 for warehouses");
                System.out.println("Press 6 for customers");
                System.out.println("Press 7 to exit");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                if      (input == 1) categoryCrud();
                else if (input == 2) productCrud();
                else if (input == 3) reviewCrud();
                else if (input == 4) storeCrud();
                else if (input == 5) warehouseCrud();
                else if (input == 6) customerCrud();
                else if (input == 7) break;
                else System.out.println("Not a valid option !");
            }
            System.out.println("Goodbye !");
            connectionProvider.kill();
        } catch (SQLException e) {
            handleSQLException(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Not a valid option !");
            sc.next();
        }
    }

    private void login() {
        while (true) {
            try {
                Console console = System.console();
                
                System.out.println("Enter your admin id: ");
                int id = sc.nextInt();
                
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
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid data");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void categoryCrud() {
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
                sc.nextLine();
                
                if      (input == 1) printCategories();
                else if (input == 2) getCategoryById();
                else if (input == 3) createCategory();
                else if (input == 4) updateCategory();
                else if (input == 5) deleteCategory();
                else if (input == 6) break;
                else System.out.println("Invalid option");
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void printCategories() throws ClassNotFoundException, SQLException {
        Map<Integer, Category> categories = categoryService.getCategories();
        for (Integer id : categories.keySet()) { 
            System.out.println("Category Id: " + id + ", " + categories.get(id));
        }
    }

    private void getCategoryById() throws ClassNotFoundException, SQLException {
        showCancelInteger();
        System.out.println("Enter id of category: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        Category category = categoryService.getCategory(id);
        System.out.println(category);
    }

    private void createCategory() throws SQLException, ClassNotFoundException {
        showCancelString();        
        System.out.println("Enter category name: ");
        
        String name = sc.nextLine();
        
        if (cancelStringOperation(name)) return;
        
        categoryService.createCategory(new Category(name));
        System.out.println("Successfully created category !");
    }

    private void updateCategory() throws ClassNotFoundException, SQLException {
        printCategories();
        showCancelInteger();
        System.out.println("Enter id of category from above to update: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        showCancelString();
        System.out.println("Enter new category name: ");
        
        String name = sc.nextLine();
        
        if (cancelStringOperation(name)) return;
        
        categoryService.updateCategory(id, name);
        System.out.println("Successfully updated category !");
    }

    private void deleteCategory() throws ClassNotFoundException, SQLException {
        printCategories();
        showCancelInteger();
        System.out.println("Enter id of category from above to delete: ");
        
        int id = sc.nextInt();
        
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        categoryService.deleteCategory(id);
        System.out.println("Successfully deleted category");
    }

    private void productCrud() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all products");
                System.out.println("Enter 2 to get a product by id");
                System.out.println("Enter 3 to create a product");
                System.out.println("Enter 4 to update a product by id");
                System.out.println("Enter 5 to delete a product by id");
                System.out.println("Enter 6 to exit products");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();
                if      (input == 1) printProducts();
                else if (input == 2) getProductById();
                else if (input == 3) createProduct();
                else if (input == 4) updateProduct();
                else if (input == 5) deleteProduct();
                else if (input == 6) break;
                else System.out.println("Invalid option");
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void createProduct() throws ClassNotFoundException, SQLException {
        showCancelString();
        System.out.println("Enter name of the product: ");
        
        String productName = sc.nextLine();
        
        if (cancelStringOperation(productName)) return;
        
        printCategories();
        showCancelInteger();
        System.out.println("Select a category id from above: ");
        
        int categoryId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(categoryId)) return;
        
        productService.createProduct(new Product(productName, categoryId));
        System.out.println("Product created !");
    }

    private void updateProduct() throws SQLException, ClassNotFoundException {
        printProducts();
        showCancelInteger();
        System.out.println("Enter product id from above to update: ");
        
        int productId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(productId)) return;
        
        showCancelString();
        System.out.println("Enter new name of new product: ");
        String productName = sc.nextLine();
        
        if (cancelStringOperation(productName)) return;
        
        printCategories();
        showCancelInteger();
        System.out.println("Select the new category id from above: ");
        
        int categoryId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(categoryId)) return;

        productService.updateProduct(productId, new Product(productName, categoryId));
        System.out.println("Updated product !");
    }

    private void deleteProduct() throws SQLException, ClassNotFoundException {
        printProducts();
        showCancelInteger();
        System.out.println("Enter product id from above to delete: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        productService.deleteProduct(id);
        System.out.println("Product deleted !");
    }

    private void reviewCrud() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all reviews");
                System.out.println("Enter 2 to get a review by id");
                System.out.println("Enter 3 to get all flagged reviews");
                System.out.println("Enter 4 to delete all flagged reviews");
                System.out.println("Enter 5 to get reviews for a product by id");
                System.out.println("Enter 6 to exit reviews");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();
                if      (input == 1) printReviews();
                else if (input == 2) getReviewById();
                else if (input == 3) printFlaggedReviews();
                else if (input == 4) deleteFlaggedReviews();
                else if (input == 5) getReviewForProduct();
                else if (input == 6) break;
                else System.out.println("Invalid option");
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void printReviews() throws ClassNotFoundException, SQLException {
        Map<Integer, Review> reviews = reviewService.getAllReviews();
        for (Integer id : reviews.keySet()) {
            System.out.println("Review Id: " + id + ", " + reviews.get(id));
        }
    }

    private void getReviewById() throws SQLException, ClassNotFoundException {
        showCancelInteger();
        System.out.println("Enter id of review: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        Review review = reviewService.getReview(id);
        System.out.println(review);
    }

    private void printFlaggedReviews() throws ClassNotFoundException, SQLException {
        Map<Integer, Review> reviews = reviewService.getFlaggedReviews();
        for (Integer id : reviews.keySet()) {
            System.out.println("Review Id: " + id + ", " + reviews.get(id));
        }
    }

    private void deleteFlaggedReviews() throws SQLException {
        reviewService.deleteFlaggedReviews();
        System.out.println("Deleted flagged reviews !");
    }

    private void storeCrud() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all stores");
                System.out.println("Enter 2 to get store by id");
                System.out.println("Enter 3 to create a store");
                System.out.println("Enter 4 to update a store by id");
                System.out.println("Enter 5 to delete a store by id");
                System.out.println("Enter 6 to create a price of a product at a store");
                System.out.println("Enter 7 to update a price of a product at a store");
                System.out.println("Enter 8 to get price of a product at different stores");
                System.out.println("Enter 9 to exit stores");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();
                if      (input == 1) printStores();
                else if (input == 2) getStoreById();
                else if (input == 3) createStore();
                else if (input == 4) updateStore();
                else if (input == 5) deleteStore();
                else if (input == 6) createPrice();
                else if (input == 7) updatePrice();
                else if (input == 8) getPriceAtStore();
                else if (input == 9) break;
                else System.out.println("Invalid option");
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void createStore() throws SQLException, ClassNotFoundException {
        showCancelString();
        System.out.println("Please enter store name: ");
        
        String storeName = sc.nextLine();
        
        if (cancelStringOperation(storeName)) return;
        
        storeService.createStore(new Store(storeName));
        System.out.println("Store has been created");
    }

    private void updateStore() throws ClassNotFoundException, SQLException {
        printStores();
        showCancelInteger();
        System.out.println("Please enter store id to update: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        showCancelString();
        System.out.println("Please enter new store name: ");
        
        String storeName = sc.nextLine();
        
        if (cancelStringOperation(storeName)) return;
        
        storeService.updateStore(id, storeName);
        System.out.println("Store has been updated");
    }

    private void deleteStore() throws ClassNotFoundException, SQLException {
        printStores();
        showCancelInteger();
        System.out.println("Please enter store id to delete: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        storeService.deleteStore(id);
        System.out.println("Store has been deleted");
    }

    private void createPrice() throws SQLException, ClassNotFoundException {
        printProducts();
        showCancelInteger();
        System.out.println("Please choose product id from above: ");
        
        int productId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(productId)) return;
        
        printStores();
        showCancelInteger();
        System.out.println("Please choose store id from above: ");
        
        int storeId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(storeId)) return;
        
        showCancelInteger();
        System.out.println("Please enter a price: ");
        
        int price = sc.nextInt();
        
        if (cancelIntegerOperation(price)) return;
        
        storeService.createPrice(storeId, productId, price);
        System.out.println("Price has been inserted !");
    }

    private void updatePrice() throws SQLException, ClassNotFoundException {
        printProducts();
        showCancelInteger();
        System.out.println("Please choose product id from above: ");
        
        int productId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(productId)) return;
        
        printStores();
        showCancelInteger();
        System.out.println("Please choose store id from above: ");
        
        int storeId = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(storeId)) return;
        
        showCancelInteger();
        System.out.println("Please enter a new price: ");
        
        int price = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(price)) return;
        
        storeService.createPrice(storeId, productId, price);
        System.out.println("Price has been updated !");
    }

    private void warehouseCrud() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all warehouses");
                System.out.println("Enter 2 to get warehouse by id");
                System.out.println("Enter 3 to create a warehouse");
                System.out.println("Enter 4 to update a warehouse by id");
                System.out.println("Enter 5 to delete a warehouse by id");
                System.out.println("Enter 6 to insert a product in a warehouse");
                System.out.println("Enter 7 to update a stock of a product at a warehouse");
                System.out.println("Enter 8 to get the total stock of a product");
                System.out.println("Enter 9 to exit warehouses");
                System.out.println("--------------------------------------");
                int input = sc.nextInt(); sc.nextLine();
                if      (input == 1) printWarehouses(); 
                else if (input == 2) getWarehouseById();
                else if (input == 3) createWarehouse();
                else if (input == 4) updateWarehouse();
                else if (input == 5) deleteWarehouse();
                else if (input == 6) insertProduct();
                else if (input == 7) updateProductStock();
                else if (input == 8) getProductStock();
                else if (input == 9) break;
                else System.out.println("Invalid option");
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void printWarehouses() throws ClassNotFoundException, SQLException {
        Map<Integer, Warehouse> warehouses = warehouseService.getWarehouses();
        for (Integer id : warehouses.keySet()) {
            System.out.println("Warehouse Id: " + id + ", " + warehouses.get(id));
        }
    }

    private void getWarehouseById() throws SQLException, ClassNotFoundException {
        showCancelInteger();
        System.out.println("Please enter warehouse id: ");
        
        int id = sc.nextInt(); 
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        Warehouse warehouse = warehouseService.getWarehouse(id);
        System.out.println(warehouse);
    }

    private void createWarehouse() throws SQLException, ClassNotFoundException {
        showCancelString();
        System.out.println("Please enter name of new warehouse: ");
        
        String name = sc.nextLine();
        
        if (cancelStringOperation(name)) return;
        
        showCancelString();
        System.out.println("Please enter new address of warehouse: ");
        
        String address = sc.nextLine();
        
        if (cancelStringOperation(address)) return;
        
        warehouseService.create(new Warehouse(name, address));
        System.out.println("Created warehouse !");
    }

    private void updateWarehouse() throws SQLException, ClassNotFoundException {
        showCancelInteger();
        System.out.println("Please enter warehouse id: ");
        
        int id = sc.nextInt(); sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        showCancelString();
        System.out.println("Please enter new name of warehouse: ");
        
        String name = sc.nextLine();
        
        if (cancelStringOperation(name)) return;
        
        showCancelString();
        System.out.println("Please enter address of new warehouse: ");
    
        String address = sc.nextLine();
        
        if (cancelStringOperation(address)) return;
        
        warehouseService.update(id, new Warehouse(name, address));
        System.out.println("Updated warehouse !");
    }

    private void deleteWarehouse() throws SQLException {
        showCancelInteger();
        System.out.println("Please enter warehouse id: ");
        
        int id = sc.nextInt(); sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        warehouseService.delete(id);
        System.out.println("Deleted warehouse !");
    }

    private void insertProduct() throws SQLException, ClassNotFoundException {
        printWarehouses();
        showCancelInteger();
        System.out.println("Choose warehouse id from above: ");
        
        int warehouseId = sc.nextInt();
        
        if (cancelIntegerOperation(warehouseId)) return;
        
        printProducts();
        showCancelInteger();
        System.out.println("Choose product id from above: ");
        
        int productId = sc.nextInt();
        
        if (cancelIntegerOperation(productId)) return;
        
        showCancelInteger();
        System.out.println("Enter quantity: ");
        
        int quantity = sc.nextInt();
        
        if (cancelIntegerOperation(quantity)) return;
        
        warehouseService.insertProduct(warehouseId, productId, quantity);
        System.out.println("Stock and product has been inserted !");
    }

    private void updateProductStock() throws SQLException, ClassNotFoundException {
        printWarehouses();
        showCancelInteger();
        System.out.println("Choose warehouse id from above: ");
        
        int warehouseId = sc.nextInt();
        
        if (cancelIntegerOperation(warehouseId)) return;
        
        printProducts();
        showCancelInteger();
        System.out.println("Choose product id from above: ");
        
        int productId = sc.nextInt();
        
        if (cancelIntegerOperation(productId)) return;
        
        showCancelInteger();
        System.out.println("Enter quantity: ");
        
        int quantity = sc.nextInt();
        
        if (cancelIntegerOperation(quantity)) return;
        
        warehouseService.insertProduct(warehouseId, productId, quantity);
        System.out.println("Stock has been updated !");
    }

    private void getProductStock() throws SQLException, ClassNotFoundException {
        printProducts();
        showCancelInteger();
        System.out.println("Enter product id from above: ");
        
        int id = sc.nextInt();
        
        if (cancelIntegerOperation(id)) return;
        
        int stock = warehouseService.getProductStock(id);
        System.out.println("Stock of product: " + stock);
    }

    private void customerCrud() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all customers");
                System.out.println("Enter 2 to get customer by id");
                System.out.println("Enter 3 to exit customers");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                if      (input == 1) printCustomers();
                else if (input == 2) getCustomerById();
                else if (input == 3) break;
                else System.out.println("Invalid option");
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void printCustomers() throws ClassNotFoundException, SQLException {
        Map<Integer, Customer> customers = customerService.getAllCustomers();
        for (Integer id : customers.keySet()) {
            System.out.println("Id: " + id + ", " + customers.get(id));
        }
    }

    private void getCustomerById() throws ClassNotFoundException, SQLException {
        showCancelInteger();
        System.out.println("Enter id: ");

        int id = sc.nextInt();
        
        if (cancelIntegerOperation(id)) return;
        
        Customer customer = customerService.getCustomer(id);
        System.out.println(customer);
    }
}
