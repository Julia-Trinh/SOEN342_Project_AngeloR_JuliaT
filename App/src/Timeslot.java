import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Timeslot {
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;

    public Timeslot(List<String> days, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<String> getDays() {
        return days;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isTimeslotOverlapping(Timeslot newTimeslot){
        //check if the date ranges overlap

        //newTimeslot needs to end before existingTimeslot.startDate or needs to start after existingTimeslot.endDate
        //no overlap = false    |   overlap = true
        if (!(newTimeslot.getEndDate().isBefore(this.startDate) || newTimeslot.getStartDate().isAfter(this.endDate))) {
              
            //check if the days overlap
            for (String newDay : newTimeslot.getDays()) {
                if (this.days.contains(newDay)) {

                    //check if the times overlap
                    //same logic as overlapping Date
                    if (!(newTimeslot.getEndTime().isBefore(this.startTime) || newTimeslot.getStartTime().isAfter(this.endTime))) {
                        return true; //conflict found
                    }
                }
            }
        }
        return false; //no conflicts found
    }
}