import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private int id;
    private String activityType;
    private int capacity;
    private Timeslot timeslot;
    private List<Offering> offerings;
    Database db = new Database();

    public Lesson(String activityType, int capacity, Timeslot timeslot) throws ClassNotFoundException, SQLException {
        this.activityType = activityType;
        this.capacity = capacity;
        this.timeslot = timeslot;

        id = db.addLesson(activityType, capacity, timeslot.getId());
    }

    public void addOffering(Offering offering){
        offerings.add(offering);
    }

    public int getId(){
        return id;
    }

    public Timeslot getTimeslot(){
        return this.timeslot;
    }

    public int getCapacity(){
        return capacity;
    }
}