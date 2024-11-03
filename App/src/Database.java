import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Connection con;
    private static boolean hasData = false;

    // Table definitions
    private static final String TABLE_ADMINISTRATOR = "Administrator";
    private static final String TABLE_INSTRUCTOR = "Instructor";
    private static final String TABLE_CLIENT = "Client";
    private static final String TABLE_GUARDIAN = "Guardian";
    private static final String TABLE_ORGANIZATION = "Organization";
    private static final String TABLE_LESSON = "Lesson";
    private static final String TABLE_LOCATION = "Location";
    private static final String TABLE_OFFERING = "Offering";
    private static final String TABLE_BOOKING = "Booking";
    private static final String TABLE_TIMESLOT = "Timeslot";
    private static final String TABLE_SCHEDULE = "Schedule";

    // SQL creation statements
        private static final String CREATE_TABLE_ADMINISTRATOR = 
        "CREATE TABLE " + TABLE_ADMINISTRATOR + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "name VARCHAR(255), " + 
        "phoneNumber INTEGER" +
        ")";
    
    private static final String CREATE_TABLE_INSTRUCTOR = 
        "CREATE TABLE " + TABLE_INSTRUCTOR + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "name VARCHAR(255), " + 
        "activityType VARCHAR(255), " +
        "cityAvailabilities VARCHAR(255), " +
        "scheduleId INTEGER" +
        ")";
    
    private static final String CREATE_TABLE_CLIENT = 
        "CREATE TABLE " + TABLE_CLIENT + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "name VARCHAR(255), " + 
        "age INTEGER, " +
        "scheduleId INTEGER " +
        ")";
    
    private static final String CREATE_TABLE_GUARDIAN = 
        "CREATE TABLE " + TABLE_GUARDIAN + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "name VARCHAR(255), " + 
        "phoneNumber INTEGER, " +
        "clientId INTEGER," +
        "relationship VARCHAR(255)" +
        ")";
    
    private static final String CREATE_TABLE_ORGANIZATION = 
        "CREATE TABLE " + TABLE_ORGANIZATION + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "name VARCHAR(255) " + 
        ")";
    
    private static final String CREATE_TABLE_LESSON = 
        "CREATE TABLE " + TABLE_LESSON + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "activityType VARCHAR(255), " + 
        "capacity INTEGER, " +
        "timeslotId INTEGER" +
        ")";
    
    private static final String CREATE_TABLE_LOCATION = 
        "CREATE TABLE " + TABLE_LOCATION + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "name VARCHAR(255), " +
        "spaceType VARCHAR(255), " + 
        "city VARCHAR(255), " +
        "organizationId INTEGER, " +
        "scheduleId INTEGER " +
        ")";
    
    private static final String CREATE_TABLE_OFFERING = 
        "CREATE TABLE " + TABLE_OFFERING + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "lessonId INTEGER, " + 
        "locationId INTEGER, " +
        "instructorId INTEGER, " +
        "isAvailableToPublic BOOLEAN " +
        ")";
    
    private static final String CREATE_TABLE_BOOKING = 
        "CREATE TABLE " + TABLE_BOOKING + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "clientId INTEGER, " + 
        "offeringId INTEGER" +
        ")";
    
    private static final String CREATE_TABLE_TIMESLOT = 
        "CREATE TABLE " + TABLE_TIMESLOT + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "startTime TEXT, " + 
        "endTime TEXT, " +
        "days TEXT, " +
        "startDate TEXT, " +
        "endDate TEXT," +
        "ScheduleId INTEGER" +
        ")";
    
    private static final String CREATE_TABLE_SCHEDULE = 
        "CREATE TABLE " + TABLE_SCHEDULE + 
        " ("+ 
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
        "timeslotId INTEGER, " + 
        "offeringId INTEGER" +
        ")";

    // Method to get the connection
    public void getConnection() throws ClassNotFoundException, SQLException {
        if (con == null || con.isClosed()) {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:database.db");
            if (!hasData) {
                initialize();
            }
        }
    }

    // Method to initialize the database
    private void initialize() throws SQLException {
        if (!hasData) {
            hasData = true;

            Statement state = con.createStatement();

            // Create tables
            createTableIfNotExists(state, TABLE_ADMINISTRATOR, CREATE_TABLE_ADMINISTRATOR);
            createTableIfNotExists(state, TABLE_INSTRUCTOR, CREATE_TABLE_INSTRUCTOR);
            createTableIfNotExists(state, TABLE_CLIENT, CREATE_TABLE_CLIENT);
            createTableIfNotExists(state, TABLE_GUARDIAN, CREATE_TABLE_GUARDIAN);
            createTableIfNotExists(state, TABLE_ORGANIZATION, CREATE_TABLE_ORGANIZATION);
            createTableIfNotExists(state, TABLE_LESSON, CREATE_TABLE_LESSON);
            createTableIfNotExists(state, TABLE_LOCATION, CREATE_TABLE_LOCATION);
            createTableIfNotExists(state, TABLE_OFFERING, CREATE_TABLE_OFFERING);
            createTableIfNotExists(state, TABLE_BOOKING, CREATE_TABLE_BOOKING);
            createTableIfNotExists(state, TABLE_TIMESLOT, CREATE_TABLE_TIMESLOT);
            createTableIfNotExists(state, TABLE_SCHEDULE, CREATE_TABLE_SCHEDULE);

            state.close();
        }
    }

    // Helper method to create a table if it does not exist
    private void createTableIfNotExists(Statement state, String tableName, String createTableSQL) throws SQLException {
        ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
        if (!res.next()) {
            System.out.println("Building the " + tableName + " table.");
            state.execute(createTableSQL);
        }
        res.close();
    }

    // Method to close the connection
    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Closed database connection");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}