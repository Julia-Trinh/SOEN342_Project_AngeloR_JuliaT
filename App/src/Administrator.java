import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Administrator extends RegisteredUser{
    private int id;
    private Organization organization;
    Database db = Database.getInstance();

    public Administrator(String username, String password, String name, int phoneNumber, Organization organization) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.organization = organization;
        id = db.addAdmin(username, password, name, phoneNumber, organization.getId());
    }

    public int getId(){
        return id;
    }

    public void addLocation(String name, String activityType, String city){
        // TO-DO: add new location to database (new Location(name, activityType, city, organization))
    }

    public void createLocationTimeslot(List<String> days, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Schedule locationSchedule) throws ClassNotFoundException, SQLException{
        Timeslot timeslot = new Timeslot(days, startTime, endTime, startDate, endDate);
        if (locationSchedule.isAvailableTimeslot(timeslot)) {
            locationSchedule.addTimeSlot(timeslot);
        }
        else {
            System.out.println("Error: Unable to add timeslot. Conflicting schedule detected.");
        }
    }

    public void makeLesson(String activityType, int capacity, Timeslot timeslot){
        // TO-DO: add new lesson to database (new Lesson(String activityType, int capacity, Timeslot timeslot))
    }

    public void addOffering(Lesson lesson, Location location){
        // TO-DO: add new offering to database (new Offering(Lesson lesson, Location location))
        // check if the location timeslots permits the lesson timeslots before creating a new offering
        //needs connection to organization
        //maybe should have connection to organizations catalog to manage it
    }
}
