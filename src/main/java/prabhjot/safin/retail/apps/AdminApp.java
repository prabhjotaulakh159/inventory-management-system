package prabhjot.safin.retail.apps;

import java.io.Console;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import prabhjot.safin.retail.models.Admin;
import prabhjot.safin.retail.models.audit.Audit;
import prabhjot.safin.retail.models.Category;
import prabhjot.safin.retail.models.Customer;
import prabhjot.safin.retail.models.Product;
import prabhjot.safin.retail.models.Review;
import prabhjot.safin.retail.models.Store;
import prabhjot.safin.retail.models.Warehouse;
import prabhjot.safin.retail.services.audit.AuditTable;

/**
 * Admin version of the application
 * @author Prabhjot Aulakh, Safin Haque
 */
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
                System.out.println("Press 7 for audits");
                System.out.println("Press 8 to exit");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                if      (input == 1) categoryCrud();
                else if (input == 2) productCrud();
                else if (input == 3) reviewCrud();
                else if (input == 4) storeCrud();
                else if (input == 5) warehouseCrud();
                else if (input == 6) customerCrud();
                else if (input == 7) auditCrud();
                else if (input == 8) break;
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

    /**
     * Logs in a admin
     */
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

    /**
     * Provides UI for category options
     */
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

    /**
     * Prints categories on the terminal
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void printCategories() throws ClassNotFoundException, SQLException {
        Map<Integer, Category> categories = categoryService.getCategories();
        for (Integer id : categories.keySet()) { 
            System.out.println("Category Id: " + id + ", " + categories.get(id));
        }
    }

    /**
     * Provides UI to get a category by id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getCategoryById() throws ClassNotFoundException, SQLException {
        showCancelInteger();
        System.out.println("Enter id of category: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        Category category = categoryService.getCategory(id);
        System.out.println(category);
    }

    /**
     * Provides UI to create a category
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void createCategory() throws SQLException, ClassNotFoundException {
        showCancelString();        
        System.out.println("Enter category name: ");
        
        String name = sc.nextLine();
        
        if (cancelStringOperation(name)) return;
        
        categoryService.createCategory(new Category(name));
        System.out.println("Successfully created category !");
    }

    /**
     * Provides UI to update a category
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to delete a category
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to delete for product options
     */
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
    
    /**
     * Provides UI to create a product
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to update a product
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to delete a product
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI for review options
     */
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

    /**
     * Prints all reviews on the terminal
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void printReviews() throws ClassNotFoundException, SQLException {
        Map<Integer, Review> reviews = reviewService.getAllReviews();
        for (Integer id : reviews.keySet()) {
            System.out.println("Review Id: " + id + ", " + reviews.get(id));
        }
    }
    
    /**
     * Provides UI to get a review by id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getReviewById() throws SQLException, ClassNotFoundException {
        showCancelInteger();
        System.out.println("Enter id of review: ");
        
        int id = sc.nextInt();
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        Review review = reviewService.getReview(id);
        System.out.println(review);
    }

    /**
     * Prints flagged reviews on the terminal
     */
    private void printFlaggedReviews() throws ClassNotFoundException, SQLException {
        Map<Integer, Review> reviews = reviewService.getFlaggedReviews();
        for (Integer id : reviews.keySet()) {
            System.out.println("Review Id: " + id + ", " + reviews.get(id));
        }
    }

    /**
     * Deletes flagged reviews in the database
     */
    private void deleteFlaggedReviews() throws SQLException {
        reviewService.deleteFlaggedReviews();
        System.out.println("Deleted flagged reviews !");
    }

    /**
     * Provides UI for store operations
     */
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

    /**
     * Provides UI to create a store
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void createStore() throws SQLException, ClassNotFoundException {
        showCancelString();
        System.out.println("Please enter store name: ");
        
        String storeName = sc.nextLine();
        
        if (cancelStringOperation(storeName)) return;
        
        storeService.createStore(new Store(storeName));
        System.out.println("Store has been created");
    }

    /**
     * Provides UI to update a store
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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

    /**
     * Provides UI to delete a store
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to create a price for a product at a store
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to update a price for a product at a store
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI for warehouse options
     */
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

    /**
     * Prints warehouses on the terminal
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void printWarehouses() throws ClassNotFoundException, SQLException {
        Map<Integer, Warehouse> warehouses = warehouseService.getWarehouses();
        for (Integer id : warehouses.keySet()) {
            System.out.println("Warehouse Id: " + id + ", " + warehouses.get(id));
        }
    }

    /**
     * Provides UI to get a warehouse by id and print it on the terminal
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getWarehouseById() throws SQLException, ClassNotFoundException {
        showCancelInteger();
        System.out.println("Please enter warehouse id: ");
        
        int id = sc.nextInt(); 
        sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        Warehouse warehouse = warehouseService.getWarehouse(id);
        System.out.println(warehouse);
    }

    /**
     * Provides UI to create a warehouse
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to update a warehouse
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to delete a warehouse
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void deleteWarehouse() throws SQLException {
        showCancelInteger();
        System.out.println("Please enter warehouse id: ");
        
        int id = sc.nextInt(); sc.nextLine();
        
        if (cancelIntegerOperation(id)) return;
        
        warehouseService.delete(id);
        System.out.println("Deleted warehouse !");
    }

    /**
     * Provides UI to insert a product and a quantity in a warehouse
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to update the stock of a product at a warehouse
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Provides UI to get the total stock of product
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getProductStock() throws SQLException, ClassNotFoundException {
        printProducts();
        showCancelInteger();
        System.out.println("Enter product id from above: ");
        
        int id = sc.nextInt();
        
        if (cancelIntegerOperation(id)) return;
        
        int stock = warehouseService.getProductStock(id);
        System.out.println("Stock of product: " + stock);
    }

    /**
     * Provides UI for customer options
     */
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

    /**
     * Prints all customers on the terminal
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void printCustomers() throws ClassNotFoundException, SQLException {
        Map<Integer, Customer> customers = customerService.getAllCustomers();
        for (Integer id : customers.keySet()) {
            System.out.println("Id: " + id + ", " + customers.get(id));
        }
    }

    /**
     * Provides UI to get a customer by id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getCustomerById() throws ClassNotFoundException, SQLException {
        showCancelInteger();
        System.out.println("Enter id: ");

        int id = sc.nextInt();
        
        if (cancelIntegerOperation(id)) return;
        
        Customer customer = customerService.getCustomer(id);
        System.out.println(customer);
    }

    /**
     * Provides UI to view audits
     */
    public void auditCrud() {
        while(true) {
            System.out.println("Enter 1 to get all audit logs");
            // TODO: get audits on table on a specific date
            // TODO: get audits on table for a specific id
            System.out.println("Enter 4 to exit audits");
            try {
                int input = sc.nextInt();
                sc.nextLine();
                
                if (input == 1) getAuditLogsOnTable();
                else if (input == 4) break;
                else throw new InputMismatchException();
            
            } catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Not a valid option !");
                sc.next();
            }
        }
    }

    /**
     * Provides UI to get audit logs on a table
     */
    public void getAuditLogsOnTable() throws SQLException {
        showCancelInteger();
        System.out.println("Enter 1 to get admin logs");
        System.out.println("Enter 2 to get customers logs");
        System.out.println("Enter 3 to get categories logs");
        System.out.println("Enter 4 to get warehouses logs");
        System.out.println("Enter 5 to get products logs");
        System.out.println("Enter 6 to get stock update in warehouse logs");
        System.out.println("Enter 7 to get store logs");
        System.out.println("Enter 8 to get store product price update logs");
        System.out.println("Enter 9 to get order logs");
        System.out.println("Enter 10 to get order products logs");
        System.out.println("Enter 11 to get review logs");


        int input = sc.nextInt();

        if (cancelIntegerOperation(input)) return;

        if      (input == 1) printAudit(AuditTable.ADMINS);
        else if (input == 2) printAudit(AuditTable.CUSTOMERS);
        else if (input == 3) printAudit(AuditTable.CATEGORIES);
        else if (input == 4) printAudit(AuditTable.WAREHOUSES);
        else if (input == 5) printAudit(AuditTable.PRODUCTS);
        else if (input == 6) printAudit(AuditTable.PRODUCTS_WAREHOUSES);
        else if (input == 7) printAudit(AuditTable.STORES);
        else if (input == 8) printAudit(AuditTable.PRODUCTS_STORES);
        else if (input == 9) printAudit(AuditTable.ORDERS);
        else if (input == 10) printAudit(AuditTable.ORDERS_PRODUCTS); 
        else if (input == 11) printAudit(AuditTable.REVIEWS);
        else throw new InputMismatchException(); 

    }

    /**
     * Prints all audits on the terminal
     * @throws SQLException
     */
    public void printAudit(AuditTable table) throws SQLException {
        List<Audit> logs = auditService.getListOfAuditWanted(table);
        for (Audit log : logs) {
            System.out.println(log);
        }
    }



    
    public void getAuditByDate() throws SQLException{

    }
}
