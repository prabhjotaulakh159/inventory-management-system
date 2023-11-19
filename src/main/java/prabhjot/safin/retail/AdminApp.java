package prabhjot.safin.retail;

import java.io.Console;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Admin;
import prabhjot.safin.retail.models.Category;
import prabhjot.safin.retail.models.Product;
import prabhjot.safin.retail.models.Review;
import prabhjot.safin.retail.models.Store;
import prabhjot.safin.retail.services.AdminService;
import prabhjot.safin.retail.services.CategoryService;
import prabhjot.safin.retail.services.ProductService;
import prabhjot.safin.retail.services.ReviewService;
import prabhjot.safin.retail.services.StoreService;
// import prabhjot.safin.retail.services.WarehouseService;

public class AdminApp {
    private static Scanner sc = new Scanner(System.in);
    private static ConnectionProvider connectionProvider;
    public static void main(String[] args) {
        try {
            connectionProvider = new ConnectionProvider();
            AdminService adminService = new AdminService(connectionProvider.getConnection());
            CategoryService categoryService = new CategoryService(connectionProvider.getConnection());
            ProductService productService = new ProductService(connectionProvider.getConnection());
            ReviewService reviewService = new ReviewService(connectionProvider.getConnection());
            StoreService storeService = new StoreService(connectionProvider.getConnection());
            // WarehouseService warehouseService = new WarehouseService(connectionProvider.getConnection());

            login(adminService);

            while (true) {
                System.out.println("Here are your options: ");
                System.out.println("Press 1 for categories");
                System.out.println("Press 2 for products");
                System.out.println("Press 3 for reviews");
                System.out.println("Press 4 for stores");
                System.out.println("Press 5 to exit");
                
                int input = sc.nextInt();

                if (input == 1) {
                    categoryCrud(categoryService);
                } else if (input == 2) {
                    productCrud(productService, categoryService);
                } else if (input == 3) {
                    reviewCrud(reviewService);
                } else if (input == 4) {
                    storeCrud(storeService);
                } else if (input == 5) {
                    break;
                } else {
                    System.out.println("Not a valid option !");
                }
            }

            System.out.println("Goodbye !");

            connectionProvider.kill();
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
                e.printStackTrace();
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
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
                    printCategories(categories);
                } else if (input == 2) {
                    System.out.println("Enter id of category: ");
                    int id = sc.nextInt();
                    Category category = categoryService.getCategory(id);
                    System.out.println("Category: " + category.getCategory());
                } else if (input == 3) {
                    System.out.println("Enter category name: ");
                    String name = sc.next();
                    categoryService.createCategory(new Category(name));
                    System.out.println("Successfully created category !");
                } else if (input == 4) {
                    printCategories(categoryService.getCategories());
                    System.out.println("Enter id of category from above to update: ");
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
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printCategories(Map<Integer, Category> categories) {
        for (Integer id : categories.keySet()) { 
            String categoryName = categories.get(id).getCategory();
            System.out.println("Category Id: " + id + ", Category: " + categoryName);
        }
    }

    private static void productCrud(ProductService productService, CategoryService categoryService) {
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
                if (input == 1) {
                    Map<Integer, Product> products = productService.getProducts();
                    printProducts(products, categoryService);
                } else if (input == 2) {
                    System.out.println("Enter id of product: ");
                    int id = sc.nextInt();
                    Product product = productService.getProduct(id);
                    System.out.println("Product name: " + product.getName() + ", Category: " + categoryService.getCategory(product.getCategory_id()).getCategory());
                } else if (input == 3) {
                    System.out.println("Enter name of the product: ");
                    String productName = sc.next();
                    printCategories(categoryService.getCategories());
                    System.out.println("Select a category id from above: ");
                    int categoryId = sc.nextInt();
                    productService.createProduct(new Product(productName, categoryId));
                    System.out.println("Product created !");
                } else if (input == 4) {
                    printProducts(productService.getProducts(), categoryService);
                    System.out.println("Enter product id from above to update: ");
                    int productId = sc.nextInt();
                    System.out.println("Enter new name of new product: ");
                    String productName = sc.next();
                    printCategories(categoryService.getCategories());
                    System.out.println("Select the new category id from above: ");
                    int categoryId = sc.nextInt();
                    productService.updateProduct(productId, new Product(productName, categoryId));
                    System.out.println("Updated product !");
                } else if (input == 5) {
                    printProducts(productService.getProducts(), categoryService);
                    System.out.println("Enter product id to delete: ");
                    int id = sc.nextInt();
                    productService.deleteProduct(id);
                    System.out.println("Product deleted !");
                } else if (input == 6) {
                    break;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printProducts(Map<Integer, Product> products, CategoryService categoryService) throws SQLException, ClassNotFoundException {
        for (Integer id : products.keySet()) {
            String product = products.get(id).getName();
            Category category = categoryService.getCategory(products.get(id).getCategory_id());
            System.out.println("Product Id: " + id + ", Product Name: " + product + ", Category: " + category.getCategory());
        }
    }

    private static void reviewCrud(ReviewService reviewService) {
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
                if (input == 1) {
                    Map<Integer, Review> reviews = reviewService.getAllReviews();
                    printReviews(reviews);
                } else if (input == 2) {
                    System.out.println("Enter id of review: ");
                    int id = sc.nextInt();
                    Review review = reviewService.getReview(id);
                    System.out.println("Customer Id: " + review.getCustomerId() + ", Product Id: " + review.getProductId() + ", Flags: " + review.getFlags() + ", Rating: " + review.getRating() + ", Description: " + review.getDescription());
                } else if (input == 3) {
                    Map<Integer, Review> flagged = reviewService.getFlaggedReviews();
                    printReviews(flagged);
                } else if (input == 4) {
                    reviewService.deleteFlaggedReviews();
                    System.out.println("Deleted flagged reviews !");
                } else if (input == 5) {
                    printProducts(new ProductService(connectionProvider.getConnection()).getProducts(), new CategoryService(connectionProvider.getConnection()));
                    System.out.println("Enter product id to get reviews on: ");
                    int id = sc.nextInt();
                    Map<Integer, Review> reviews = reviewService.getReviewForProduct(id);
                    printReviews(reviews);
                } else if (input == 6) {
                    break;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printReviews(Map<Integer, Review> reviews) {
        for (Integer id : reviews.keySet()) {
            int customerId = reviews.get(id).getCustomerId();
            int productId = reviews.get(id).getProductId();
            int flags = reviews.get(id).getFlags();
            int rating = reviews.get(id).getRating();
            String description = reviews.get(id).getDescription();
            System.out.println("Review Id: " + id + ", Customer Id : " + customerId + ", Product Id: " + productId + ", Flags: " + flags + ", Rating: " + rating + ", Description: " + description);
        }
    }

    private static void storeCrud(StoreService storeService) {
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
                System.out.println("Enter 8 to get all prices of a product at different stores");
                System.out.println("Enter 9 to exit stores");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                if (input == 1) {
                    Map<Integer, Store> stores = storeService.getStores();
                    printStores(stores);
                } else if (input == 2) {
                    System.out.println("Enter store id: ");
                    int id = sc.nextInt();
                    Store store = storeService.getStore(id);
                    System.out.println("Store name: " + store.getName());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printStores(Map<Integer, Store> stores) {
        for (Integer id : stores.keySet()) {
            System.out.println("Store Id: " + id + ", Store Name: " + stores.get(id).getName());
        }
    }
}
