import java.util.ArrayList;
import java.util.List;

public class Location {
    private String name;
    private String activityType;
    private String city;
    private Schedule schedule;
    private List<Offering> offerings;
    private Organization organization;

    public Location(String name, String activityType, String city, Organization organization) {
        this.name = name;
        this.activityType = activityType;
        this.city = city;
        this.schedule = new Schedule();
        this.organization = organization;
    }

    public void addOffering(Offering offering){
        offerings.add(offering);
    }

    public Schedule getSchedule(){
        return this.schedule;
    }

    public String getCity(){
        return this.city;
    }
}