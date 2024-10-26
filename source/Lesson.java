package source;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String activityType;
    private int capacity;
    private Timeslot timeslot;
    private List<Offering> offerings;

    public Lesson(String activityType, int capacity, Timeslot timeslot) {
        this.activityType = activityType;
        this.capacity = capacity;
        this.timeslot = timeslot;
    }

    public void addOffering(Offering offering){
        offerings.add(offering);
    }
}