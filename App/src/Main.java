import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Database db = Database.getInstance();
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    static RegisteredUser loggedUser = null;

    public static void main(String[] args) {

        try {
            db.getConnection();
            addOffering();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Scanner key = new Scanner(System.in);
        // while (true) {
        // defaultMenu();
        // int userOption = key.nextInt();

        // switch (userOption) {
        // case 1:
        // userLogin();
        // break;
        // case 2:
        // userRegistration();
        // break;
        // case 3:
        // browsableOfferings();
        // break;
        // case 4:
        // System.out.println("Exiting the menu.");
        // break;

        // default:
        // System.out.println("Invalid option. Please try again.");
        // break;
        // }

        // if (userOption == 4) {
        // break;
        // }
        // }
        // key.close();

    }

    public static void defaultMenu() {
        System.out.println("Welcome to our Lesson booking system!\n" +
                "Please pick one of the following options:\n" +
                "1. Login\n" +
                "2. Register\n" +
                "3. Browse lessons\n" +
                "4. Exit\n");
    }

    public static void userLogin() throws ClassNotFoundException, SQLException {
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
                adminMenu();
                break;
            case 4:
                break;

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }

        key.close();
    }

    public static void userRegistration() {
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
                // option to register as a guardian
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
        ResultSet rs1 = db.displayLocations();
        ResultSet rs2 = db.displayLessons();
        Scanner key = new Scanner(System.in);

        // Let Admin create a new or select an existing location
        System.out.println(
                "Please pick an existing location by entering their ID or add a new location by entering \"0\"");
        System.out.println("0. Add a new location.");
        while (rs1.next()) {
            System.out.println(rs1.getString("id") + ". " + rs1.getString("name") + " offers " + rs1.getString("spaceType")
                    + " classes in " + rs1.getString("city") + ".");
        }
        int userInput = key.nextInt();

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
            List<String> days = Arrays.asList(key.nextLine().split(","));
            System.out.println("Please enter the start time of the activity in this format \"HH:mm\": ");
            LocalTime startTime = LocalTime.parse(key.nextLine(), timeFormatter);
            System.out.println("Please enter the end time of the activity in this format \"HH:mm\": ");
            LocalTime endTime = LocalTime.parse(key.nextLine(), timeFormatter);
            System.out.println("Please enter the start date of the activity in this format \"MMMM dd, yyyy\": ");
            LocalDate startDate = LocalDate.parse(key.nextLine(), dateFormatter);
            System.out.println("Please enter the end date of the activity in this format \"MMMM dd, yyyy\": ");
            LocalDate endDate = LocalDate.parse(key.nextLine(), dateFormatter);
            timeslot = ((Administrator) loggedUser).createLocationTimeslot(days, startTime, endTime, startDate, endDate, location.getSchedule());
        }
        System.out.println("Timeslot is valid.");

        // Let Admin create a new or select an existing lesson
        System.out.println(
                "Please pick an existing lesson by entering their ID or add a new lesson by entering \"0\"");
        System.out.println("0. Add a new lesson.");
        while (rs2.next()) {
            if (rs2.getInt("capacity") == 1){
                System.out.print(rs2.getString("id") + ". " + "Private " + rs2.getString("activityType") + " lesson.");
            }
            else System.out.print(rs2.getString("id") + ". " + "Group " + rs2.getString("activityType") + " lesson of " + rs2.getString("capacity") + ".");
        }
        userInput = key.nextInt();

        // Create a lesson
        Lesson lesson;
        if (userInput == 0){
            System.out.println("Please enter the capacity of the lesson: ");
            int capacity = key.nextInt();
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
}