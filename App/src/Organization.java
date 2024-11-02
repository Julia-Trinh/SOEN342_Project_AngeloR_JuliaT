import java.util.ArrayList;
import java.util.List;

public class Organization {
    private String name;
    private List<Offering> offerings;
    private List<Location> locations;

    public Organization(String name){
        this.name = name;
    }

    public void createOffering(Lesson lesson, Location location){
        offerings.add(new Offering(lesson, location)); //create unassigned offering
    }

    public void addLocation(Location location){
        locations.add(location);
    }
}
