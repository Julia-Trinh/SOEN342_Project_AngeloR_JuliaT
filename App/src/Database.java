import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

// Singleton
public class Database extends Thread{
    private static Database instance = null;
    private static Connection con;
    private static boolean hasData = false;

    // Semaphores
    private final Semaphore writeBlock = new Semaphore(1);
    private final Semaphore mutex = new Semaphore(1);
    private int readCount = 0;

    // Date and time formatters
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // Table definitions
    private static final String TABLE_ADMINISTRATOR = "Administrator";
    private static final String TABLE_INSTRUCTOR = "Instructor";
    private static final String TABLE_CLIENT = "Client";
    private static final String TABLE_ORGANIZATION = "Organization";
    private static final String TABLE_LESSON = "Lesson";
    private static final String TABLE_LOCATION = "Location";
    private static final String TABLE_OFFERING = "Offering";
    private static final String TABLE_BOOKING = "Booking";
    private static final String TABLE_TIMESLOT = "Timeslot";
    private static final String TABLE_SCHEDULE = "Schedule";

    private Database(){
    }

    public static Database getInstance(){
        if (instance == null){
            instance = new Database();
        }
        return instance;
    }

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
        "cityAvailabilities VARCHAR(255), " +
        "PRIMARY KEY(id AUTOINCREMENT) " +
        "FOREIGN KEY(scheduleId) REFERENCES Schedule(id)" +
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
        "isGuardian BOOLEAN NOT NULL, " +
        "guardianName VARCHAR(255), " +
        "relationshipWithYouth VARCHAR(255), " +
        "guardianAge INTEGER, " +
        "PRIMARY KEY(id AUTOINCREMENT), " +
        "FOREIGN KEY(scheduleId) REFERENCES Schedule(id)" +
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
        "PRIMARY KEY(id AUTOINCREMENT), " +
        "FOREIGN KEY(timeslotId) REFERENCES Timeslot(id) " +
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
        "PRIMARY KEY(id AUTOINCREMENT), " +
        "FOREIGN KEY(scheduleId) REFERENCES Schedule(id), " +
        "FOREIGN KEY(organizationId) REFERENCES Organization(id)" +
        ")";
    
    private static final String CREATE_TABLE_OFFERING = 
        "CREATE TABLE " + TABLE_OFFERING + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "lessonId INTEGER NOT NULL, " + 
        "locationId INTEGER NOT NULL, " +
        "timeslotId INTEGER NOT NULL, " +
        "instructorId INTEGER, " +
        "isAvailableToPublic BOOLEAN NOT NULL, " +
        "PRIMARY KEY(id AUTOINCREMENT), " +
        "FOREIGN KEY(instructorId) REFERENCES Instructor(id), " +
        "FOREIGN KEY(lessonId) REFERENCES Lesson(id), " +
        "FOREIGN KEY(locationId) REFERENCES Location(id), " +
        "FOREIGN KEY(timeslotId) REFERENCES Timeslot(id)" +
        ")";
    
    private static final String CREATE_TABLE_BOOKING = 
        "CREATE TABLE " + TABLE_BOOKING + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " + 
        "clientId INTEGER NOT NULL, " + 
        "offeringId INTEGER NOT NULL, " +
        "PRIMARY KEY(id AUTOINCREMENT), " +
        "FOREIGN KEY(clientId) REFERENCES Client(id), " +
        "FOREIGN KEY(offeringId) REFERENCES Offering(id)" +
        ")";
    
    private static final String CREATE_TABLE_TIMESLOT = 
        "CREATE TABLE " + TABLE_TIMESLOT + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " +  
        "startTime TEXT NOT NULL, " + 
        "endTime TEXT NOT NULL, " +
        "day TEXT NOT NULL, " +
        "startDate TEXT NOT NULL, " +
        "endDate TEXT NOT NULL," +
        "scheduleId INTEGER, " +
        "PRIMARY KEY(id AUTOINCREMENT), " +
        "FOREIGN KEY(ScheduleId) REFERENCES Schedule(id)" +
        ")";
    
    private static final String CREATE_TABLE_SCHEDULE = 
        "CREATE TABLE " + TABLE_SCHEDULE + 
        " ("+ 
        "id INTEGER NOT NULL UNIQUE, " +  
        "PRIMARY KEY(id AUTOINCREMENT) " +
        ")";

    // Method to get the connection
    public void getConnection() throws ClassNotFoundException, SQLException, InterruptedException {
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection("jdbc:sqlite:database.db");

            if (!hasData) {
                initialize();
            }
        }
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

