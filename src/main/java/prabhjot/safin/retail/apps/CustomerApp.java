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

public class CustomerApp extends Application {
    public CustomerApp() throws SQLException {
        super();
    }

    private int id;
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
                System.out.println("Press 4 for Stores");
                System.out.println("Press 5 to exit");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();

                if(input == 1) productCrud();
                else if(input == 2) ordersOptions();
                else if(input == 3) reviewsOptions();
                else if(input == 4) storeOptions();
                else if(input == 5) break;
                else System.out.println("Invalid Option");
            }
        } catch (InputMismatchException e) {
            System.out.println("Not a valid option !");
            sc.next();
        } 
    }

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
                Customer customer= customerService.login(id, password);
                if(customer != null){
                    System.out.println("Welcome " + customer.getFirstname() + " " + customer.getLastname() + "!");
                    break;
                }
                else{
                    System.out.println("Invalid Login");
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid data");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

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
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getReviewOfProduct() throws SQLException, ClassNotFoundException{
        printProducts();
        System.out.println("Type the product id you want to see reviews from:");
        int productId= sc.nextInt();
        Map<Integer, Review> reviews = reviewService.getReviewForProduct(productId);
        System.out.println("Reviews:");
        for(Integer id: reviews.keySet()){
            System.out.println(reviews.get(id));
        }
    }
       

    private void ordersOptions(){
         while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to Create an Order");
                System.out.println("Enter 2 to Delete your Order");
                System.out.println("Enter 3 to get Details of Order");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) createOrder();
                else if(input == 2) delete_order();
                else if(input == 3) getOrderDetails();
                else System.out.println("Invalid Option");
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void createOrder() throws SQLException, ClassNotFoundException {
        printStores();
        System.out.println("Which store do you want to buy from? Enter the store Id:");
        int storeid= sc.nextInt();
        Order order = new Order(id, storeid, new Date(System.currentTimeMillis()));
        Map<Integer, Integer> products= new HashMap<>();
        while(true){
            System.out.println("Enter 0 to quit");
            try{
                printProducts();
                System.out.println("choose which product you'd like to buy");
                int productId=sc.nextInt();
                System.out.println("How many would you like to buy?");
                int quantity= sc.nextInt(); sc.nextLine();
                products.put(productId, quantity);
                System.out.println("Are you done picking your products? y/n");
                String ans= sc.nextLine();

                if(ans.equals("y")){
                    break;
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
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

    private void delete_order() throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }
        System.out.println("Choose which Order you want to delete");
        int deleteId= sc.nextInt();
        orderService.deleteOrder(deleteId);
        System.out.println("Order has been deleted!");
    }
    
    private void getOrderDetails() throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }
        System.out.println("Choose order to get details on: ");
        int orderId = sc.nextInt();
        Map<String, Integer> details = orderService.getOrderDetails(orderId);
        for (String name : details.keySet()) {
            System.out.println("Product: " + name + ", Quantity: " + details.get(name) + ", Price: " + orderService.getOrderTotal(orderId));
        }
    }


    private void reviewsOptions(){
        while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to Make a Review");
                System.out.println("Enter 2 to delete a Review you made");
                System.out.println("Enter 3 to Flag a Review");
                System.out.println("Enter 4 to exit products");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1)createReview();
                else if(input == 2) deleteReview();
                else if(input ==3) flagReview();
                else if(input == 4) break;
                else System.out.println("Invalid Option");
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void createReview() throws SQLException, ClassNotFoundException{
        printProducts();
        System.out.println("Choose which product you want to review, enter id:");
        int productId= sc.nextInt();
        System.out.println("Enter your rating, it can be between 1 and 5");
        int rating= sc.nextInt();
        System.out.println("Give us a brief description on what you like/dislike:");
        String description = sc.nextLine();
        Review review = new Review(id, productId, 0, rating, description);
        reviewService.create(review);
        System.out.println("Review Created!");
    }
    
    private void deleteReview() throws SQLException{
        System.out.println("Enter the Review Id you want to delete");
        int reviewId= sc.nextInt();
        reviewService.delete(reviewId);
        System.out.println("Review Removed!");
    }

    private void flagReview() throws SQLException, ClassNotFoundException{
        getReviewForProduct();
        System.out.println("Which review Id do you want to flag?");
        int review = sc.nextInt();
        reviewService.flagReview(review);
        System.out.println("Review has been flagged");
    }
    
    private void storeOptions(){
        while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to View all Stores");
                System.out.println("Enter 2 to get a store by their ID");
                System.out.println("Enter 3 to exit products");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) printStores();
                else if (input == 2) getStoreById();
                else if(input == 3) break;
                else System.out.println("Invalid Option");

            }catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid option entered !");
                sc.next();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

    

