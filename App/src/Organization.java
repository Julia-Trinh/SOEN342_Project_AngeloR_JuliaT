import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Organization {
    private String name;
    private List<Offering> offerings;
    private List<Location> locations;
    Database db = new Database();

    public Organization(String name) throws ClassNotFoundException, SQLException{
        this.name = name;
        db.addOrganization(name);
    }

    public void createOffering(Lesson lesson, Location location){
        offerings.add(new Offering(lesson, location)); //create unassigned offering
    }

    public void addLocation(Location location){
        locations.add(location);
    }
}
