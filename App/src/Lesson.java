import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private int id;
    private String activityType;
    private int capacity;
    private List<Offering> offerings;
    Database db = Database.getInstance();

    public Lesson(String activityType, int capacity) throws ClassNotFoundException, SQLException {
        this.activityType = activityType;
        this.capacity = capacity;
        id = db.addLesson(activityType, capacity);
    }

    public Lesson(int id, String activityType, int capacity){
        this.id = id;
        this.activityType = activityType;
        this.capacity = capacity;
    }

    public void addOffering(Offering offering){
        offerings.add(offering);
    }

    public int getId(){
        return id;
    }

    public int getCapacity(){
        return capacity;
    }
}