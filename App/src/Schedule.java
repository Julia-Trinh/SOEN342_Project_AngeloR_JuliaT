import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private int id;
    private List<Timeslot> timeslots;
    Database db = new Database();

    public Schedule() throws ClassNotFoundException, SQLException {
        this.timeslots = new ArrayList<>();
        id = db.addSchedule();
    }

    public void addTimeSlot(Timeslot timeslot) {
        //need to check if timeslot fits in schedule
        //add timeslot if valid
        if(isAvailableTimeslot(timeslot)){
            timeslots.add(timeslot);
            timeslot.setSchedule(this);
            // TO-DO: add to db, convert the list of all timeslot ids into a string seperated by commas
        }
        else{
            System.out.println("Error: Timeslot conflicts with schedule. Timeslot not added to schedule.");
        }
        
    }

    public int getId(){
        return id;
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