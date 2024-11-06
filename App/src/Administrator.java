import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Administrator extends RegisteredUser{
    private int id;
    Database db = Database.getInstance();

    public Administrator(String username, String password, String name, int phoneNumber) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        id = db.addAdmin(username, password, name, phoneNumber);
    }

    public Administrator (int id, String username, String password, String name, int phoneNumber){
        super(username, password, name, phoneNumber);
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public Timeslot createLocationTimeslot(List<String> days, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Schedule locationSchedule) throws ClassNotFoundException, SQLException{
        Timeslot timeslot = new Timeslot(days, startTime, endTime, startDate, endDate);
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
