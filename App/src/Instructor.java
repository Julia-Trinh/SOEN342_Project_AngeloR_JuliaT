import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends RegisteredUser{
    private int id;
    private String activityType;
    private List<String> cityAvailabilities;
    private List<Offering> offerings; // To get from database: SELECT * FROM Offering WHERE instructorId = __;
    private Schedule schedule;
    Database db = new Database();

    public Instructor(String username, String password, String name, int phoneNumber, String activityType, List<String> cityAvailabilities) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.activityType = activityType;
        this.cityAvailabilities = cityAvailabilities;
        this.schedule = new Schedule();
        id = db.addInstructor(username, password, name, phoneNumber, this.schedule.getId());
    }

    public int getId(){
        return id;
    }

    public void pickOffering(Offering offering) throws ClassNotFoundException, SQLException{
        if (schedule.isAvailableTimeslot(offering.getLessonTimeslot()) && cityAvailabilities.contains(offering.geLocation().getCity())){
            offerings.add(offering);
            offering.assignOffering(this);
        }
        else{
            System.out.println("Error: Unable to add offering. Conflicting schedule detected.");
        }
    }

    public void addTimeSlot(Timeslot timeslot) throws ClassNotFoundException, SQLException{
        if (schedule.isAvailableTimeslot(timeslot)) {
            schedule.addTimeSlot(timeslot);
        }
        else {
            System.out.println("Error: Unable to add timeslot. Conflicting schedule detected.");
        }
    }
}
