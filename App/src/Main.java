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

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        try {
            db.getConnection();

            while (true) {
                System.out.println("Welcome to our Lesson booking system!\n" +
                                    "Please pick one of the following options:\n" +
                                    "1. Login\n" +
                                    "2. Register\n" +
                                    "3. Browse lessons\n" +
                                    "4. Exit\n");
                int userOption;
                System.out.print("Enter choice: ");
                userOption = key.nextInt();
                key.nextLine();
                
                switch (userOption) {
                    case 1: // login
                        userLogin();
                        if (loggedUser instanceof Client) clientMenu();
                        else if (loggedUser instanceof Instructor) instructorMenu();
                        else if (loggedUser instanceof Administrator) adminMenu();
                        break;
                    case 2: // register
                        userRegistration();
                        break;
                    case 3: // browse offerings
                        browsableOfferings();
                        System.out.println("Press Enter to continue...");
                        key.nextLine();
                        break;
                    case 4: // exit
                        System.out.println("\nExiting the menu. \nEnd of program.");
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void userLogin() throws ClassNotFoundException, SQLException{
        System.out.println("\nPlease pick one of the following options:\n" + 
                            "1. Login as Client\n" +
                            "2. Login as Instructor\n" +
                            "3. Login as Admin\n" + 
                            "4. Return\n\n" +
                            "Enter choice: ");
        
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        key.nextLine();
        
        while (loggedUser == null){
            System.out.print("\nUsername: ");
            String username = key.nextLine();
            System.out.print("Password: ");
            String password = key.nextLine();

            RegisteredUser user = null;
            switch (userOption) {
                case 1: // login as client
                    user = db.retrieveUserFromCredentials(username, password, "Client");
                    break;
                case 2: // login as instructor
                    user = db.retrieveUserFromCredentials(username, password, "Instructor");
                    break;
                case 3: // login as admin
                    user = db.retrieveUserFromCredentials(username, password, "Administrator");
                    break;
                case 4: // return to default menu
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            if (user != null) loggedUser = user;
            if (userOption == 4) break;
        }
    }

    public static void userRegistration() throws ClassNotFoundException, SQLException {
        System.out.println("Please pick one of the following options:\n" + 
                            "1. Register as Client\n" +
                            "2. Register as Instructor\n" +
                            "3. Register as Admin\n" + 
                            "4. Return\n");
        System.out.print("Enter choice: ");
        
        //get user input
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        key.nextLine();
        
            switch (userOption) {
                case 1:
                    // register as client
                    //get client information
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
                        //get guardian information
                        System.out.print("Client is under 18. Enter guardian information.\n");
                        System.out.print("Enter guardian name: ");
                        String guardianName = key.nextLine();
                        System.out.print("Enter relationship with youth: ");
                        String relationshipWithYouth = key.nextLine();
                        System.out.print("Enter guardian age: ");
                        int guardianAge = key.nextInt();
                        key.nextLine();

                        //instantiate guardian
                        new Guardian(clientUsername, clientPassword, clientName, clientPhoneNumber, clientAge, guardianName, relationshipWithYouth, guardianAge);
                    }
                    else{//client is 18 or older - instantiate client
                        new Client(clientUsername, clientPassword, clientName, clientPhoneNumber, clientAge);
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
        System.out.println("\nHello " + loggedUser.getName() + ",");
        while(true){
            System.out.println("Please chose what you want to do as an admin:\n" +
                    "1. View all bookings.\n" +
                    "2. Manage bookings.\n" +
                    "3. View all offerings.\n" +
                    "4. Add offering.\n" +
                    "5. Manage offerings.\n" +
                    "6. Manage instructor/client accounts.\n" +
                    "7. Logout.");
            
            int userOption;
            System.out.print("Enter choice: ");
            userOption = key.nextInt();
            key.nextLine();
            switch (userOption) {
                case 1://view all bookings
                    db.displayAllBookings();
                    break;
                case 2://manage bookings
                    manageBookingsAdmin();
                    break;
                case 3:
                    ResultSet rs = db.displayOfferings(); // TO-DO: to modify the output format to ressemble displayAssignedOfferings
                        while (rs.next()){
                            System.out.println("- The " + rs.getString("locationName") + ", in " + rs.getString("city") + ", is available for " +
                                                rs.getString("activityType") + " classes on " + rs.getString("day") + " from " + rs.getString("startTime") +
                                                " to " + rs.getString("endTime") + ", from " + rs.getString("startDate") + " to " + rs.getString("endDate") + ".");
                        }
                        System.out.println("\nPress Enter to continue...");
                        key.nextLine();
                    break;
                case 4:
                    addOffering();
                    break;      
                case 5:
                    // TO-DO: manage offerings
                    manageOfferingsAdmin();
                    break;
                case 6:
                    manageAccounts();
                    break;
                case 7:
                    loggedUser = null;
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            if (userOption == 6) break;
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
            System.out.println(rs1.getString("id") + ". " + rs1.getString("name") + " offers a " + rs1.getString("spaceType")
                    + " space in " + rs1.getString("city") + ".");

        }
        int userInput = key.nextInt();
        key.nextLine(); // To finish the line of nextInt

        // Create a new location
        Location location;
        if (userInput == 0) {
            System.out.println("Please enter the name of the location: ");
            String name = key.nextLine();

            System.out.println("Please enter the space type of the location: ");
            String activityType = key.nextLine();

            System.out.println("Please enter the city of the location: ");
            String city = key.nextLine();

            System.out.println("Please enter the name of the organization of the location: ");
            String organizationName = key.nextLine();

            if (db.retrieveOrganizationIdFromName(organizationName) == -1) {
                location = new Location(name, activityType, city, new Organization(organizationName));
            } else {
                location = new Location(name, activityType, city,
                        new Organization(db.retrieveOrganizationIdFromName(organizationName), organizationName));
            }
            System.out.println("\nNew location added.\n");
        }
        // Retrieve existing location
        else {
            location = db.retrieveLocation(userInput);
        }

        // Create a timeslot
        Timeslot timeslot = null;
        while (timeslot == null){
            System.out.println(
                    "Please enter the day of the week that the activity will take place in this location: ");
            String dayString = key.nextLine();

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

            timeslot = ((Administrator) loggedUser).createLocationTimeslot(dayString, startTime, endTime, startDate, endDate, location.getSchedule());
        }
        System.out.println("\nTimeslot is valid.\n");

        // Let Admin create a new or select an existing lesson
        System.out.println(
                "Please pick an existing lesson by entering their ID or add a new lesson by entering \"0\"");
        System.out.println("0. Add a new lesson.");
        ResultSet rs2 = db.displayLessons();
        while (rs2.next()) {
            if (rs2.getInt("capacity") == 1){
                System.out.println(rs2.getString("id") + ". " + "Private " + rs2.getString("activityType") + " lesson.");
            }
            else System.out.println(rs2.getString("id") + ". " + "Group " + rs2.getString("activityType") + " lesson of " + rs2.getString("capacity") + ".");
        }
        System.out.print("Enter choice: ");
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
            System.out.println("\nNew lesson created.\n");
        }
        // Retrieve existing lesson
        else{
            lesson = db.retrieveLesson(userInput);
        }

        // Create an offering
        Offering offering = new Offering(lesson, location, timeslot);
        lesson.addOffering(offering);
        System.out.println("\nNew offering created.\n");
        System.out.println("Press Enter to continue...");
        key.nextLine();
    }

    public static void manageBookingsAdmin() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Which booking would you like to delete?");
        db.displayAllBookings();
        System.out.print("Enter the ID of the booking you would like to delete: ");
        int bookingId = key.nextInt();
        key.nextLine();

        db.deleteBooking(bookingId);


    }

    public static void manageOfferingsAdmin() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Which offering would you like to delete?");
        ResultSet rs = db.displayOfferings();
        while (rs.next()){
            System.out.println(rs.getString("id") + ". The " + rs.getString("locationName") + ", in " + rs.getString("city") + ", is available for " +
                                rs.getString("activityType") + " classes on " + rs.getString("day") + " from " + rs.getString("startTime") +
                                " to " + rs.getString("endTime") + ", from " + rs.getString("startDate") + " to " + rs.getString("endDate") + ".");
        }
        System.out.print("Enter the ID of the offering you would like to delete: ");
        int offeringId = key.nextInt();
        key.nextLine();

        db.deleteOffering(offeringId);
    }

    public static void instructorMenu() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("\nHello " + loggedUser.getName() + ",");
        while(true){
            System.out.println("\nplease chose what you want to do as an instructor:\n" +
                    "1. View all my bookings\n" +
                    "2. View unassigned Offers\n" +
                    "3. Assign to Offer\n" +
                    "4. Logout\n");
            int userOption;
            System.out.print("Enter choice: ");
            userOption = key.nextInt();
            key.nextLine();

            switch (userOption) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    assignOffering();
                    break;
                case 4:
                    loggedUser = null;
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void assignOffering() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Please pick an offering to assign to yourself by entering their ID.");
        Instructor instructor = (Instructor) loggedUser;
        ResultSet rs = db.displayUnassignedOfferings(instructor.getCityAvailabilities(), instructor.getActivityType());

        System.out.printf("%-5s %-20s %-20s %-20s %-10s %-10s %-20s %-20s %-10s%n", 
                  "ID", "Activity Type", "Location Name", "Location City", 
                  "Start Time", "End Time", "Start Date", "End Date", "Day");

        // Print values
        while (rs.next()) {
            System.out.printf("%-5s %-20s %-20s %-20s %-10s %-10s %-20s %-20s %-10s%n", 
                            rs.getString("id"), rs.getString("activityType"), rs.getString("locationName"), rs.getString("locationCity"), 
                            rs.getString("startTime"), rs.getString("endTime"), rs.getString("startDate"), rs.getString("endDate"), rs.getString("day"));
        }
    
        System.out.print("\nEnter choice: ");
        int userInput = key.nextInt();
        key.nextLine(); // To finish the line of nextInt

        //check if offer fits in instructor's schedule
        boolean fitsInSchedule = instructor.getSchedule().isAvailableTimeslot(db.retrieveOfferingTimeslot(userInput));

        if(fitsInSchedule){
            System.out.println("Offering fits in schedule.");
            //assign offering to instructor
            db.assignInstructor(instructor.getId(), userInput);
        }
        else{
            System.out.println("Offering does not fit in schedule.");
            return;
        }
    }

    public static List<String> convertStringToList(String str){
        List<String> list = new ArrayList<String>();
        String[] arr = str.split(",");
        for (String s : arr){
            list.add(s);
        }
        return list;
    }



    public static void manageAccounts() throws ClassNotFoundException, SQLException{
        Scanner key = new Scanner(System.in);

        System.out.print("\nWould you like to view Instructor accounts (1) or Client accounts (2)?: ");
        int userOption = key.nextInt();
        key.nextLine();

        int accountDeletionOption = -1;
        do {
            switch (userOption) {
                case 1:
                    ResultSet rs1 = db.displayInstructors();
                    while (rs1.next()){
                        System.out.println(rs1.getString("id") + ". Username: " + rs1.getString("username") + " | Name: " + rs1.getString("name") +
                                            " | Phone Number: " + rs1.getString("phoneNumber") + " | Age: " + rs1.getString("age"));
                    }
                    System.out.print("\nTo delete an account, enter its ID. To exit, enter 0: ");
                    accountDeletionOption = key.nextInt();
                    key.nextLine();
                    if (accountDeletionOption != 0){
                        db.deleteInstructor(accountDeletionOption);
                    }
                    break;
    
                case 2:
                    ResultSet rs2 = db.displayClients();
                    while (rs2.next()){
                        System.out.println(rs2.getString("id") + ". Username: " + rs2.getString("username") + " | Name: " + rs2.getString("name") +
                                            " | Phone Number: " + rs2.getString("phoneNumber") + " | Age: " + rs2.getString("age"));
                    }
                    System.out.print("\nTo delete an account, enter its ID. To exit, enter 0: ");
                    accountDeletionOption = key.nextInt();
                    key.nextLine();
                    if (accountDeletionOption != 0){
                        db.deleteClient(accountDeletionOption);
                    }
                    break;
    
                default:
                    System.out.println("\nPlease enter 1 or 2.\n");
                    break;
            }
            if (accountDeletionOption == 0) break;
        } while (userOption != 1 && userOption != 2);
    }

    public static void clientMenu() throws ClassNotFoundException, SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("\nHello " + loggedUser.getName() + ",");
        while(true){
            System.out.println("Please chose what you want to do as a client:\n" +
                    "1. Make a booking.\n" +
                    "2. Manage my bookings.\n" +
                    "3. Logout.");
            
            int userOption;

            System.out.print("Enter choice: ");
            userOption = key.nextInt();
            key.nextLine();
            switch (userOption) {
                case 1:
                    browsableOfferings();
                    System.out.print("Please pick a lesson by entering their ID: ");
                    int offeringId = key.nextInt();
                    key.nextLine();
                    if (db.checkOfferingAvailability(offeringId) && db.checkOfferingOccupancy(offeringId)){
                        if(((Client) loggedUser).getSchedule().isAvailableTimeslot(db.retrieveOfferingTimeslot(offeringId))) {
                            db.addBooking(loggedUser.getId(), offeringId);
                            System.out.println("\nBooking has been successful!");
                        }
                        else System.out.println("\nError. Timeslots overlapping.");
                    }
                    else {
                        System.out.println("\nLesson unavailable. Please try again.\n");
                    }
                    break;
                case 2:
                    // TO-DO: manage bookings
                    break;
                case 3:
                    loggedUser = null;
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            if (userOption == 3) break;
        }
    }

    public static void browsableOfferings() throws ClassNotFoundException, SQLException {
        ResultSet rsL = db.displayLocations();
        System.out.println();
            while (rsL.next()){
                ResultSet rsO = db.displayAssignedOfferingsByLocation(rsL.getInt("id"));
                if (rsO == null) continue;

                System.out.println("We offer a " + rsL.getString("spaceType") + " in " + rsL.getString("name") + " as follows:");
                while (rsO.next()){
                    // id
                    String id = String.format("%-4s", rsO.getString("id"));
                    // Day and dates
                    String dayAndDates = String.format("%-48s", rsO.getString("day") + " " + rsO.getString("startDate") + " - " + rsO.getString("endDate") + ". ");
                    // Time
                    String time = String.format("%-15s", rsO.getString("startTime") + " - " + rsO.getString("endTime"));
                    // Capacity
                    String capacity = rsO.getInt("capacity") > 1 ? "Group" : "Private";
                    String capacityFormatted = String.format("%-9s", capacity); 
                    // Instructor
                    String instructor = String.format("%-15s", rsO.getString("instructorName"));
                    // Availability
                    String availability = rsO.getBoolean("isAvailableToPublic") ? "" : "UNAVAILABLE";
                    String availabilityFormatted = String.format("%-15s", availability);

                    // Print the formatted output
                    System.out.println(id + dayAndDates + time + capacityFormatted + "Instructor: " + instructor + availabilityFormatted);
                }
                System.out.println();
            }
    }
}