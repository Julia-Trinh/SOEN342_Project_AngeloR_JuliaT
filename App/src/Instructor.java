package src;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends RegisteredUser{
    private String activityType;
    private List<String> cityAvailabilities;
    private List<Offering> offerings;
    private Schedule schedule;

    public Instructor(String name, String phoneNumber, String activityType, List<String> cityAvailabilities){
        super(name, phoneNumber);
        this.activityType = activityType;
        this.cityAvailabilities = cityAvailabilities;
        this.schedule = new Schedule();
    }

    public void pickOffering(Offering offering){
        if (schedule.isAvailableTimeslot(offering.getLessonTimeslot()) && cityAvailabilities.contains(offering.geLocation().getCity())){
            offerings.add(offering);
            offering.assignOffering(this);
        }
        else{
            System.out.println("Error: Unable to add offering. Conflicting schedule detected.");
        }
    }

    public void addTimeSlot(Timeslot timeslot){
        if (schedule.isAvailableTimeslot(timeslot)) {
            schedule.addTimeSlot(timeslot);
        }
        else {
            System.out.println("Error: Unable to add timeslot. Conflicting schedule detected.");
        }
    }
}
