package prabhjot.safin.retail.apps;

import java.io.Console;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import prabhjot.safin.retail.models.Admin;
import prabhjot.safin.retail.models.Audit;
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
    @Override
    public void run() {
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
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (input == 1) {
                    categoryCrud(); 
                } else if (input == 2) {
                    productCrud(); 
                } else if (input == 3) {
                    reviewCrud(); 
                } else if (input == 4) {
                    storeCrud(); 
                } else if (input == 5) {
                    warehouseCrud(); 
                } else if (input == 6) {
                    customerCrud(); 
                } else if (input == 7) {
                    auditCrud(); 
                } else if (input == 8) {
                    break; 
                } else {
                    throw new NumberFormatException(); 
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        } 
        System.out.println("Goodbye !");
        try {
            this.connectionProvider.kill();
        } catch (SQLException e) {
            this.handleSQLException(e);
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
                int id = Integer.parseInt(this.sc.nextLine());
                System.out.println("Enter your password: ");
                String password = "";
                char[] passwordInput = console.readPassword();
                for (char c : passwordInput) {
                    password += c;
                }
                Admin admin = new Admin(id, password);
                int status = adminService.login(admin);
                if (status == 0) {
                    System.out.println("Hello admin #" + admin.getId());
                    break;
                } else {
                    System.out.println("Login failed !");
                }
            } catch (SQLException e) {
                handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
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
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to get all categories");
            System.out.println("Enter 2 to get a category by id");
            System.out.println("Enter 3 to create a category");
            System.out.println("Enter 4 to update a category by id");
            System.out.println("Enter 5 to delete a category by id");
            System.out.println("Enter 6 to exit categories");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (input == 1) {
                    printCategories();
                } else if (input == 2) {
                    getCategoryById();
                } else if (input == 3) {
                    createCategory();
                } else if (input == 4) {
                    updateCategory();
                } else if (input == 5) {
                    deleteCategory();
                } else if (input == 6) {
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } 
        }
    }

    /**
     * Prints categories on the terminal
     */
    private void printCategories() {
        try {
            Map<Integer, Category> categories = this.categoryService.getCategories();
            for (Integer id : categories.keySet()) { 
                System.out.println("Category Id: " + id + ", " + categories.get(id));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides UI to get a category by id
     */
    private void getCategoryById() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Enter id of category: ");
                int id = Integer.parseInt(this.sc.nextLine());                
                if (cancelIntegerOperation(id)) {
                    return;
                }
                Category category = this.categoryService.getCategory(id);
                System.out.println(category);
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
     * Provides UI to create a category
     */
    private void createCategory() {
        try {
            this.showCancelString();        
            System.out.println("Enter category name: ");
            String name = sc.nextLine();
            if (cancelStringOperation(name)) { 
                return;
            }
            this.categoryService.createCategory(new Category(name));
            System.out.println("Successfully created category !");
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides UI to update a category
     */
    private void updateCategory() {
        while (true) {
            try {
                this.printCategories();
                this.showCancelInteger();
                System.out.println("Enter id of category from above to update: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                this.showCancelString();
                System.out.println("Enter new category name: ");
                String name = sc.nextLine();
                if (cancelStringOperation(name)) { 
                    return;
                }
                this.categoryService.updateCategory(id, name);
                System.out.println("Successfully updated category !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to delete a category
     */
    private void deleteCategory() {
        while (true) {
            try {
                this.printCategories();
                this.showCancelInteger();
                System.out.println("Enter id of category from above to delete: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(id)) {
                    return;
                }
                this.categoryService.deleteCategory(id);
                System.out.println("Successfully deleted category");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to delete for product options
     */
    private void productCrud() {
        while (true) {
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to get all products");
            System.out.println("Enter 2 to get a product by id");
            System.out.println("Enter 3 to create a product");
            System.out.println("Enter 4 to update a product by id");
            System.out.println("Enter 5 to delete a product by id");
            System.out.println("Enter 6 to exit products");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (input == 1) {
                    printProducts();
                } else if (input == 2) {
                    getProductById();
                } else if (input == 3) {
                    createProduct();
                } else if (input == 4) {
                    updateProduct();
                } else if (input == 5) {
                    deleteProduct();
                } else if (input == 6) {
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } 
        }
    }
    
    /**
     * Provides UI to create a product
     */
    private void createProduct() {
         while (true) {
            try {
                this.showCancelString();
                System.out.println("Enter name of the product: ");
                String productName = sc.nextLine();
                if (cancelStringOperation(productName)) {
                    return;
                }
                this.printCategories();
                this.showCancelInteger();
                System.out.println("Select a category id from above: ");
                int categoryId = Integer.parseInt(this.sc.nextLine());                
                if (cancelIntegerOperation(categoryId)) { 
                    return;
                }
                this.productService.createProduct(new Product(productName, categoryId));
                System.out.println("Product created !");
                break;
            } catch (SQLException e) {
                handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Provides UI to update a product
     */
    private void updateProduct() {
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Enter product id from above to update: ");                
                int productId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(productId)) {
                    return;
                }
                this.showCancelString();
                System.out.println("Enter new name of new product: ");
                String productName = sc.nextLine();
                if (cancelStringOperation(productName)) {
                    return;
                }
                this.printCategories();
                this.showCancelInteger();
                System.out.println("Select the new category id from above: ");                
                int categoryId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(categoryId)) {
                    return;
                }
                this.productService.updateProduct(productId, new Product(productName, categoryId));
                System.out.println("Updated product !");
                break;
            } catch (SQLException e) {
                handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Provides UI to delete a product
     */
    private void deleteProduct() {
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Enter product id from above to delete: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) { 
                    return;
                }
                this.productService.deleteProduct(id);
                System.out.println("Product deleted !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI for review options
     */
    private void reviewCrud() {
        while (true) {
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to get all reviews");
            System.out.println("Enter 2 to get a review by id");
            System.out.println("Enter 3 to get all flagged reviews");
            System.out.println("Enter 4 to delete all flagged reviews");
            System.out.println("Enter 5 to get reviews for a product by id");
            System.out.println("Enter 6 to exit reviews");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (input == 1) {
                    this.printReviews();
                } else if (input == 2) {
                    this.getReviewById();
                } else if (input == 3) {
                    this.printFlaggedReviews();
                } else if (input == 4) {
                    this.deleteFlaggedReviews();
                } else if (input == 5) {
                    this.getReviewForProduct();
                } else if (input == 6) {
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } 
        }
    }

    /**
     * Prints all reviews on the terminal
     */
    private void printReviews() {
        try {
            Map<Integer, Review> reviews = reviewService.getAllReviews();
            for (Integer id : reviews.keySet()) {
                System.out.println("Review Id: " + id + ", " + reviews.get(id));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Provides UI to get a review by id
     */
    private void getReviewById() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Enter id of review: ");    
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                Review review = this.reviewService.getReview(id);
                System.out.println(review);
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints flagged reviews on the terminal
     */
    private void printFlaggedReviews() {
        try {
            Map<Integer, Review> reviews = this.reviewService.getFlaggedReviews();
            for (Integer id : reviews.keySet()) {
                System.out.println("Review Id: " + id + ", " + reviews.get(id));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        }
    }


    /**
     * Deletes flagged reviews in the database
     */
    private void deleteFlaggedReviews() {
        try {
            reviewService.deleteFlaggedReviews();
            System.out.println("Deleted flagged reviews !");
        } catch (SQLException e) {
            this.handleSQLException(e);
        }
    }

    /**
     * Provides UI for store operations
     */
    private void storeCrud() {
        while (true) {
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
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (input == 1) {
                    this.printStores();
                } else if (input == 2) {
                    this.getStoreById();
                } else if (input == 3) {
                    this.createStore();
                } else if (input == 4) {
                    this.updateStore();
                } else if (input == 5) {
                    this.deleteStore();
                } else if (input == 6) {
                    this.createPrice();
                } else if (input == 7) {
                    this.updatePrice();
                } else if (input == 8) {
                   this.getPriceAtStore();
                } else if (input == 9) {
                    break;
                } else {
                    throw new NumberFormatException();
                }                
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } 
        }
    }

    /**
     * Provides UI to create a store
     */
    private void createStore() {
        try {
            this.showCancelString();
            System.out.println("Please enter store name: ");
            String storeName = sc.nextLine();
            if (cancelStringOperation(storeName)) {
                return;
            }
            this.storeService.createStore(new Store(storeName));
            System.out.println("Store has been created");
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        }
    }


    /**
     * Provides UI to update a store
     */
    private void updateStore() {
        while (true) {
            try {
                this.printStores();
                this.showCancelInteger();
                System.out.println("Please enter store id to update: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) { 
                    return;
                }
                this.showCancelString();
                System.out.println("Please enter new store name: ");
                String storeName = sc.nextLine();
                if (cancelStringOperation(storeName)) { 
                    return;
                }
                this.storeService.updateStore(id, storeName);
                System.out.println("Store has been updated");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to delete a store
     */
    private void deleteStore() {
        while (true) {
            try {
                this.printStores();
                this.showCancelInteger();
                System.out.println("Please enter store id to delete: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                this.storeService.deleteStore(id);
                System.out.println("Store has been deleted");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to create a price for a product at a store
     */
    private void createPrice() {
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Please choose product id from above: ");
                int productId = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                this.printStores();
                this.showCancelInteger();
                System.out.println("Please choose store id from above: ");
                int storeId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(storeId)) {
                    return;
                }
                showCancelInteger();
                System.out.println("Please enter a price: ");
                int price = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(price)) { 
                    return;
                }
                this.storeService.createPrice(storeId, productId, price);
                System.out.println("Price has been inserted !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to update a price for a product at a store
     */
    private void updatePrice() {
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Please choose product id from above: ");
                int productId = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                this.printStores();
                this.showCancelInteger();
                System.out.println("Please choose store id from above: ");
                int storeId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(storeId)){ 
                    return;
                }
                this.showCancelInteger();
                System.out.println("Please enter a new price: ");
                int price = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(price)) {
                    return;
                }
                this.storeService.createPrice(storeId, productId, price);
                System.out.println("Price has been updated !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI for warehouse options
     */
    private void warehouseCrud() {
        while (true) {
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
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (input == 1) {
                    this.printWarehouses();
                } else if (input == 2) {
                    this.getWarehouseById();
                } else if (input == 3) {
                    this.createWarehouse();
                } else if (input == 4) {
                    this.updateWarehouse();
                } else if (input == 5) {
                    this.deleteWarehouse();
                } else if (input == 6) {
                    this.insertProduct();
                } else if (input == 7) {
                    this.updateProductStock();
                } else if (input == 8) {
                    this.getProductStock();
                } else if (input == 9) {
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Prints warehouses on the terminal
     */
    private void printWarehouses() {
        try {
            Map<Integer, Warehouse> warehouses = this.warehouseService.getWarehouses();
            for (Integer id : warehouses.keySet()) {
                System.out.println("Warehouse Id: " + id + ", " + warehouses.get(id));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides UI to get a warehouse by id and print it on the terminal
     */
    private void getWarehouseById() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Please enter warehouse id: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                Warehouse warehouse = this.warehouseService.getWarehouse(id);
                System.out.println(warehouse);
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
     * Provides UI to create a warehouse
     */
    private void createWarehouse() {
        while (true) {
            try {
                this.showCancelString();
                System.out.println("Please enter name of new warehouse: ");
                String name = sc.nextLine();
                if (cancelStringOperation(name)) {
                    return;
                }
                this.showCancelString();
                System.out.println("Please enter new address of warehouse: ");
                String address = sc.nextLine();
                if (cancelStringOperation(address)) {
                    return;
                }
                this.warehouseService.create(new Warehouse(name, address));
                System.out.println("Created warehouse !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Provides UI to update a warehouse
     */
    private void updateWarehouse() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Please enter warehouse id: ");                
                int id = Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                this.showCancelString();
                System.out.println("Please enter new name of warehouse: ");
                String name = sc.nextLine();
                if (cancelStringOperation(name)) { 
                    return;
                }
                this.showCancelString();
                System.out.println("Please enter address of new warehouse: ");
                String address = sc.nextLine();
                if (cancelStringOperation(address)) {
                    return;
                }
                this.warehouseService.update(id, new Warehouse(name, address));
                System.out.println("Updated warehouse !");
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
     * Provides UI to delete a warehouse
     */
    private void deleteWarehouse() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Please enter warehouse id: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                this.warehouseService.delete(id);
                System.out.println("Deleted warehouse !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to insert a product and a quantity in a warehouse
     */
    private void insertProduct() {
        while (true) {
            try {
                this.printWarehouses();
                this.showCancelInteger();
                System.out.println("Choose warehouse id from above: ");
                int warehouseId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(warehouseId)) {
                    return;
                }
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Choose product id from above: ");
                int productId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(productId)) {
                    return;
                }
                this.showCancelInteger();
                System.out.println("Enter quantity: ");
                int quantity = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(quantity)) {
                    return;
                }
                this.warehouseService.insertProduct(warehouseId, productId, quantity);
                System.out.println("Stock and product has been inserted !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to update the stock of a product at a warehouse
     */
    private void updateProductStock() {
        while (true) {
            try {
                this.printWarehouses();
                this.showCancelInteger();
                System.out.println("Choose warehouse id from above: ");
                int warehouseId = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(warehouseId)) {
                    return;
                }
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Choose product id from above: ");
                int productId = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                this.showCancelInteger();
                System.out.println("Enter quantity: ");
                int quantity = Integer.parseInt(this.sc.nextLine());
                if (this.cancelIntegerOperation(quantity)) {
                    return;
                }
                warehouseService.insertProduct(warehouseId, productId, quantity);
                System.out.println("Stock has been updated !");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to get the total stock of product
     */
    private void getProductStock() {
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Enter product id from above: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                int stock = warehouseService.getProductStock(id);
                System.out.println("Stock of product: " + stock);
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI for customer options
     */
    private void customerCrud() {
        while (true) {
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to get all customers");
            System.out.println("Enter 2 to get customer by id");
            System.out.println("Enter 3 to exit customers");
            System.out.println("--------------------------------------");
            try {
                int input = sc.nextInt(); sc.nextLine();
                if (input == 1) {
                    this.printCustomers();
                } else if (input == 2) {
                    this.getCustomerById();
                } else if (input == 3) {
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Prints all customers on the terminal
     */
    private void printCustomers() {
        try {
            Map<Integer, Customer> customers;
            customers = customerService.getAllCustomers();
            for (Integer id : customers.keySet()) {
                System.out.println("Id: " + id + ", " + customers.get(id));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides UI to get a customer by id
     */
    private void getCustomerById() {
        while (true) {
            try {
                this.showCancelInteger();
                System.out.println("Enter id: ");
                int id = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(id)) {
                    return;
                }
                Customer customer = this.customerService.getCustomer(id);
                System.out.println(customer);
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
     * Provides UI to view audits
     */
    public void auditCrud() {
        while(true) {
            this.showCancelInteger();
            System.out.println("Enter 1 to get all audit logs");
            System.out.println("Enter 2 to get an audit by id");
            System.out.println("Enter 3 to exit audits");
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                 if (cancelIntegerOperation(input)) {
                    return;
                }
                if (input == 1)  {
                    this.getAuditLogsOnTable();
                } else if(input == 2) {
                    this.getAuditById();
                } else if (input == 3)  {
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to get audit logs on a table
     */
    public void getAuditLogsOnTable() {
        while (true) {
            System.out.println("--------------------------------------");
            this.showCancelInteger();
            System.out.println("Enter 1 to get admin logs");
            System.out.println("Enter 2 to get customers logs");
            System.out.println("Enter 3 to get categories logs");
            System.out.println("Enter 4 to get warehouses logs");
            System.out.println("Enter 5 to get products logs");
            System.out.println("Enter 6 to get store logs");
            System.out.println("Enter 7 to get order logs");
            System.out.println("Enter 8 to get review logs");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(input)) {
                    return;
                }
                if (input == 1) { 
                    this.printAudit(AuditTable.ADMINS);
                } else if (input == 2) { 
                    this.printAudit(AuditTable.CUSTOMERS);
                } else if (input == 3) { 
                    this.printAudit(AuditTable.CATEGORIES);
                } else if (input == 4) { 
                    this.printAudit(AuditTable.WAREHOUSES);
                } else if (input == 5) { 
                    this.printAudit(AuditTable.PRODUCTS);
                } else if (input == 6) { 
                    this.printAudit(AuditTable.STORES);
                } else if (input == 7) { 
                    this.printAudit(AuditTable.ORDERS);
                } else if (input == 8) { 
                    this.printAudit(AuditTable.REVIEWS);
                } else {
                    throw new NumberFormatException();
                } 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints all audits on the terminal
     */
    public void printAudit(AuditTable table) {
        try {
            List<Audit> logs = auditService.getListOfAuditWanted(table);
            for (Audit log : logs) {
                System.out.println(log);
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        }
    }



    /**
     * Provides UI to get a audit by id
     */
    public void getAuditById() {
        while (true) {
            System.out.println("--------------------------------------");
            this.showCancelInteger();
            System.out.println("Enter 1 to choose admin logs by Id");
            System.out.println("Enter 2 to choose customers logs by Id");
            System.out.println("Enter 3 to choose categories logs by Id");
            System.out.println("Enter 4 to choose warehouses logs by Id");
            System.out.println("Enter 5 to choose products logs by Id");
            System.out.println("Enter 6 to choose store logs by Id");
            System.out.println("Enter 7 to choose order logs by Id");
            System.out.println("Enter 8 to choose review logs by Id");
            System.out.println("--------------------------------------");
            try {
                int logsChosen = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(logsChosen)) {
                    return;
                }
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("enter the Id you want to see a list of :");
                System.out.println("--------------------------------------");
                int idChosen = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(idChosen)) {
                    return;
                }
                List<Audit> audits = new ArrayList<>();
                if (logsChosen == 1) { 
                    audits = auditService.getAuditById(AuditTable.ADMINS, idChosen);
                } else if(logsChosen == 2) { 
                    audits = auditService.getAuditById(AuditTable.CUSTOMERS, idChosen);
                } else if(logsChosen == 3) { 
                    audits = auditService.getAuditById(AuditTable.CATEGORIES, idChosen);
                } else if(logsChosen == 4) { 
                    audits = auditService.getAuditById(AuditTable.WAREHOUSES, idChosen);
                } else if(logsChosen == 5) { 
                    audits = auditService.getAuditById(AuditTable.PRODUCTS, idChosen);
                } else if(logsChosen == 6) { 
                    audits = auditService.getAuditById(AuditTable.STORES, idChosen);
                } else if(logsChosen == 7){ 
                    audits=auditService.getAuditById(AuditTable.ORDERS, idChosen);
                } else if(logsChosen == 8) { 
                    audits=auditService.getAuditById(AuditTable.REVIEWS, idChosen);
                } else { 
                    throw new NumberFormatException();
                } 
                System.out.println();
                System.out.println("--------------------------------------");
                printAudit(audits);
                System.out.println("--------------------------------------");
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } catch (SQLException e) {
                this.handleSQLException(e);
            }
        }
    }

    /**
     * Prints a list of audits on the terminal
     * @param audits List of audits
     */
    public void printAudit(List<Audit> audits) {
        for (Audit log : audits) {
            System.out.println(log);
        }
    }
}
