import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        /* 
    	SQLiteTest test = new SQLiteTest();
        ResultSet rs;

        try {

            rs = test.displayUsers();
            while (rs.next()) {
                System.out.println(rs.getString("username") + " " + rs.getString("name"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            */


        Database db = new Database();
        try {
            db.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // Scanner key = new Scanner(System.in);
        // while (true) {
        //     defaultMenu();
        //     int userOption = key.nextInt();
        
        //     switch (userOption) {
        //         case 1:
        //             userLogin();
        //             break;
        //         case 2:
        //             userRegistration();
        //             break;
        //         case 3:
        //             browsableOfferings();
        //             break;
        //         case 4:
        //             System.out.println("Exiting the menu.");
        //             break;
        
        //         default:
        //             System.out.println("Invalid option. Please try again.");
        //             break;
        //     }
        
        //     if (userOption == 4) {
        //         break;
        //     }
        // }
        // key.close();
        
        
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
                case 1:// login as client
                    while(true){
                        if (validateCredentials(1)){break;}
                    }
                    //send to client menu
                    clientMenu();
                    break;

                case 2:// login as instructor
                    while(true){
                        if (validateCredentials(2)){break;}
                    }
                    //send to instructor menu
                    instructorMenu();
                    break;

                case 3:// login as admin
                    while(true){
                        if (validateCredentials(3)){break;}
                    }
                    //send to admin menu
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

    public static void userRegistration(){
        System.out.println("""
                           Please pick one of the following options:
                           1. Register as Customer
                           2. Register as Instructor
                           3. Register as Admin
                           4. Return
                           """);
        
        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        
            switch (userOption) {
                case 1:
                    // register as customer
                    //create client instance and add to database
                    break;
                case 2:
                    // register as instructor
                    //create instructor instance and add to database
                    break;
                case 3:
                    // register as admin
                    //create admin instance and add to database
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
        //query all available offerings available to public
    }

    public static boolean  validateCredentials(int userType){
        // TO-DO: query database for matching credentials
        String usernameInput;
        String passwordInput;
        Scanner key = new Scanner(System.in);
        boolean passwordfound = false;

        while(!passwordfound){
            System.out.print("Enter username: ");
            usernameInput = key.nextLine();
            System.out.print("Enter password: ");
            passwordInput = key.nextLine();

            //check if username and password match
            switch(userType){
                case 1:
                    //query client table for matching creds
                    // if(matchfound()){return true;}
                    break;
                case 2:
                    //query instructor table for matching creds
                    // if(matchfound()){return true;}
                    break;
                case 3:
                    //query admin table for matching creds
                    // if(matchfound()){return true;}
            }
            
        }
        return true;
    }   


    public static void clientMenu(){
        System.out.println("""
            Please pick one of the following options:
            1. Browse Offerings
            2. Create Booking
            3. View Booking Details
            3. Cancel Booking
            3. Logout
            """);

        Scanner key = new Scanner(System.in);
        int userOption = key.nextInt();
        
            switch (userOption) {
                case 1:
                    // query all assigned offerings (available and non-available)
                    break;
                case 2:
                    // create booking for selected offering
                    break;
                case 3:
                    // view booking details
                    break;
                case 4:
                    // cancel booking
                    break;
                case 5:
                    // logout
        
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        
        key.close();
    }

    public static void instructorMenu(){
        System.out.println("""
            Please pick one of the following options:
            1. Browse Unassigned Offerings
            2. Select Unassigned Offering
            3. Logout
            """);
        
            Scanner key = new Scanner(System.in);
            int userOption = key.nextInt();
            
                switch (userOption) {
                    case 1:
                        // query all unassigned offerings
                        break;
                    case 2:
                        // change boolean value of offering to assigned
                        break;
                    case 3:
                        // logout
            
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            
            key.close();
    }

    public static void adminMenu(){
        System.out.println("""
            Please pick one of the following options:
            1. Read Records
            2. Modify Records
            3. Enter new Offering
            4. Cancel Offering
            5. Logout
            """);

            Scanner key = new Scanner(System.in);
            int userOption = key.nextInt();
            
                switch (userOption) {
                    case 1:
                        // read records
                        break;
                    case 2:
                        // modify records
                        break;
                    case 3:
                        // enter new offering
                        break;
                    case 4:
                        // cancel offering
                        break;
                    case 5:
                        // logout
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            
            key.close();
    }
}