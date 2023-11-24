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
    public CustomerApp() throws SQLException {
        super();
    }

    private int id;
    private Customer current;
    
    @Override
    public void run() {
        try{
            login();
            while(true){
                System.out.println("--------------------------------------");
                System.out.println("Here are your options: ");
                System.out.println("Press 1 for Products");
                System.out.println("Press 2 for Orders");
                System.out.println("Press 3 for Placing Reviews");
                System.out.println("Press 4 to update your information");
                System.out.println("Press 5 to exit");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                if     (input == 1) productCrud();
                else if(input == 2) ordersOptions();
                else if(input == 3) reviewsOptions();
                else if(input == 4) updateCustomer();
                else if(input == 5) break;
                else System.out.println("Invalid Option");
            }
            System.out.println("Goodbye !");
            connectionProvider.kill();
        } catch (InputMismatchException e) {
            System.out.println("Not a valid option !");
            sc.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs in a customer
     */
    private void login(){
        while(true){
            try{
                Console console = System.console();
                System.out.println("Enter your customer ID: ");
                id=sc.nextInt();
                System.out.println("Enter your password: ");
                String password = "";
                char[] passwordInput = console.readPassword();
                for (char c : passwordInput) password += c;
                this.current = customerService.login(id, password);
                if(current != null){
                    System.out.println("Welcome " + current.getFirstname() + " " + current.getLastname() + "!");
                    break;
                }
                else{
                    System.out.println("Invalid Login");
                }
            }catch (SQLException e) {
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
     * Provides UI for product related operations
     */
    private void productCrud(){
        while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all products");
                System.out.println("Enter 2 to get a product by id");
                System.out.println("Enter 3 to get the Reviews of a product");
                System.out.println("Enter 4 to get product price at store");
                System.out.println("Enter 5 to exit products");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) printProducts();
                else if(input == 2) getProductById();
                else if(input == 3) getReviewOfProduct();
                else if(input == 4) getPriceAtStore();
                else if (input == 5) break;
                else System.out.println("Invalid Option");
            }catch (SQLException e) {
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
     * Prompts users to enter a product id and get a reviews on it
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getReviewOfProduct() throws SQLException, ClassNotFoundException{
        printProducts();
        showCancelInteger();
        System.out.println("Type the product id you want to see reviews from:");
        
        int productId= sc.nextInt(); 
        sc.nextLine();
        
        if(cancelIntegerOperation(productId))return;
        
        Map<Integer, Review> reviews = reviewService.getReviewForProduct(productId);
        
        System.out.println("Reviews:");
        for(Integer id: reviews.keySet()){
            System.out.println("Review ID: " + id + " | " + reviews.get(id));
        }
        
        System.out.println("Would you like to flag one of these reviews? y/n");
        String answer= sc.nextLine().toLowerCase();
        if(answer.equals("y")){
            flagReview();
        }
    }
       
    /**
     * Provides UI for options on orders
     */
    private void ordersOptions(){
         while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to Create an Order");
                System.out.println("Enter 2 to Delete your Order");
                System.out.println("Enter 3 to get Details of Order");
                System.out.println("Enter 4 to View all your orders");
                System.out.println("Enter 5 to Exit Orders");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) createOrder();
                else if(input == 2) delete_order();
                else if(input == 3) getOrderDetails();
                else if(input == 4) viewAllOrders();
                else if(input == 5) break;
                else System.out.println("Invalid Option");
            }catch (SQLException e) {
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
     * Provides UI to create an order
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void createOrder() throws SQLException, ClassNotFoundException {
        printStores();
        showCancelInteger();
        System.out.println("Which store do you want to buy from? Enter the store Id:");
        
        int storeid= sc.nextInt();
        
        if(cancelIntegerOperation(storeid))return;
        
        Order order = new Order(id, storeid, new Date(System.currentTimeMillis()));
        Map<Integer, Integer> products= new HashMap<>();
        while(true){
            try{
                printProducts();
                showCancelInteger();
                System.out.println("choose which product you'd like to buy");

                int productId=sc.nextInt();
                
                if (cancelIntegerOperation(productId)) return;

                showCancelInteger();
                System.out.println("How many would you like to buy?");
                
                int quantity= sc.nextInt(); 
                sc.nextLine();
                
                if (cancelIntegerOperation(quantity)) return;

                products.put(productId, quantity);
                
                System.out.println("Are you done picking your products? y/n");
                
                String ans= sc.nextLine().toLowerCase();
                if(ans.equals("y")) break;

            }catch (SQLException e) {
                handleSQLException(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        int orderId= orderService.createOrder(order, products);
        System.out.println("Here is your order id: " + orderId);
    }

    /**
     * Provides UI to delete an order
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void delete_order() throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }
        
        showCancelInteger();
        System.out.println("Choose which Order you want to delete");
        
        int deleteId= sc.nextInt();
       
        if (cancelIntegerOperation(deleteId)) return;
        
        orderService.deleteOrder(deleteId);
        System.out.println("Order has been deleted!");
    }
    
    /**
     * Provides UI to get the details of an order
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getOrderDetails() throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }

        showCancelInteger();
        System.out.println("Choose order to get details on: ");
        
        int orderId = sc.nextInt();
        
        if (cancelIntegerOperation(orderId)) return;
        
        Map<String, Integer> details = orderService.getOrderDetails(orderId);
        for (String name : details.keySet()) {
            System.out.println("Product: " + name + ", Quantity: " + details.get(name) + ", Price: " + orderService.getOrderTotal(orderId));
        }
    }

    /**
     * Provides UI for view all orders for the logged in customer
     */
    public void viewAllOrders() throws SQLException, ClassNotFoundException{
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }
    }

    /**
     * Provides UI for review options
     */
    private void reviewsOptions(){
        while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to Make a Review");
                System.out.println("Enter 2 to delete a Review you made");
                System.out.println("Enter 3 to update a Review");
                System.out.println("Enter 4 to exit Review");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1)createReview();
                else if(input == 2) deleteReview();
                else if(input == 3) updateReview();
                else if(input == 4) break;
                else System.out.println("Invalid Option");
            }catch (SQLException e) {
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
     * Provides UI to create a review 
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void createReview() throws SQLException, ClassNotFoundException{
        printProducts();
        showCancelInteger();
        System.out.println("Choose which product you want to review, enter id:");
        
        int productId= sc.nextInt();
        
        if (cancelIntegerOperation(productId)) return;
        
        showCancelInteger();
        System.out.println("Enter your rating, it can be between 1 and 5");
        
        int rating= sc.nextInt(); sc.nextLine();
        
        if (cancelIntegerOperation(rating)) return;

        showCancelString();
        System.out.println("Give us a brief description on what you like/dislike:");
        
        String description = sc.nextLine();
        
        if (cancelStringOperation(description)) return;
        
        Review review = new Review(id, productId, 0, rating, description);
        reviewService.create(review);
        System.out.println("Review Created!");
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

    

