package prabhjot.safin.retail;

import java.io.Console;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Customer;
import prabhjot.safin.retail.models.Order;
import prabhjot.safin.retail.models.Review;
import prabhjot.safin.retail.models.Warehouse;
import prabhjot.safin.retail.services.CustomerService;
import prabhjot.safin.retail.services.OrderService;
import prabhjot.safin.retail.services.ProductService;
import prabhjot.safin.retail.services.ReviewService;
import prabhjot.safin.retail.services.StoreService;

public class CustomerApp {
    private static Scanner sc;
    private static OrderService orderService;
    private static ProductService productService;
    private static ReviewService reviewService;
    private static StoreService storeService;
    private static CustomerService customerService;
    private static ConnectionProvider connectionProv;
    private static int id;
    public static void main(String[] args) {
        try{
            sc= new Scanner(System.in);
            connectionProv = new ConnectionProvider();
            orderService = new OrderService(connectionProv.getConnection());
            productService = new ProductService(connectionProv.getConnection());
            reviewService = new ReviewService(connectionProv.getConnection());
            storeService = new StoreService(connectionProv.getConnection());
            customerService = new CustomerService(connectionProv.getConnection());
            login(customerService);

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
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Not a valid option !");
            sc.next();
        } 
    }

    private static void login(CustomerService cs){
        while(true){
            try{
                Console console = System.console();
                System.out.println("Enter your customer ID: ");
                id=sc.nextInt();
                System.out.println("Enter your password: ");
                String password = "";
                char[] passwordInput = console.readPassword();
                for (char c : passwordInput) password += c;
                Customer customer= cs.login(id, password);
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

    private static void productCrud(){
        while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to get all products");
                System.out.println("Enter 2 to get a product by id");
                System.out.println("Enter 3 to get the Reviews of a product");
                System.out.println("Enter 4 to exit products");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) AdminApp.printProducts();
                else if(input == 2) AdminApp.getProductById();
                else if(input == 3) getReviewOfProduct();
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

    private static void getReviewOfProduct() throws SQLException, ClassNotFoundException{
        AdminApp.printProducts();
        System.out.println("Type the product id you want to see reviews from:");
        int productId= sc.nextInt();
        Map<Integer, Review> reviews = reviewService.getReviewForProduct(productId);
        System.out.println("Reviews:");
        for(Integer prodId: reviews.keySet()){
            System.out.println(reviews.get(prodId).getRating() + " | " + reviews.get(prodId).getDescription() + " | Flagged: " + reviews.get(prodId).getFlags());
        }
    }
       

    private static void ordersOptions(){
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

    private static int createOrder() throws SQLException, ClassNotFoundException {
        AdminApp.printStores();
        System.out.println("Which store do you want to buy from? Enter the store Id:");
        int storeid= sc.nextInt();
        Order order = new Order(id, storeid);
        Map<Integer, Integer> products= new HashMap<>();
        while(true){
            try{
                AdminApp.printProducts();
                System.out.println("choose which product you'd like to buy");
                int productId=sc.nextInt();
                System.out.println("How many would you like to buy?");
                int quantity= sc.nextInt();
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
        return orderId;
    }

    private static void delete_order() throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }
        System.out.println("Choose which Order you want to delete");
        int deleteId= sc.nextInt();
        orderService.deleteOrder(deleteId);
        System.out.println("Order has been deleted!");
    }


    private static void reviewsOptions(){
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

                if(input == 1)

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

    private static void createReview() throws SQLException, ClassNotFoundException{
        AdminApp.printProducts();
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

}

    

