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
        // TO-DO: also check if the location works
        if (schedule.isAvailableTimeslot(offering.getLessonTimeslot())){
            offerings.add(offering);
            offering.assignOffering(this);
        }
        else{
            System.out.println("Error: Unable to add offering. Conflicting schedule detected.");
        }
    }
}
