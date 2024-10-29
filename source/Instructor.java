package source;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends RegisteredUser{
    private String activityType;
    private List<String> cityAvailabilities;
    private List<Offering> offerings;
    private Schedule schedule; // TO-DO: add schedule initialization

    public Instructor(String name, String phoneNumber, String activityType, List<String> cityAvailabilities){
        super(name, phoneNumber);
        this.activityType = activityType;
        this.cityAvailabilities = cityAvailabilities;
    }

    public void pickOffering(Offering offering){
        // TO-DO: add if to check if the timeslot and location works, and add to schedule. If not, error message
        offerings.add(offering);
        offering.assignOffering(this);
    }
}