    // Method to initialize the database
    private void initialize() throws SQLException, InterruptedException {
        if (!hasData) {
            hasData = true;

            Statement state = con.createStatement();

            // Create tables
            createTableIfNotExists(state, TABLE_ADMINISTRATOR, CREATE_TABLE_ADMINISTRATOR);
            createTableIfNotExists(state, TABLE_INSTRUCTOR, CREATE_TABLE_INSTRUCTOR);
            createTableIfNotExists(state, TABLE_CLIENT, CREATE_TABLE_CLIENT);
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
    private synchronized void createTableIfNotExists(Statement state, String tableName, String createTableSQL) throws SQLException, InterruptedException {
        writeBlock.acquire();

        ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
        if (!res.next()) {
            System.out.println("Building the " + tableName + " table.");
            state.execute(createTableSQL);
        }
        res.close();

        writeBlock.release();
    }

    
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ------------------------------ ADD Methods ------------------------------
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public int addAdmin(String username, String password, String name, int phoneNumber) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();

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

        writeBlock.release();

        return id;
    }

    public int addInstructor(String username, String password, String name, int phoneNumber, int scheduleId, String cityAvailabilities, String activityType) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Instructor (username, password, name, phoneNumber, scheduleId, cityAvailabilities, activityType) VALUES (?, ?, ?, ?, ?, ?, ?);");
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, name);
        prep.setInt(4, phoneNumber);
        prep.setInt(5, scheduleId);
        prep.setString(6, cityAvailabilities);
        prep.setString(7, activityType);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        writeBlock.release();

