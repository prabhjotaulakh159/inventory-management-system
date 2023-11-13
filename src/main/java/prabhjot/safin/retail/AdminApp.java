package prabhjot.safin.retail;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminApp {
    public static void main(String[] args) {
        System.out.println("Welcome to super store admin program !");
        int id = getId();
        String password = getPassword();

        System.out.println(id + ", " + password);
    }
    
    private static int getId() {
        Scanner sc = new Scanner(System.in);
        int id = 0;
        while (true) {
            try {
                System.out.println("Enter your ID: ");
                id = sc.nextInt();
                sc.close();
                return id;
            } catch (InputMismatchException e) {
                System.out.println("Make sure to enter a number");
                sc.next();
            } 
        }
    }

    private static String getPassword() {
        System.out.println("Enter your password: ");
        String password = "";
        Console console = System.console();
        char[] input = console.readPassword();
        for (char c : input) {
            password += c;
        }
        return password;
    }
}
