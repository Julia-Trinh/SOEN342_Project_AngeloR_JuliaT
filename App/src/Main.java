import java.awt.RenderingHints;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static Database db = Database.getInstance();
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    static RegisteredUser loggedUser = null;
    //static RegisteredUser loggedUser = new Administrator(1, "bla", "123", "bla", 514);

    public static void main(String[] args) {
        try {
            db.getConnection();
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
                    //browsableOfferings(); -> need to add
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

    public static void defaultMenu() {
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
                    System.out.print("Enter city availabilities (seperate with comma): ");
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

                    Administrator admin = new Administrator(adminUsername, adminPassword, adminName, adminPhoneNumber);
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

    public static void adminMenu() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        while(true){
            System.out.println("Hello " + loggedUser.getName() + ", please chose what you want to do as an admin:\n" +
                    "1. View all bookings.\n" +
                    "2. Manage bookings.\n" +
                    "3. View all offerings.\n" +
                    "4. Add offering.\n" +
                    "5. Manage instructor/client accounts.\n" +
                    "6. Logout.");
            
            int userOption;
            do {
                userOption = key.nextInt();
                switch (userOption) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        addOffering();
                        break;
                    case 5:

                        break;
                    case 6:
                        System.out.println("Logged out.");
                        key.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } while (userOption != 1 || userOption != 2 || userOption != 3 || userOption != 4 || userOption != 5 || userOption != 6);
        }
    }

    public static void addOffering() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);

        // Let Admin create a new or select an existing location
        System.out.println(
                "Please pick an existing location by entering their ID or add a new location by entering \"0\"");
        System.out.println("0. Add a new location.");
        ResultSet rs1 = db.displayLocations();
        while (rs1.next()) {
            System.out.println(rs1.getString("id") + ". " + rs1.getString("name") + " offers " + rs1.getString("spaceType")
                    + " classes in " + rs1.getString("city") + ".");
        }
        int userInput = key.nextInt();
        key.nextLine(); // To finish the line of nextInt

        // Create a new location
        Location location;
        if (userInput == 0) {
            System.out.println("Please enter the name of the location: ");
            String name = key.nextLine();

            System.out.println("Please enter the activity type of the location: ");
            String activityType = key.nextLine();

            System.out.println("Please enter the city of the location: ");
            String city = key.nextLine();

            System.out.println("Please enter the name of the organization of the location: ");
            String organizationName = key.nextLine();

            if (db.getOrganizationIdFromName(organizationName) == -1) {
                location = new Location(name, activityType, city, new Organization(organizationName));
            } else {
                location = new Location(name, activityType, city,
                        new Organization(db.getOrganizationIdFromName(organizationName), organizationName));
            }
            System.out.println("New location added.");
        }
        // Retrieve existing location
        else {
            location = db.retrieveLocation(userInput);
        }

        // Create a timeslot
        Timeslot timeslot = null;
        while (timeslot == null){
            System.out.println(
                    "Please enter the number of days separated by commas (e.g. Mondays,Tuesdays,Wednesdays) that the activity will take place in this location: ");
            String dayString = key.nextLine();
            List<String> days = Arrays.asList(dayString.split(","));

            System.out.println("Please enter the start time of the activity in this format \"HH:mm\": ");
            String startTimeString = key.nextLine();
            LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);

            System.out.println("Please enter the end time of the activity in this format \"HH:mm\": ");
            String endTimeString = key.nextLine();
            LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);

            System.out.println("Please enter the start date of the activity in this format \"MMMM d, yyyy\": ");
            String startDateString = key.nextLine();
            LocalDate startDate = LocalDate.parse(startDateString, dateFormatter);

            System.out.println("Please enter the end date of the activity in this format \"MMMM d, yyyy\": ");
            String endDateString = key.nextLine();
            LocalDate endDate = LocalDate.parse(endDateString, dateFormatter);

            timeslot = ((Administrator) loggedUser).createLocationTimeslot(days, startTime, endTime, startDate, endDate, location.getSchedule());
        }
        System.out.println("Timeslot is valid.");

        // Let Admin create a new or select an existing lesson
        System.out.println(
                "Please pick an existing lesson by entering their ID or add a new lesson by entering \"0\"");
        System.out.println("0. Add a new lesson.");
        ResultSet rs2 = db.displayLessons();
        while (rs2.next()) {
            if (rs2.getInt("capacity") == 1){
                System.out.print(rs2.getString("id") + ". " + "Private " + rs2.getString("activityType") + " lesson.");
            }
            else System.out.print(rs2.getString("id") + ". " + "Group " + rs2.getString("activityType") + " lesson of " + rs2.getString("capacity") + ".");
        }
        userInput = key.nextInt();
        key.nextLine(); // To finish the line of nextInt

        // Create a lesson
        Lesson lesson;
        if (userInput == 0){
            System.out.println("Please enter the capacity of the lesson: ");
            int capacity = key.nextInt();
            key.nextLine(); // To finish the line of nextInt
            System.out.println("Please enter the activity type of the lesson: ");
            String activityType = key.nextLine();
            lesson = new Lesson(activityType, capacity);
        }
        // Retrieve existing lesson
        else{
            lesson = db.retrieveLesson(userInput);
        }

        // Create an offering
        Offering offering = new Offering(lesson, location, timeslot);
        lesson.addOffering(offering);
        System.out.println("New offering created.");

        key.close();
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