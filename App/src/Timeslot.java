import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Timeslot {
    private int id;
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Schedule schedule;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    Database db = Database.getInstance();

    public Timeslot(List<String> days, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) throws ClassNotFoundException, SQLException {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;

        // Change all attribute into a string before storing into the db
        id = db.addTimeslot(String.join(", ", days), startTime.format(timeFormatter), endTime.format(timeFormatter), startDate.format(dateFormatter), endDate.format(dateFormatter));
    }

    public Timeslot(int id, String days, String startTime, String endTime, String startDate, String endDate){
        this.id = id;
        
        // Split the days string by commas and store it as a List
        this.days = Arrays.asList(days.split(","));
        
        // Convert start and end times to LocalTime
        this.startTime = LocalTime.parse(startTime, timeFormatter);
        this.endTime = LocalTime.parse(endTime, timeFormatter);
        
        // Convert start and end dates to LocalDate
        this.startDate = LocalDate.parse(startDate, dateFormatter);
        this.endDate = LocalDate.parse(endDate, dateFormatter);
    }

    public int getId(){
        return id;
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) throws ClassNotFoundException, SQLException {
        this.schedule = schedule;
        db.setScheduleToTimeslot(schedule.getId());
    }

    public void setRetrievedSchedule(Schedule schedule){
        this.schedule = schedule;
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