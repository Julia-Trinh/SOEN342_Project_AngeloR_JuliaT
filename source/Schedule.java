package source;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Timeslot> timeslots;

    public Schedule() {
        this.timeslots = new ArrayList<>();
    }

    public void addTimeSlot(Timeslot timeslot) {
        timeslots.add(timeslot);
    }

    public List<Timeslot> getTimeSlots() {
        return timeslots;
    }
}