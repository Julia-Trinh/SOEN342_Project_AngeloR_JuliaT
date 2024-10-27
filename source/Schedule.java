package source;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Timeslot> timeslots;

    public Schedule() {
        this.timeslots = new ArrayList<>();
    }

    public void addTimeSlot(Timeslot timeslot) {
        //need to check if timeslot fits in schedule
        //add timeslot if valid
        if(isAvailableTimeslot(timeslot)){
            timeslots.add(timeslot);
        }
        else{
            System.out.println("Error: Timeslot conflicts with schedule. Timeslot not added to schedule.");
        }
        
    }

    public List<Timeslot> getTimeSlots() {
        return timeslots;
    }

    public boolean isAvailableTimeslot(Timeslot newTimeslot){
        //compare newTimeslot to schedule to ensure it fits in schedule
        for (Timeslot existingTimeslot : timeslots) {
            if (existingTimeslot.isTimeslotOverlapping(newTimeslot)){
                return false; //confict found
            }
        }
        return true;
    }
}