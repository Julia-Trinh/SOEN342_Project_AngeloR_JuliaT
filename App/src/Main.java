import java.awt.RenderingHints;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    private static Database db = Database.getInstance();

    public static void main(String[] args) {
        try {
            db.getConnection();
            // Timeslot ts = new Timeslot(List.of("Tuesdays", "Wednesdays"), LocalTime.of(13, 0), LocalTime.of(15, 0), LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1));
            // Lesson l = new Lesson("Judo", 5, ts);
            // Organization concordia = new Organization("Concordia");
            // Location lo = new Location("Hall", "Judo", "Montreal", concordia);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        Scanner key = new Scanner(System.in);
        while (true) {
            defaultMenu();
            int userOption;
            
            System.out.print("Enter choice: ");
            userOption = key.nextInt();
            key.nextLine();
            
            
        
            switch (userOption) {
                case 1:
                    userLogin();
                    break;
                case 2:
                    try {
                        userRegistration();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    browsableOfferings();
                    break;
                case 4:
                    System.out.println("Exiting the menu. \nEnd of program.");
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
                            "1. Login as Client\n" +
                            "2. Login as Instructor\n" +
                            "3. Login as Admin\n" + 
                            "4. Return\n\n" +
                            "Enter choice: ");
        
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        key.nextLine();
        
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

    public static void userRegistration() throws ClassNotFoundException, SQLException {
        System.out.println("Please pick one of the following options:\n" + 
                            "1. Register as Client\n" +
                            "2. Register as Instructor\n" +
                            "3. Register as Admin\n" + 
                            "4. Return\n");
        System.out.print("Enter choice: ");
        
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        key.nextLine();
        
            switch (userOption) {
                case 1:
                    // register as client
                    System.out.print("Enter username: ");
                    String clientUsername = key.nextLine();
                    System.out.print("Enter password: ");
                    String clientPassword = key.nextLine();
                    System.out.print("Enter name: ");
                    String clientName = key.nextLine();
                    System.out.print("Enter phone number: ");
                    int clientPhoneNumber = key.nextInt();
                    key.nextLine();
                    System.out.print("Enter age: ");
                    int clientAge = key.nextInt();
                    key.nextLine();

                    if(clientAge < 18){// option to register as a guardian
                        System.out.print("Client is under 18. Enter guardian information.\n");
                        System.out.print("Enter guardian name: ");
                        String guardianName = key.nextLine();
                        System.out.print("Enter relationship with youth: ");
                        String relationshipWithYouth = key.nextLine();
                        System.out.print("Enter guardian age: ");
                        int guardianAge = key.nextInt();
                        key.nextLine();
                        Guardian guardian = new Guardian(clientUsername, clientPassword, clientName, clientPhoneNumber, clientAge, guardianName, relationshipWithYouth, guardianAge);
                    }
                    else{
                        Client client = new Client(clientUsername, clientPassword, clientName, clientPhoneNumber, clientAge);
                    }
                    //add to database through constructor
                    System.out.println("Client user added to database.");
                    break;
                case 2:
                    // register as instructor
                    System.out.print("Enter username: ");
                    String instructorUsername = key.nextLine();
                    System.out.print("Enter password: ");
                    String instructorPassword = key.nextLine();
                    System.out.print("Enter name: ");
                    String instructorName = key.nextLine();
                    System.out.print("Enter phone number: ");
                    int instructorPhoneNumber = key.nextInt();
                    key.nextLine();
                    System.out.print("Enter activity type: ");
                    String instructorActivityType = key.nextLine();
                    System.out.print("Enter city availabilities: ");
                    String instructorCityAvailabilities = key.nextLine();

                    Instructor instructor = new Instructor(instructorUsername, instructorPassword, instructorName, instructorPhoneNumber, instructorActivityType, convertStringToList(instructorCityAvailabilities));
                    //add to database through constructor
                    System.out.println("Instructor user added to database.");
                    break;
                case 3:
                    // register as admin

                    System.out.print("Enter username: ");
                    String adminUsername = key.nextLine();
                    System.out.print("Enter password: ");
                    String adminPassword = key.nextLine();
                    System.out.print("Enter name: ");
                    String adminName = key.nextLine();
                    System.out.print("Enter phone number: ");
                    int adminPhoneNumber = key.nextInt();
                    key.nextLine();
                    System.out.print("Enter organization: ");   //idk if we should add this
                    String adminOrganization = key.nextLine();

                    Administrator admin = new Administrator(adminUsername, adminPassword, adminName, adminPhoneNumber, new Organization(adminOrganization));
                    //add to database through constructor
                    System.out.println("Admin user added to database.");
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
        }
        System.out.println("Now returning to main menu.");
        System.out.println("Press Enter to continue...");
        key.nextLine();

        
        
    }

    public static void browsableOfferings(){
        // TO-DO: output list of offerings
    }

    public static List<String> convertStringToList(String str){
        List<String> list = new ArrayList<String>();
        String[] arr = str.split(",");
        for (String s : arr){
            list.add(s);
        }
        return list;
    }
}