        return id;
    }

    public int addClient(String username, String password, String name, int phoneNumber, int age, int scheduleId, boolean isGuardian, String guardianName, String relationshipWithYouth, int guardianAge) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Client (username, password, name, phoneNumber, age, scheduleId, isGuardian, guardianName, relationshipWithYouth, guardianAge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        prep.setString(1, username);
        prep.setString(2, password);
        prep.setString(3, name);
        prep.setInt(4, phoneNumber);
        prep.setInt(5, age);
        prep.setInt(6, scheduleId);
        prep.setBoolean(7, isGuardian);
        prep.setString(8, guardianName);
        prep.setString(9, relationshipWithYouth);
        prep.setInt(10, guardianAge);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        writeBlock.release();

        return id;
    }

    public int addOrganization(String name) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
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

        writeBlock.release();

        return id;
    }

    public int addTimeslot(String day, String startTime, String endTime, String startDate, String endDate) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Timeslot (startTime, endTime, day, startDate, endDate) VALUES (?, ?, ?, ?, ?);");
        prep.setString(1, startTime);
        prep.setString(2, endTime);
        prep.setString(3, day);
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

        writeBlock.release();

        return id;
    }

    public int addLesson(String activityType, int capacity) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Lesson (activityType, capacity) VALUES (?, ?);");
        prep.setString(1, activityType);
        prep.setInt(2, capacity);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        writeBlock.release();

        return id;
    }

    public int addLocation(String name, String activityType, String city, int organizationId, int scheduleId) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Location (name, spaceType, city, organizationId, scheduleId) VALUES (?, ?, ?, ?, ?);");
        prep.setString(1, name);
        prep.setString(2, activityType);
        prep.setString(3, city);
        prep.setInt(4, organizationId);
        prep.setInt(5, scheduleId);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        writeBlock.release();

        return id;
    }

    public int addOffering(int lessonId, int locationId, int timeslotId, boolean isAvailableToPublic) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Offering (lessonId, locationId, timeslotId, isAvailableToPublic) VALUES (?, ?, ?, ?);");
        prep.setInt(1, lessonId);
        prep.setInt(2, locationId);
        prep.setInt(3, timeslotId);
        prep.setBoolean(4, isAvailableToPublic);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        writeBlock.release();

        return id;
    }

    public int addBooking(int clientId, int offeringId) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Booking (clientId, offeringId) VALUES (?, ?);");
        prep.setInt(1, clientId);
        prep.setInt(2, offeringId);
        prep.execute();

        // Retrieve the generated ID
        int id = -1;
        try (ResultSet rs = prep.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        // Retrieve the number of bookings for the offering
        PreparedStatement countBookingsPrep = con.prepareStatement("SELECT COUNT(*) FROM Booking WHERE offeringId = ?");
        countBookingsPrep.setInt(1, offeringId);
        ResultSet countResult = countBookingsPrep.executeQuery();
        int numberOfBookings = 0;
        if (countResult.next()) {
            numberOfBookings = countResult.getInt(1);
        }
    
        // Retrieve the capacity of the lesson associated with the offering
        PreparedStatement lessonCapacityPrep = con.prepareStatement("SELECT l.capacity FROM Lesson l " +
            "JOIN Offering o ON l.id = o.lessonId WHERE o.id = ?");
        lessonCapacityPrep.setInt(1, offeringId);
        ResultSet lessonResult = lessonCapacityPrep.executeQuery();
        int lessonCapacity = 0;
        if (lessonResult.next()) {
            lessonCapacity = lessonResult.getInt("capacity");
        }
    
        // Check if the number of bookings equals the lesson's capacity
        if (numberOfBookings == lessonCapacity) {
            setOfferingPublicAvailability(offeringId, false);
        }

        // Copy offering timeslot to the client's schedule
        PreparedStatement timeslotPrep = con.prepareStatement("SELECT T.day, T.startTime, T.endTime, T.startDate, T.endDate, C.scheduleId " +
                                                                "FROM Offering O " +
                                                                "JOIN Timeslot T ON O.timeslotId = T.id " +
                                                                "JOIN Client C ON C.id = ? " +
                                                                "WHERE O.id = ?");
        timeslotPrep.setInt(1, clientId);
        timeslotPrep.setInt(2, offeringId);

        String day = null;
        String startTime = null;
        String endTime = null;
        String startDate = null;
        String endDate = null;
        int scheduleId = -1;

        try (ResultSet rs = timeslotPrep.executeQuery()) {
            if (rs.next()) {
                day = rs.getString("day");
                startTime = rs.getString("startTime");
                endTime = rs.getString("endTime");
                startDate = rs.getString("startDate");
                endDate = rs.getString("endDate");
                scheduleId = rs.getInt("scheduleId");
            } else {
                throw new SQLException("No matching offering or client found.");
            }
        }

        int newTimeslotId = addTimeslot(day, startTime, endTime, startDate, endDate);

        PreparedStatement updateTimeslotPrep = con.prepareStatement("UPDATE Timeslot SET scheduleId = ? WHERE id = ?");
        updateTimeslotPrep.setInt(1, scheduleId);
        updateTimeslotPrep.setInt(2, newTimeslotId);
        updateTimeslotPrep.executeUpdate();

        writeBlock.release();

        return id;
    }

    public int addSchedule() throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
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

        writeBlock.release();

        return id;
    }

    public void addInstructorTimeslot(Schedule instructorSchedule, Timeslot selectedTimeslot) throws ClassNotFoundException, SQLException, InterruptedException {
        if (con == null) {
            getConnection();
        }
        


        // New timeslot
        String day = selectedTimeslot.getDay();
        LocalTime startTime = selectedTimeslot.getStartTime();
        LocalTime endTime = selectedTimeslot.getEndTime();
        LocalDate startDate = selectedTimeslot.getStartDate();
        LocalDate endDate = selectedTimeslot.getEndDate();

        // Add timeslot to database
        int timeslotId = addTimeslot(day, startTime.format(timeFormatter), endTime.format(timeFormatter), startDate.format(dateFormatter), endDate.format(dateFormatter));

        // Assign timeslot to instructor schedule
        PreparedStatement prep = con.prepareStatement("UPDATE Timeslot SET scheduleId = ? WHERE id = ?;");
        prep.setInt(1, instructorSchedule.getId());
        prep.setInt(2, timeslotId);
        prep.execute();
        
    }
    
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ------------------------------ UPDATE Methods ---------------------------
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void setScheduleToTimeslot(int timeslotId, int scheduleId) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("UPDATE Timeslot SET scheduleId = ? WHERE id = ?;");
        prep.setInt(1, scheduleId);
        prep.setInt(2, timeslotId);
        prep.execute();

        writeBlock.release();
    }

    public void assignOffering(int instructorId) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO Offering (instructorId, isAvailableToPublic) VALUES (?, ?);");
        prep.setInt(1, instructorId);
        prep.setBoolean(2, true);
        prep.execute();

        writeBlock.release();
    }

    public void assignInstructor(int instructorId, int offeringId) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
        

        //change isAvailableToPublic to 1
        PreparedStatement prep = con.prepareStatement("UPDATE Offering SET isAvailableToPublic = 1 WHERE id = ?;");
        prep.setInt(1, offeringId);
        prep.execute();

        PreparedStatement prep1 = con.prepareStatement("UPDATE Offering SET instructorId = ? WHERE id = ?;");
        prep1.setInt(1, instructorId);
        prep1.setInt(2, offeringId);
        prep1.execute();

        writeBlock.release();

        System.out.println("Instructor successfully assigned to offering.");
    }

    public void setOfferingPublicAvailability(int id, boolean availability) throws ClassNotFoundException, SQLException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("UPDATE Offering SET isAvailableToPublic = ? WHERE id = ?;");
        prep.setBoolean(1, availability);
        prep.setInt(2, id);
        prep.execute();

        writeBlock.release();
    }
    
    


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ------------------------------ READ Methods -----------------------------
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   
    public int retrieveOrganizationIdFromName(String name) throws ClassNotFoundException, SQLException, InterruptedException{
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();

        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("SELECT id FROM Organization WHERE name = ?");
        prep.setString(1, name);
        ResultSet rs = prep.executeQuery();

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (rs.next()){
            return rs.getInt("id");
        }
        else return -1;
    }

    public Location retrieveLocation(int locationId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        // Get Location data
        PreparedStatement prep1 = con.prepareStatement("SELECT id, name, spaceType, city, organizationId, scheduleId FROM Location WHERE id = ?");
        prep1.setInt(1, locationId);
        ResultSet rs1 = prep1.executeQuery();

        // Get Organization data
        PreparedStatement prep2 = con.prepareStatement("SELECT name FROM Organization WHERE id = ?");
        prep2.setInt(1, rs1.getInt("organizationId"));
        ResultSet rs2 = prep2.executeQuery();

        // Get schedule
        Schedule s = retrieveSchedule(rs1.getInt("scheduleId"));

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return new Location(rs1.getInt("id"), rs1.getString("name"), rs1.getString("spaceType"), rs1.getString("city"), new Organization(rs1.getInt("organizationId"), rs2.getString("name")), s);
    }

    public Schedule retrieveSchedule(int scheduleId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("SELECT id, startTime, endTime, day, startDate, endDate FROM Timeslot WHERE scheduleId = ?");
        prep.setInt(1, scheduleId);
        ResultSet rs3 = prep.executeQuery();
        List<Timeslot> timeslots = new ArrayList<>();
        while(rs3.next()){
            timeslots.add(new Timeslot(rs3.getInt("id"), rs3.getString("day"), rs3.getString("startTime"), rs3.getString("endTime"), rs3.getString("startDate"), rs3.getString("endDate")));
        }
        Schedule s = new Schedule(scheduleId, timeslots);
        for (Timeslot timeslot : timeslots) timeslot.setRetrievedSchedule(s);

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return s;
    }

    public RegisteredUser retrieveUserFromCredentials(String username, String password, String userType) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        // Client
        if (userType.equals("Client")){
            PreparedStatement prep = con.prepareStatement("SELECT * FROM Client WHERE username = ? AND password = ?;");
            prep.setString(1, username);
            prep.setString(2, password);
            ResultSet rs = prep.executeQuery();
            Schedule schedule = retrieveSchedule(rs.getInt("scheduleId"));

            RegisteredUser user = null;
            if (rs.next()) {
                if (rs.getBoolean("isGuardian") == false){
                    user = new Client(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("name"),
                                        rs.getInt("phoneNumber"), rs.getInt("age"), schedule);
                }
                else {
                    user = new Guardian (rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("name"),
                                            rs.getInt("phoneNumber"), rs.getInt("age"), schedule, rs.getString("guardianName"), 
                                            rs.getString("relationshipWithYouth"), rs.getInt("guardianAge"));
                }
            }
            
            mutex.acquire();
            readCount--;
            if (readCount == 0) writeBlock.release();
            mutex.release();

            return user;
        }

        // Instructor
        else if (userType.equals("Instructor")){
            PreparedStatement prep = con.prepareStatement("SELECT * FROM Instructor WHERE username = ? AND password = ?;");
            prep.setString(1, username);
            prep.setString(2, password);
            ResultSet rs = prep.executeQuery();
            Schedule schedule = retrieveSchedule(rs.getInt("scheduleId"));

            RegisteredUser user = null;
            if (rs.next()) {
                user = new Instructor(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("name"),
                                        rs.getInt("phoneNumber"), rs.getString("activityType"), 
                                        Arrays.asList(rs.getString("cityAvailabilities").split(",")), schedule);
            }

            mutex.acquire();
            readCount--;
            if (readCount == 0) writeBlock.release();
            mutex.release();

            return user;
        }

        // Admin
        else if (userType.equals("Administrator")){
            PreparedStatement prep = con.prepareStatement("SELECT * FROM Administrator WHERE username = ? AND password = ?;");
            prep.setString(1, username);
            prep.setString(2, password);
            ResultSet rs = prep.executeQuery();

            RegisteredUser user = null;
            if (rs.next()) {
                user = new Administrator(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("name"),
                                            rs.getInt("phoneNumber"));
            }

            mutex.acquire();
            readCount--;
            if (readCount == 0) writeBlock.release();
            mutex.release();

            return user;
        }

        else {
            mutex.acquire();
            readCount--;
            if (readCount == 0) writeBlock.release();
            mutex.release();
            return null;
        }
    }

    public Timeslot retrieveOfferingTimeslot(int offeringId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        //get timeslot from database
        String query = "SELECT t.id, t.startTime, t.endTime, t.startDate, t.endDate, t.day " +
                       "FROM Offering o " +
                       "JOIN Timeslot t ON o.timeslotId = t.id " +
                       "WHERE o.id = ?";
        
        PreparedStatement prep = con.prepareStatement(query);
        prep.setInt(1, offeringId);
        ResultSet rs = prep.executeQuery();

        //get timeslot attributes
        if (rs.next()) {
            int timeslotId = rs.getInt("id");
            String day = rs.getString("day");
            String startTime = rs.getString("startTime");
            String endTime = rs.getString("endTime");
            String startDate = rs.getString("startDate");
            String endDate = rs.getString("endDate");

            Timeslot timeslot = new Timeslot(
                timeslotId,
                day,
                startTime,
                endTime,
                startDate,
                endDate
            );

            mutex.acquire();
            readCount--;
            if (readCount == 0) writeBlock.release();
            mutex.release();

            return timeslot;
        } else {
            mutex.acquire();
            readCount--;
            if (readCount == 0) writeBlock.release();
            mutex.release();

            System.out.println("No timeslot found for the given offering ID.");
            return null;
        }
    }

    public Lesson retrieveLesson(int lessonId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("SELECT activityType, capacity FROM Lesson WHERE id = ?");
        prep.setInt(1, lessonId);
        ResultSet rs = prep.executeQuery();

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return new Lesson(lessonId, rs.getString("activityType"), rs.getInt("capacity"));
    }

    public boolean checkOfferingAvailability(int offeringId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("SELECT isAvailableToPublic FROM Offering WHERE id = ?");
        prep.setInt(1, offeringId);
        ResultSet rs = prep.executeQuery();
    
        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (rs.next()) {
            return rs.getBoolean("isAvailableToPublic");
        } else {
            System.out.println("Offering with ID " + offeringId + " not found.");
            return false;
        }
    }
    
    public int retrieveOfferingOccupancy(int offeringId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("SELECT COUNT(*) FROM Booking WHERE offeringId = ?");
        prep.setInt(1, offeringId);
        ResultSet rs = prep.executeQuery();
    
        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (rs.next()) {
            return rs.getInt(1);
        } else {
            System.out.println("Offering with ID " + offeringId + " not found.");
            return -1;
        }
    }

    public int retrieveLessonCapacity(int offeringId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("SELECT l.capacity FROM Lesson l " +
            "JOIN Offering o ON l.id = o.lessonId WHERE o.id = ?");
        prep.setInt(1, offeringId);
        ResultSet rs = prep.executeQuery();
    
        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (rs.next()) {
            return rs.getInt("capacity");
        } else {
            System.out.println("Offering with ID " + offeringId + " not found.");
            return -1;
        }
    }

    public boolean checkOfferingOccupancy(int offeringId) throws ClassNotFoundException, SQLException, InterruptedException{
        int offeringOccupancy = retrieveOfferingOccupancy(offeringId);
        int lessonCapacity = retrieveLessonCapacity(offeringId);
        return offeringOccupancy < lessonCapacity; //return true if there is space available
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ------------------------------ DISPLAY Methods --------------------------
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ResultSet displayLessons() throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet rs = state.executeQuery("SELECT id, activityType, capacity FROM Lesson");
        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("Currently no Lessons found in the database.");
        }

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();
        
        return rs;
    }    

    public ResultSet displayLocations() throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet rs = state.executeQuery("SELECT id, name, spaceType, city FROM Location");
        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("Currently no Locations found in the database.");
        }

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return rs;
    }

    public ResultSet displayUnassignedOfferings(List<String> cityAvailabilities, String activityType) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();

        if (con == null) {
            getConnection();
        }

        // Convert list to a single string
        String cityList = cityAvailabilities.stream()
        .map(city -> "'" + city + "'")
        .collect(Collectors.joining(", "));

        String query = "SELECT o.id, l.activityType, loc.name AS locationName, loc.city AS locationCity, " +
        "t.startTime, t.endTime, t.startDate, t.endDate, t.day " +
        "FROM Offering o " +
        "JOIN Lesson l ON o.lessonId = l.id " +
        "JOIN Location loc ON o.locationId = loc.id " +
        "JOIN Timeslot t ON o.timeslotId = t.id " +
        "WHERE o.instructorId IS NULL " +
        "AND o.isAvailableToPublic = 0 " +   //must be unassigned
        "AND loc.city IN (" + cityList + ")" +
        "AND l.activityType = ?";

        PreparedStatement prep = con.prepareStatement(query);
        prep.setString(1, activityType);
    
        ResultSet rs = prep.executeQuery();
    
        if (!rs.isBeforeFirst()) {
            System.out.println("Currently no unassigned Offerings found in the database.");
        }
    
        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return rs;
    }

    public ResultSet displayOfferings() throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("""
                                                    SELECT 
                                                        Location.name AS locationName,
                                                        Location.spaceType AS activityType,
                                                        Timeslot.days,
                                                        Timeslot.startTime,
                                                        Timeslot.endTime,
                                                        Timeslot.startDate,
                                                        Timeslot.endDate,
                                                        Offering.isAvailableToPublic
                                                    FROM Offering
                                                    JOIN Location ON Offering.locationId = Location.id
                                                    JOIN Timeslot ON Offering.timeslotId = Timeslot.id;
                                                    """);
        ResultSet rs = prep.executeQuery();
        if (!rs.isBeforeFirst()) {
            System.out.println("Currently no Offerings found in the database.");
        }

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return rs;
    }

    public ResultSet displayAssignedOfferingsByLocation(int locationId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("""
                                                    SELECT 
                                                        Location.name AS locationName,
                                                        Location.city,
                                                        Location.spaceType,
                                                        Timeslot.day,
                                                        Timeslot.startTime,
                                                        Timeslot.endTime,
                                                        Timeslot.startDate,
                                                        Timeslot.endDate,
                                                        Lesson.capacity,
                                                        Lesson.activityType,
                                                        Instructor.name AS instructorName,
                                                        Offering.isAvailableToPublic,
                                                        Offering.id
                                                    FROM Offering
                                                    JOIN Location ON Offering.locationId = Location.id
                                                    JOIN Timeslot ON Offering.timeslotId = Timeslot.id
                                                    JOIN Lesson ON Offering.lessonId = Lesson.id
                                                    JOIN Instructor ON Offering.instructorId = Instructor.id
                                                    WHERE Offering.instructorId IS NOT NULL AND Offering.locationId = ?;
                                                    """);
        prep.setInt(1, locationId);
        ResultSet rs = prep.executeQuery();

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (!rs.isBeforeFirst()) {
            return null;
        }
        return rs;
    }
    

    public ResultSet displayClients() throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM Client;");
        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("Currently no Clients found in the database.");
        }

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return rs;
    }

    public ResultSet displayInstructors() throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM Instructor;");
        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("Currently no Instructors found in the database.");
        }

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        return rs;
    }

    public void displayAllBookings() throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        String query = "SELECT b.id AS bookingId, c.name AS clientName, l.activityType AS lessonName, loc.name AS locationName " +
                       "FROM Booking b " +
                       "JOIN Client c ON b.clientId = c.id " +
                       "JOIN Offering o ON b.offeringId = o.id " +
                       "JOIN Lesson l ON o.lessonId = l.id " +
                       "JOIN Location loc ON o.locationId = loc.id";
    
        PreparedStatement prep = con.prepareStatement(query);
        ResultSet rs = prep.executeQuery();
    
        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("Currently no Bookings found in the database.");
        } else {
            System.out.println("\nBookings:");
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("bookingId") + ", Client: " + rs.getString("clientName") + ", Lesson: " + rs.getString("lessonName") + ", Location: " + rs.getString("locationName"));
            }
            System.out.println();
        }
    }

    public ResultSet displayAssignedOfferingsByInstructor(int instructorId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("""
                                                    SELECT 
                                                        Location.name AS locationName,
                                                        Location.city,
                                                        Location.spaceType,
                                                        Timeslot.day,
                                                        Timeslot.startTime,
                                                        Timeslot.endTime,
                                                        Timeslot.startDate,
                                                        Timeslot.endDate,
                                                        Lesson.capacity,
                                                        Lesson.activityType,
                                                        Instructor.name AS instructorName,
                                                        Offering.isAvailableToPublic,
                                                        Offering.id
                                                    FROM Offering
                                                    JOIN Location ON Offering.locationId = Location.id
                                                    JOIN Timeslot ON Offering.timeslotId = Timeslot.id
                                                    JOIN Lesson ON Offering.lessonId = Lesson.id
                                                    JOIN Instructor ON Offering.instructorId = Instructor.id
                                                    WHERE Offering.instructorId = ?;
                                                    """);
        prep.setInt(1, instructorId);
        ResultSet rs = prep.executeQuery();

        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (!rs.isBeforeFirst()) {
            return null;
        }
        return rs;
    }

    public void displayBookingsByClient(int clientId) throws ClassNotFoundException, SQLException, InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) writeBlock.acquire();
        mutex.release();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("""
                                                        SELECT 
                                                            Location.name AS locationName,
                                                            Location.city,
                                                            Timeslot.day,
                                                            Timeslot.startTime,
                                                            Timeslot.endTime,
                                                            Timeslot.startDate,
                                                            Timeslot.endDate,
                                                            Lesson.activityType,
                                                            Booking.id
                                                        FROM Booking
                                                        JOIN Offering ON Booking.offeringId = Offering.id
                                                        JOIN Location ON Offering.locationId = Location.id
                                                        JOIN Timeslot ON Offering.timeslotId = Timeslot.id
                                                        JOIN Lesson ON Offering.lessonId = Lesson.id
                                                        WHERE Booking.clientId = ?;
                                                    """);
        prep.setInt(1, clientId);
        ResultSet rs = prep.executeQuery();
    
        mutex.acquire();
        readCount--;
        if (readCount == 0) writeBlock.release();
        mutex.release();

        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("Currently no Bookings found in the database.");
        } else {
            System.out.println("\nYour bookings:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + ". " + rs.getString("day") + " " + rs.getString("startDate") + " to " + rs.getString("endDate") +
                    ", " + rs.getString("startTime") + " - " + rs.getString("endTime") +
                    "\tLesson: " + rs.getString("activityType") +
                    "\tLocation: " + rs.getString("locationName") +
                    "\tCity: " + rs.getString("city")
                );
            }
            System.out.println();
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ------------------------------ DELETE Methods ---------------------------
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void deleteClient(int clientId) throws SQLException, ClassNotFoundException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("DELETE FROM Client WHERE id = ?;");
        prep.setInt(1, clientId);
    
        int rowsAffected = prep.executeUpdate();

        writeBlock.release();

        if (rowsAffected > 0) {
            System.out.println("\nClient with ID " + clientId + " has been deleted.");
        } else {
            System.out.println("\nNo client found with ID " + clientId + ".");
        }
    }    

    public void deleteInstructor(int instructorId) throws SQLException, ClassNotFoundException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("DELETE FROM Instructor WHERE id = ?;");
        prep.setInt(1, instructorId);
    
        int rowsAffected = prep.executeUpdate();

        writeBlock.release();

        if (rowsAffected > 0) {
            System.out.println("\nInstructor with ID " + instructorId + " has been deleted.");
        } else {
            System.out.println("\nNo instructor found with ID " + instructorId + ".");
        }
    }

    public void deleteBooking(int bookingId) throws SQLException, ClassNotFoundException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }

        // Get the offeringId associated with the booking
        PreparedStatement offeringIdPrep = con.prepareStatement("SELECT offeringId FROM Booking WHERE id = ?");
        offeringIdPrep.setInt(1, bookingId);

        int offeringId = -1;
        try (ResultSet rs = offeringIdPrep.executeQuery()) {
            if (rs.next()) {
                offeringId = rs.getInt("offeringId");
            } else {
                System.out.println("No booking found with ID " + bookingId);
                return;
            }
        }

        // Delete the booking
        PreparedStatement prep = con.prepareStatement("DELETE FROM Booking WHERE id = ?;");
        prep.setInt(1, bookingId);

        int rowsAffected = prep.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("\nBooking with ID " + bookingId + " has been deleted.");
        } else {
            System.out.println("\nNo booking found with ID " + bookingId + ".");
            return; // Exit if no booking was found to delete
        }

        // Fetch the timeslot attributes of the lesson associated with the booking
        PreparedStatement timeslotFetchPrep = con.prepareStatement(
            "SELECT T.id, T.day, T.startTime, T.endTime, T.startDate, T.endDate, B.clientId " +
            "FROM Offering O " +
            "JOIN Timeslot T ON O.timeslotId = T.id " +
            "JOIN Booking B ON B.offeringId = O.id " + 
            "WHERE O.id = ?"
        );
        timeslotFetchPrep.setInt(1, offeringId);

        String day = null;
        String startTime = null;
        String endTime = null;
        String startDate = null;
        String endDate = null;
        int clientId = -1;

        try (ResultSet rs = timeslotFetchPrep.executeQuery()) {
            if (rs.next()) {
                day = rs.getString("day");
                startTime = rs.getString("startTime");
                endTime = rs.getString("endTime");
                startDate = rs.getString("startDate");
                endDate = rs.getString("endDate");
                clientId = rs.getInt("clientId");
            } else {
                System.out.println("\nNo timeslot in your schedule found for the deleted booking.");
                return; // Exit if no timeslot is found
            }
        }

        // Find and delete the matching timeslot from the client's schedule
        PreparedStatement clientTimeslotPrep = con.prepareStatement(
            "DELETE FROM Timeslot " +
            "WHERE day = ? AND startTime = ? AND endTime = ? AND startDate = ? AND endDate = ? AND scheduleId = " +
            "(SELECT scheduleId FROM Client WHERE id = ?)"
        );
        clientTimeslotPrep.setString(1, day);
        clientTimeslotPrep.setString(2, startTime);
        clientTimeslotPrep.setString(3, endTime);
        clientTimeslotPrep.setString(4, startDate);
        clientTimeslotPrep.setString(5, endDate);
        clientTimeslotPrep.setInt(6, clientId);

        int deletedRows = clientTimeslotPrep.executeUpdate();
        if (deletedRows > 0) {
            System.out.println("\nTimeslot has been deleted from the your schedule.");
        } else {
            System.out.println("\nNo matching timeslot found in the client's schedule to delete.");
        }

        // Check if the offering's availability is affected
        if(checkOfferingOccupancy(offeringId)){
            PreparedStatement offeringAvailabilityPrep = con.prepareStatement("UPDATE Offering SET isAvailableToPublic = 1 WHERE id = ?;");
            offeringAvailabilityPrep.setInt(1, offeringId);
            offeringAvailabilityPrep.execute();
        }
        else{
            PreparedStatement offeringAvailabilityPrep = con.prepareStatement("UPDATE Offering SET isAvailableToPublic = 0 WHERE id = ?;");
            offeringAvailabilityPrep.setInt(1, offeringId);
            offeringAvailabilityPrep.execute();
        }
        writeBlock.release();
    }

    public void deleteOffering(int offeringId) throws SQLException, ClassNotFoundException, InterruptedException {
        writeBlock.acquire();
        
        if (con == null) {
            getConnection();
        }
    
        //start a transaction
        //allow multiple queries to be executed in a single transaction
        //if one fails, the entire transaction is rolled back
        con.setAutoCommit(false);
    
        try {
            //delete all bookings associated with the offering
            PreparedStatement deleteBookingsPrep = con.prepareStatement("DELETE FROM Booking WHERE offeringId = ?;");
            deleteBookingsPrep.setInt(1, offeringId);
            int bookingsDeleted = deleteBookingsPrep.executeUpdate();
    
            //delete the offering
            PreparedStatement deleteOfferingPrep = con.prepareStatement("DELETE FROM Offering WHERE id = ?;");
            deleteOfferingPrep.setInt(1, offeringId);
            int offeringDeleted = deleteOfferingPrep.executeUpdate();
    
            //commit the transaction if both deletions were successful
            if (offeringDeleted > 0) {
                con.commit();
                System.out.println("\nOffering with ID " + offeringId + " and " + bookingsDeleted + " associated bookings have been deleted.");
            } else {
                con.rollback();
                System.out.println("\nNo offering found with ID " + offeringId + ".");
            }
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            //restore the auto-commit mode
            con.setAutoCommit(true);
        }
        writeBlock.release();
    }
}

        



   

