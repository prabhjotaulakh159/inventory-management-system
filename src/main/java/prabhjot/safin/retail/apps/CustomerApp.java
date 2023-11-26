package prabhjot.safin.retail.apps;

import java.io.Console;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
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
    public void run() {
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
                this.id = Integer.parseInt(sc.nextLine())
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
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
                this.showCancelInteger();
                System.out.println("Type the product id you want to see reviews from:");
                int productId = Integer.parseInt(sc.nextLine());             
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                Map<Integer, Review> reviews = reviewService.getReviewForProduct(productId);
                System.out.println("Reviews: ");
                for (Integer id: reviews.keySet()) {
                    System.out.println("Review ID: " + id + " | " + reviews.get(id));
                }
                System.out.println("Enter Y to flag a review");
                String answer = sc.nextLine().toLowerCase();
                if (answer.equals("Y")) {
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
    private void ordersOptions() {
        while (true) {
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
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Provides UI to create an order
     */
    private void createOrder() {
        int storeid = 0;
        while (true) {
            try {
                this.printStores();
                this.showCancelInteger();
                System.out.println("Which store do you want to buy from? Enter the store Id:");
                storeid = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(storeid)) {
                    return;
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
        Order order = new Order(id, storeid, new Date(System.currentTimeMillis()));
        Map<Integer, Integer> products = new HashMap<>();
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("choose which product you'd like to buy");
                int productId = Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(productId)) {
                    return;
                }
                this.showCancelInteger();
                System.out.println("How many would you like to buy?");
                int quantity =  Integer.parseInt(sc.nextLine());
                if (cancelIntegerOperation(quantity)) {
                    return;
                }
                products.put(productId, quantity);
                System.out.println("Enter Y to stop picking products");
                String ans = sc.nextLine().toUpperCase();
                if (ans.equals("Y")) {
                    break;
                }
                int orderId = this.orderService.createOrder(order, products);
                System.out.println("Here is your order id: " + orderId);
            } catch (SQLException e) {
                this.handleSQLException(e);
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid data");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Provides UI to delete an order
     */
    private void deleteOrder() {
        while (true) {
            try {
                Map<Integer, Order> orders = this.orderService.getOrdersByCustomer(id);
                for (Integer id : orders.keySet()) {
                    System.out.println("Order Id: " + id + ", " + orders.get(id));
                }
                this.showCancelInteger();
                System.out.println("Choose which Order you want to delete by id");
                int id = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(id)) {
                    return;
                }
                this.orderService.deleteOrder(id);
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
                Map<String, Integer> details = orderService.getOrderDetails(orderId);
                for (String name : details.keySet()) {
                    System.out.println("Product: " + name + ", Quantity: " + details.get(name) + ", Price: " + orderService.getOrderTotal(orderId));
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
     * Provides UI for view all orders for the logged in customer
     */
    public void viewAllOrders() {
        try {
            Map<Integer, Order> orders = this.orderService.getOrdersByCustomer(this.id);
            for (Integer orderId : orders.keySet()) {
                System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
            }
        } catch (SQLException e) {
            this.handleSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }   
    }

    /**
     * Provides UI for review options
     */
    private void reviewsOptions(){
        while (true) {
            System.out.println("--------------------------------------");
            System.out.println("Enter 1 to Make a Review");
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
     * Provides UI to create a review 
     */
    private void createReview() {
        while (true) {
            try {
                this.printProducts();
                this.showCancelInteger();
                System.out.println("Choose which product you want to review, enter id:");
                int productId = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(productId)) {
                    return;
                }
                this.showCancelInteger();
                System.out.println("Enter your rating, it can be between 1 and 5");
                int rating = Integer.parseInt(sc.nextLine());
                if (this.cancelIntegerOperation(rating)) {
                    return;
                }
                this.showCancelString();
                System.out.println("Give us a brief description on what you like/dislike:");
                String description = sc.nextLine();
                if (this.cancelStringOperation(description)) {
                    return;
                }
                Review review = new Review(id, productId, 0, rating, description);
                reviewService.create(review);
                System.out.println("Review Created!");
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
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void deleteReview() throws SQLException, ClassNotFoundException{
        Map<Integer, Review> reviews = reviewService.getReviewsForCustomer(id);
        for (Integer reviewId : reviews.keySet()) {
            System.out.println("Review Id: " + reviewId + ", " + reviews.get(reviewId));
        }
        showCancelInteger();
        System.out.println("Enter the Review Id you want to delete");
        
        int reviewId= sc.nextInt();
        
        if (cancelIntegerOperation(reviewId)) return;
        
        reviewService.delete(reviewId);
        System.out.println("Review Removed!");
    }

    /**
     * Provides UI to flag a review
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void flagReview() throws SQLException, ClassNotFoundException{
        showCancelInteger();
        System.out.println("Which review Id do you want to flag?");
        
        int review = sc.nextInt();
        
        if (cancelIntegerOperation(review)) return;
        
        reviewService.flagReview(review);
        System.out.println("Review has been flagged");
    }

    /**
     * Provides UI to update a review
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void updateReview() throws SQLException, ClassNotFoundException {
        Map<Integer, Review> reviews = reviewService.getReviewsForCustomer(id);
        for(Integer reviewId : reviews.keySet()){
            System.out.println("Review ID: " + reviewId + " | " + reviews.get(reviewId));
        }
    
        showCancelInteger();
        System.out.println("Which Review you want to Update? Enter their Id:");
        
        int reviewId = sc.nextInt(); sc.nextLine();

        if (cancelIntegerOperation(reviewId)) return;

        showCancelInteger();
        System.out.println("What is your updated Rating?:");
        
        int rating = sc.nextInt(); sc.nextLine();
        
        if (cancelIntegerOperation(rating)) return;

        showCancelString();
        System.out.println("Whats your new Description?");
        
        String description = sc.nextLine();

        if (cancelStringOperation(description)) return;

        reviewService.update(reviewId, rating, description);
        System.out.println("Review has been Updated!");
    }

    /**
     * Provides UI to update current customers information
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void updateCustomer() throws SQLException, ClassNotFoundException  {
        while (true) {
            try {
                showCancelInteger();
                System.out.println("What you want to update ?");
                System.out.println("Enter 1 for firstname");
                System.out.println("Enter 2 for lastname");
                System.out.println("Enter 3 for email");
                System.out.println("Enter 4 for address");
                System.out.println("Enter 5 for password");
                
                int input = sc.nextInt();
                sc.nextLine();
                
                if (cancelIntegerOperation(input)) return;

                String info = null;

                if (input == 1) {
                    showCancelString();
                    System.out.println("Enter new firstname: ");
                    info = sc.nextLine();
                    if (cancelStringOperation(info)) return;
                    this.current.setFirstname(info);
                } else if (input == 2) {
                    showCancelString();
                    System.out.println("Enter new lastname: ");
                    info = sc.nextLine();
                    if (cancelStringOperation(info)) return;
                    this.current.setLastname(sc.nextLine());
                } else if (input == 3) {
                    showCancelString();
                    System.out.println("Enter new email: ");
                    info = sc.nextLine();
                    if (cancelStringOperation(info)) return;
                    this.current.setEmail(sc.nextLine());
                } else if (input == 4) {
                    showCancelString();
                    System.out.println("Enter new address: ");
                    info = sc.nextLine();
                    if (cancelStringOperation(info)) return;
                    this.current.setAddress(sc.nextLine());
                } else if (input == 5) {
                    showCancelString();
                    System.out.println("Enter new password: ");
                    info = sc.nextLine();
                    if (cancelStringOperation(info)) return;
                    this.current.setPassword(sc.nextLine());
                } else {
                    throw new InputMismatchException();
                }

                customerService.updateCustomerInformation(this.current, this.id);
                System.out.println("Customer information has been updated !");
            } catch (InputMismatchException e) {
                System.out.println("Not a valid option");
                sc.next();
            }
        }
    }
}

    

