import java.sql.SQLException;
import java.util.List;

public class Instructor extends RegisteredUser{
    private String activityType;
    private List<String> cityAvailabilities;
    private List<Offering> offerings; // To get from database: SELECT * FROM Offering WHERE instructorId = __;
    private Schedule schedule;
    Database db = Database.getInstance();

    public Instructor(String username, String password, String name, int phoneNumber, String activityType, List<String> cityAvailabilities) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.activityType = activityType;
        this.cityAvailabilities = cityAvailabilities;
        this.schedule = new Schedule();
        id = db.addInstructor(username, password, name, phoneNumber, this.schedule.getId(), convertListToString(cityAvailabilities), activityType);
    }

    // If retrieved from db
    public Instructor(int id, String username, String password, String name, int phoneNumber, String activityType, List<String> cityAvailabilities, Schedule schedule) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.id = id;
        this.activityType = activityType;
        this.cityAvailabilities = cityAvailabilities;
        this.schedule = schedule;
    }

    public int getId(){
        return id;
    }

    public void pickOffering(Offering offering) throws ClassNotFoundException, SQLException{
        if (schedule.isAvailableTimeslot(offering.getTimeslot()) && cityAvailabilities.contains(offering.getLocation().getCity())){
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

    public List<String> getCityAvailabilities(){
        return cityAvailabilities;
    }

    public String convertListToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            if (i < list.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

}
