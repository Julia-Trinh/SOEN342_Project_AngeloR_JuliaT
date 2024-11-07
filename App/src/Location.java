import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Location {
    private int id;
    private String name;
    private String activityType;
    private String city;
    private Schedule schedule;
    private List<Offering> offerings;
    private Organization organization;
    Database db = Database.getInstance();

    public Location(String name, String activityType, String city, Organization organization) throws ClassNotFoundException, SQLException {
        this.name = name;
        this.activityType = activityType;
        this.city = city;
        this.schedule = new Schedule();
        this.organization = organization;

        id = db.addLocation(name, activityType, city, organization.getId(), schedule.getId());
    }

    // If retrieved from db
    public Location(int id, String name, String activityType, String city, Organization organization, Schedule schedule) throws ClassNotFoundException, SQLException {
        this.id = id;
        this.name = name;
        this.activityType = activityType;
        this.city = city;
        this.schedule = schedule;
        this.organization = organization;
    }

    public void addOffering(Offering offering){
        offerings.add(offering);
    }

    public int getId(){
        return id;
    }

    public Schedule getSchedule(){
        return this.schedule;
    }

    public String getCity(){
        return this.city;
    }
}