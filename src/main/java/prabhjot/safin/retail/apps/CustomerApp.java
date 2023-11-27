package prabhjot.safin.retail.apps;

import java.io.Console;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prabhjot.safin.retail.models.Customer;
import prabhjot.safin.retail.models.Order;
import prabhjot.safin.retail.models.Review;

/**
 * Customer version of the application
 * @author Prabhjot Aulakh, Safin Haque
 */
public class CustomerApp extends Application {
    private int id;
    private Customer current;
    
    @Override
    public void run(){
        this.login();
        while(true) {
            System.out.println("--------------------------------------");
            System.out.println("Here are your options: ");
            System.out.println("Press 1 for Products");
            System.out.println("Press 2 for Orders");
            System.out.println("Press 3 for Placing Reviews");
            System.out.println("Press 4 to update your information");
            System.out.println("Press 5 to exit");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(sc.nextLine());
                if (input == 1) { 
                    this.productCrud();
                } else if (input == 2) {
                    this.ordersOptions();
                } else if (input == 3) {
                    this.reviewsOptions();
                } else if (input == 4) {
                    this.updateCustomer();
                } else if (input == 5) {
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
            connectionProvider.kill();
        } catch (SQLException e) {
            this.handleSQLException(e);
        }
    }
        
    /**
     * Logs in a customer
     */
    private void login() {
        while (true) {
            try {
                Console console = System.console();
                System.out.println("Enter your customer ID: ");
                this.id = Integer.parseInt(sc.nextLine());
                System.out.println("Enter your password: ");
                String password = "";
                char[] passwordInput = console.readPassword();
                for (char c : passwordInput) { 
                    password += c;
                }
                this.current = this.customerService.login(id, password);
                if (this.current != null) {
                    System.out.println("Welcome " + this.current.getFirstname() + " " + this.current.getLastname() + "!");
                    break;
                } else {
                    System.out.println("Invalid Login");
                }
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Provides UI for product related operations
     */
    private void productCrud() {
        while (true) {
            System.out.println("PRODUCTS");
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to get all products");
            System.out.println("Enter 2 to get a product by id");
            System.out.println("Enter 3 to get the Reviews of a product");
            System.out.println("Enter 4 to get product price at store");
            System.out.println("Enter 5 to exit products");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(sc.nextLine()); 
                if (input == 1) {
                   this.printProducts();
                } else if (input == 2) {
                    this.getProductById();
                } else if (input == 3) {
                    this.getReviewOfProduct();
                } else if (input == 4) {
                    this.getPriceAtStore();
                } else if (input == 5) {
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
     * Prompts users to enter a product id and get a reviews on it
     */
    private void getReviewOfProduct() {
        while (true) {
            try {
                this.printProducts();
                System.out.println();
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Type the product id you want to see reviews from:");
                System.out.println("--------------------------------------");
                int productId = Integer.parseInt(sc.nextLine());             
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                Map<Integer, Review> reviews = reviewService.getReviewForProduct(productId);
                System.out.println("Reviews: ");
                System.out.println("--------------------------------------");
                for (Integer id: reviews.keySet()) {
                    System.out.println("Review ID: " + id + " | " + reviews.get(id));
                }
                System.out.println("--------------------------------------");
                System.out.println();
                System.out.println("--------------------------------------");
                System.out.println("Enter Y to flag a review, else press anything");
                System.out.println("--------------------------------------");
                String answer = sc.nextLine().toLowerCase();
                if (answer.equals("y")) {
                    this.flagReview();
                }
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
     * Provides UI for options on orders
     */
    private void ordersOptions(){
        while (true) {
            System.out.println("ORDERS");
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to Create an Order");
            System.out.println("Enter 2 to Delete your Order");
            System.out.println("Enter 3 to get Details of Order");
            System.out.println("Enter 4 to View all your orders");
            System.out.println("Enter 5 to Exit Orders");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(sc.nextLine());
                if (input == 1)  {
                    this.createOrder(); 
                } else if (input == 2) {
                    this.deleteOrder();
                } else if (input == 3) {
                    this.getOrderDetails();
                } else if (input == 4) {
                    this.viewAllOrders();
                } else if (input == 5) {
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
     * Provides UI to create an order
     */
    private void createOrder(){
    try{
        int storeid = 0;
        while (true) {
            try {
                List<Integer> stores = new ArrayList<Integer>(storeService.getStores().keySet());
                this.printStores();
                this.showCancelInteger();
                System.out.println("Which store do you want to buy from? Enter the store Id:");
                storeid = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(storeid)) {
                    return;
                }
                if (!stores.contains(storeid)) {
                    throw new NumberFormatException();   
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        } 
        Order order = new Order(id, storeid, new Date(System.currentTimeMillis()));
        Map<Integer, Integer> products = new HashMap<>();
        while (true) {
            try {
                List<Integer> productIds = new ArrayList<Integer>(this.productService.getProducts().keySet());
                System.out.println("--------------------------------------");
                this.printProducts();
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Which product you want to buy? Enter its ID:");
                System.out.println("--------------------------------------");
                int productId = Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(productId)) {
                    return;
                }
                if (!productIds.contains(productId)) {
                    throw new NumberFormatException();   
                }
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("How many would you like to buy?");
                System.out.println("--------------------------------------");
                int quantity =  Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(quantity)) {
                    return;
                }
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
                products.put(productId, quantity);
                System.out.println("--------------------------------------");
                System.out.println("Enter Y to stop picking products, else press anything");
                System.out.println("--------------------------------------");
                String ans = sc.nextLine().toUpperCase();
                if (ans.equals("Y")) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } 
        }
        int orderId = this.orderService.createOrder(order, products);
        System.out.println("Here is your order id: " + orderId);
    }catch (SQLException e) {
        this.handleSQLException(e);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (NumberFormatException e) {
        System.out.println("Please enter valid data");
    }
}

    /**
     * Provides UI to delete an order
     */
    private void deleteOrder() {
        while (true) {
            try {
                Map<Integer, Order> orders = this.orderService.getOrdersByCustomer(id);
                List<Integer> orderIds = new ArrayList<>(orders.keySet());
                System.out.println("--------------------------------------");
                for (Integer id : orders.keySet()) {
                    System.out.println("Order Id: " + id + ", " + orders.get(id));
                }
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Choose which Order you want to delete by id");
                System.out.println("--------------------------------------");
                int id = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(id)) {
                    return;
                }
                if (!orderIds.contains(id)) {
                    throw new NumberFormatException();
                }
                this.orderService.deleteOrder(id, this.id);
                System.out.println("--------------------------------------");
                System.out.println("Order has been deleted!");
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
     * Provides UI to get the details of an order
     */
    private void getOrderDetails() {
        while (true) {
            try {
                Map<Integer, Order> orders = this.orderService.getOrdersByCustomer(id);
                for(Integer orderId : orders.keySet()){
                    System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
                }
                this.showCancelInteger();
                System.out.println("Choose order to get details on: ");
                int orderId = Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(orderId)) { 
                    return;
                }
                Map<String, Integer> details = orderService.getOrderDetails(orderId, this.id);
                if (details.size() == 0) {
                    throw new NumberFormatException();
                }
                for (String name : details.keySet()) {
                    System.out.println("Product: " + name + ", Quantity: " + details.get(name));
                }
                System.out.println("Total: " + orderService.getOrderTotal(orderId) + "$");
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
     * Provides UI for view all orders for the logged in customer
     */
    public void viewAllOrders() {
        try {
            Map<Integer, Order> orders = this.orderService.getOrdersByCustomer(this.id);
            System.out.println("Here Are your Orders!");
            System.out.println("--------------------------------------");
            for (Integer orderId : orders.keySet()) {
                System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }   
        System.out.println("--------------------------------------");
    }

    /**
     * Provides UI for review options
     */
    private void reviewsOptions(){
        while (true) {
            System.out.println("REVIEWS");
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to make a Review");
            System.out.println("Enter 2 to delete a Review you made");
            System.out.println("Enter 3 to update a Review");
            System.out.println("Enter 4 to exit Review");
            System.out.println("--------------------------------------");
            try {
                int input = Integer.parseInt(sc.nextLine());
                if (input == 1) {
                    this.createReview();  
                } else if(input == 2) {
                    this.deleteReview();
                } else if(input == 3) { 
                    this.updateReview();
                } else if(input == 4) { 
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
     * Provides UI to create a review 
     */
    private void createReview() {
        while (true) {
            try {
                this.printProducts();
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Choose which product you want to review, enter id:");
                System.out.println("--------------------------------------");
                int productId = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                System.out.println("--------------------------------------");
                this.showCancelInteger();
                System.out.println("Enter your rating, it can be between 1 and 5");
                System.out.println("--------------------------------------");
                int rating = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(rating)) {
                    return;
                }
                System.out.println("--------------------------------------");
                this.showCancelString();
                System.out.println("Give us a brief description on what you like/dislike:");
                System.out.println("--------------------------------------");
                String description = sc.nextLine();
                if (this.cancelStringOperation(description)) {
                    return;
                }
                Review review = new Review(id, productId, 0, rating, description);
                reviewService.create(review);
                System.out.println("--------------------------------------");
                System.out.println("Review Created!");
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
     * Provides UI to delete a review
     */
    private void deleteReview() {
        while (true) {
            try {
                Map<Integer, Review> reviews = reviewService.getReviewsForCustomer(id);
                List<Integer> reviewIds = new ArrayList<>(reviews.keySet());
                for (Integer reviewId : reviews.keySet()) {
                    System.out.println("Review Id: " + reviewId + ", " + reviews.get(reviewId));
                }
                showCancelInteger();
                System.out.println("Enter the Review Id you want to delete");
                int reviewId = Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(reviewId)) {
                    return;
                }
                if (!reviewIds.contains(reviewId)) {
                    throw new NumberFormatException();
                }
                reviewService.delete(reviewId);
                System.out.println("Review Removed!");
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
     * Provides UI to flag a review
     */
    private void flagReview() {
        while (true) {
            try {
                System.out.println("--------------------------------------");
                showCancelInteger();
                System.out.println("Which review Id do you want to flag?");
                System.out.println("--------------------------------------");
                int review = Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(review)) {
                    return;
                }
                this.reviewService.flagReview(review);
                System.out.println("--------------------------------------");
                System.out.println("Review has been flagged");
                System.out.println("--------------------------------------");
                break;
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            }
        }
    }

    /**
     * Provides UI to update a review
     */
    private void updateReview() {
        while (true) {
            try {
                Map<Integer, Review> reviews = reviewService.getReviewsForCustomer(id);
                List<Integer> reviewIds = new ArrayList<>(reviews.keySet());
                for(Integer reviewId : reviews.keySet()){
                    System.out.println("Review ID: " + reviewId + " | " + reviews.get(reviewId));
                }
                showCancelInteger();
                System.out.println("Which Review you want to Update? Enter their Id:");
                int reviewId = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(reviewId)) {
                    return;
                }
                if (!reviewIds.contains(reviewId)) {
                    throw new NumberFormatException();
                }
                showCancelInteger();
                System.out.println("What is your updated Rating?:");
                int rating =Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(rating)) { 
                    return;
                }
                showCancelString();
                System.out.println("Whats your new Description?");
                String description = this.sc.nextLine();
                if (cancelStringOperation(description)) {
                    return;
                }
                this.reviewService.update(reviewId, rating, description);
                System.out.println("Review has been Updated!");
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
     * Provides UI to update current customers information
     */
    private void updateCustomer() {
        while (true) {
            System.out.println("What you want to update ?");
            System.out.println("Enter 1 for firstname");
            System.out.println("Enter 2 for lastname");
            System.out.println("Enter 3 for email");
            System.out.println("Enter 4 for address");
            System.out.println("Enter 5 for password");
            try {
                showCancelInteger();
                int input = Integer.parseInt(this.sc.nextLine());
                if (cancelIntegerOperation(input)) {
                    return;
                }
                String info = null;
                if (input == 1) {
                    System.out.println("--------------------------------------");
                    this.showCancelString();
                    System.out.println("Enter new firstname: ");
                    System.out.println("--------------------------------------");
                    info = this.sc.nextLine();
                    if (cancelStringOperation(info)) { 
                        return;
                    }
                    this.current.setFirstname(info);
                } else if (input == 2) {
                    System.out.println("--------------------------------------");
                    this.showCancelString();
                    System.out.println("Enter new lastname: ");
                    System.out.println("--------------------------------------");
                    info = this.sc.nextLine();
                    if (cancelStringOperation(info)) {
                        return;
                    }
                    this.current.setLastname(info);
                } else if (input == 3) {
                    System.out.println("--------------------------------------");
                    this.showCancelString();
                    System.out.println("Enter new email: ");
                    System.out.println("--------------------------------------");
                    info = this.sc.nextLine();
                    if (cancelStringOperation(info)) {
                        return;
                    }
                    this.current.setEmail(info);
                } else if (input == 4) {
                    System.out.println("--------------------------------------");
                    this.showCancelString();
                    System.out.println("Enter new address: ");
                    System.out.println("--------------------------------------");
                    info = this.sc.nextLine();
                    if (cancelStringOperation(info)) {
                        return;
                    }
                    this.current.setAddress(info);
                } else if (input == 5) {
                    System.out.println("--------------------------------------");
                    this.showCancelString();
                    System.out.println("Enter new password: ");
                    System.out.println("--------------------------------------");
                    info = this.sc.nextLine();
                    if (cancelStringOperation(info)) {
                        return;
                    }
                    this.current.setPassword(info);
                } else {
                    throw new NumberFormatException();
                }
                this.customerService.updateCustomerInformation(this.current, this.id);
                System.out.println("--------------------------------------");
                System.out.println("Customer information has been updated !");
                System.out.println("--------------------------------------");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid data");
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

    

