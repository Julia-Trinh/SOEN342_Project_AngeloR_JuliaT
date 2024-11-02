package src;
import java.sql.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner key = new Scanner(System.in);
        while (true) {
            defaultMenu();
            int userOption = key.nextInt();
        
            switch (userOption) {
                case 1:
                    userLogin();
                    break;
                case 2:
                    userRegistration();
                    break;
                case 3:
                    browsableOfferings();
                    break;
                case 4:
                    System.out.println("Exiting the menu.");
                    break;
        
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        
            if (userOption == 4) {
                break;
            }
        }
        key.close();
    }

    public static void defaultMenu(){
        System.out.println("Welcome to our Lesson booking system!\n" + 
                            "Please pick one of the following options:\n" +
                            "1. Login\n" +
                            "2. Register\n" +
                            "3. Browse lessons\n" +
                            "4. Exit\n");
    }

    public static void userLogin(){
        System.out.println("Please pick one of the following options:\n" + 
                            "1. Login as Customer\n" +
                            "2. Login as Instructor\n" +
                            "3. Login as Admin\n" + 
                            "4. Return\n");
        
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        
            switch (userOption) {
                case 1:
                    // login as customer
                    break;
                case 2:
                    // login as instructor
                    break;
                case 3:
                    // login as admin
                    break;
                case 4:
                    break;
        
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        
            key.close();
    }

    public static void userRegistration(){
        System.out.println("Please pick one of the following options:\n" + 
                            "1. Register as Customer\n" +
                            "2. Register as Instructor\n" +
                            "3. Register as Admin" + 
                            "4. Return\n");
        
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        
            switch (userOption) {
                case 1:
                    // register as customer
                    break;
                case 2:
                    // register as instructor
                    break;
                case 3:
                    // register as admin
                    break;
                case 4:
                    break;
        
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        
        key.close();
    }

    public static void browsableOfferings(){
        // TO-DO: output list of offerings
    }
}