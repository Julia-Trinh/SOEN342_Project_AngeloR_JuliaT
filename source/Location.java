package source;
import java.util.ArrayList;
import java.util.List;

public class Location {
    private String name;
    private String activityType;
    private String city;
    private Schedule schedule;
    private List<Offering> offerings;

    public Location(String name, String activityType, String city) {
        this.name = name;
        this.activityType = activityType;
        this.city = city;
        this.schedule = new Schedule();
    }

    public void addOffering(Offering offering){
        offerings.add(offering);
    }
}