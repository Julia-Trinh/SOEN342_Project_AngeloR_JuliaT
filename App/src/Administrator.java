import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Administrator extends RegisteredUser{
    Database db = Database.getInstance();

    public Administrator(String username, String password, String name, int phoneNumber) throws ClassNotFoundException, SQLException, InterruptedException{
        super(username, password, name, phoneNumber);
        this.id = db.addAdmin(username, password, name, phoneNumber);
    }

    // If retrieved from db
    public Administrator (int id, String username, String password, String name, int phoneNumber){
        super(username, password, name, phoneNumber);
        this.id = id;
    }

    public int getId(){
        return id;
    }



    public Timeslot createLocationTimeslot(String day, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Schedule locationSchedule) throws ClassNotFoundException, SQLException, InterruptedException{
        Timeslot timeslot = new Timeslot(day, startTime, endTime, startDate, endDate);
        if (locationSchedule.isAvailableTimeslot(timeslot)) {
            locationSchedule.addTimeSlot(timeslot);
            return timeslot;
        }
        else {
            System.out.println("Error: Unable to add timeslot. Conflicting schedule detected. Please try again.");
            return null;
        }
    }
}
