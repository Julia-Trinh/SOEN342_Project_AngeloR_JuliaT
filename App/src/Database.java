import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        "id INTEGER NOT NULL UNIQUE, " + 
        "username VARCHAR(255) NOT NULL, " +
        "password VARCHAR(255) NOT NULL, " +
        "name VARCHAR(255) NOT NULL, " + 
        "phoneNumber INTEGER NOT NULL, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    // TO-DO: add city availabilities
    private static final String CREATE_TABLE_INSTRUCTOR = 
        "CREATE TABLE " + TABLE_INSTRUCTOR + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "username VARCHAR(255) NOT NULL, " +
        "password VARCHAR(255) NOT NULL, " +
        "name VARCHAR(255) NOT NULL, " + 
        "phoneNumber INTEGER NOT NULL, " +
        "activityType VARCHAR(255), " +
        "cityAvailabilities VARCHAR(255), " +
        "scheduleId INTEGER, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_CLIENT = 
        "CREATE TABLE " + TABLE_CLIENT + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "username VARCHAR(255) NOT NULL, " +
        "password VARCHAR(255) NOT NULL, " +
        "name VARCHAR(255) NOT NULL, " + 
        "phoneNumber INTEGER NOT NULL, " +
        "age INTEGER NOT NULL, " +
        "scheduleId INTEGER, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_GUARDIAN = 
        "CREATE TABLE " + TABLE_GUARDIAN + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "username VARCHAR(255) NOT NULL, " +
        "password VARCHAR(255) NOT NULL, " +
        "name VARCHAR(255) NOT NULL, " + 
        "phoneNumber INTEGER NOT NULL, " +
        "age INTEGER NOT NULL, " +
        "clientId INTEGER," +
        "guardianName VARCHAR(255), " + 
        "relationship VARCHAR(255), " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_ORGANIZATION = 
        "CREATE TABLE " + TABLE_ORGANIZATION + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "name VARCHAR(255), " + 
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_LESSON = 
        "CREATE TABLE " + TABLE_LESSON + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "activityType VARCHAR(255) NOT NULL, " + 
        "capacity INTEGER NOT NULL, " +
        "timeslotId INTEGER NOT NULL, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_LOCATION = 
        "CREATE TABLE " + TABLE_LOCATION + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "name VARCHAR(255) NOT NULL, " +
        "spaceType VARCHAR(255) NOT NULL, " + 
        "city VARCHAR(255) NOT NULL, " +
        "organizationId INTEGER NOT NULL, " +
        "scheduleId INTEGER, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_OFFERING = 
        "CREATE TABLE " + TABLE_OFFERING + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "lessonId INTEGER NOT NULL, " + 
        "locationId INTEGER NOT NULL, " +
        "instructorId INTEGER, " +
        "isAvailableToPublic BOOLEAN NOT NULL, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_BOOKING = 
        "CREATE TABLE " + TABLE_BOOKING + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "clientId INTEGER, " + 
        "guardianId INTEGER, " + 
        "offeringId INTEGER NOT NULL, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_TIMESLOT = 
        "CREATE TABLE " + TABLE_TIMESLOT + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " +  
        "startTime TEXT NOT NULL, " + 
        "endTime TEXT NOT NULL, " +
        "days TEXT NOT NULL, " +
        "startDate TEXT NOT NULL, " +
        "endDate TEXT NOT NULL," +
        "ScheduleId INTEGER, " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";
    
    private static final String CREATE_TABLE_SCHEDULE = 
        "CREATE TABLE " + TABLE_SCHEDULE + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " +  
        "timeslotIds TEXT, " + 
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";

    // Method to get the connection
    public void getConnection() throws ClassNotFoundException, SQLException {
        if (con == null || con.isClosed()) {
            // Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:../database.db");

            // con = DriverManager.getConnection("./database.db");


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

    // Add data to the database and return ID
    public int addAdmin(String username, String password, String name, int phoneNumber) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Administrator (username, password, name, phoneNumber) VALUES (?, ?, ?, ?);");
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, name);
        prep.setInt(4, phoneNumber);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addInstructor(String username, String password, String name, int phoneNumber) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Instructor (username, password, name, phoneNumber) VALUES (?, ?, ?, ?);");
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, name);
        prep.setInt(4, phoneNumber);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addClient(String username, String password, String name, int phoneNumber, int age) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Client (username, password, name, phoneNumber, age) VALUES (?, ?, ?, ?, ?);");
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, name);
        prep.setInt(4, phoneNumber);
        prep.setInt(5, age);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addGuardian(String username, String password, String name, int phoneNumber, int age) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Guardian (username, password, name, phoneNumber, age) VALUES (?, ?, ?, ?, ?);");
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, name);
        prep.setInt(4, phoneNumber);
        prep.setInt(5, age);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addOrganization(String name) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Organization (name) VALUES (?);");
        prep.setString(1, name);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addTimeslot(String days, String startTime, String endTime, String startDate, String endDate) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Timeslot (startTime, endTime, days, startDate, endDate) VALUES (?, ?, ?, ?, ?);");
        prep.setString(1, startTime);
        prep.setString(2, endTime);
        prep.setString(3, days);
        prep.setString(4, startDate);
        prep.setString(5, endDate);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addLesson(String activityType, int capacity, int timeslotId) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Lesson (activityType, capacity, timeslotId) VALUES (?, ?, ?);");
        prep.setString(1, activityType);
        prep.setInt(2, capacity);
        prep.setInt(3, timeslotId);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addLocation(String name, String activityType, String city, int organizationId) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Location (name, spaceType, city, organizationId) VALUES (?, ?, ?, ?);");
        prep.setString(1, name);
        prep.setString(2, activityType);
        prep.setString(3, city);
        prep.setInt(4, organizationId);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addOffering(int lessonId, int locationId, boolean isAvailableToPublic) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Offering (lessonId, locationId, isAvailableToPublic) VALUES (?, ?, ?);");
        prep.setInt(1, lessonId);
        prep.setInt(2, locationId);
        prep.setBoolean(3, isAvailableToPublic);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addBooking(int clientId, int guardianId, int offeringId) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Booking (clientId, guardianId, offeringId) VALUES (?, ?, ?);");
        prep.setInt(1, clientId);
        prep.setInt(2, guardianId);
        prep.setInt(3, offeringId);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }

    public int addSchedule() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Schedule DEFAULT VALUES;");
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }
}