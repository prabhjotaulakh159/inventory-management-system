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
                int id=sc.nextInt();
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
                System.out.println("Enter 3 to exit products");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) AdminApp.printProducts();
                else if(input == 2) AdminApp.getProductById();
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


    private static void ordersCrud(int id){
         while(true){
            try{
                System.out.println("--------------------------------------");
                System.out.println("Enter 1 to Create an Order");
                System.out.println("Enter 2 to Delete your Order");
                System.out.println("Enter 3 to get Details of Order");
                System.out.println("--------------------------------------");
                int input = sc.nextInt();
                sc.nextLine();

                if(input == 1) createOrder(id);
                else if(input == 2) delete_order(id);
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

    private static int createOrder(int id) throws SQLException, ClassNotFoundException {
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

    private static void delete_order(int id) throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orders = orderService.getOrdersByCustomer(id);
        for(Integer orderId : orders.keySet()){
            System.out.println("Order Id: " + orderId + ", " + orders.get(orderId));
        }
        System.out.println("Choose which Order you want to delete");
        int deleteId= sc.nextInt();
        orderService.deleteOrder(deleteId);
        System.out.println("Order has been deleted!");
    }

}

    

