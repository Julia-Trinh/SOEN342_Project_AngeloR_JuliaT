import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Timeslot {
    private int id;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Schedule schedule;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    Database db = Database.getInstance();

    //constructor without an id
    public Timeslot(String day, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) throws ClassNotFoundException, SQLException, InterruptedException {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;

        // Change all attribute into a string before storing into the db
        id = db.addTimeslot(day, startTime.format(timeFormatter), endTime.format(timeFormatter), startDate.format(dateFormatter), endDate.format(dateFormatter));
    }

    //constructor with an id - used when retrieving from the database
    public Timeslot(int id, String day, String startTime, String endTime, String startDate, String endDate){
        this.id = id;
        this.day = day;
        
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

    public String getDay() {
        return day;
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

    public void setSchedule(Schedule schedule) throws ClassNotFoundException, SQLException, InterruptedException {
        this.schedule = schedule;
        db.setScheduleToTimeslot(id, schedule.getId());
    }

    public void setRetrievedSchedule(Schedule schedule){
        this.schedule = schedule;
    }

    public boolean isTimeslotOverlapping(Timeslot newTimeslot){
        //check if the date ranges overlap

        //newTimeslot needs to end before existingTimeslot.startDate or needs to start after existingTimeslot.endDate
        //no overlap = false    |   overlap = true
        if (!(newTimeslot.getEndDate().isBefore(this.startDate) || newTimeslot.getStartDate().isAfter(this.endDate))) {
              
            //check if the day overlap
            
            if (this.day.equals(newTimeslot.getDay())) {

                //check if the times overlap
                //same logic as overlapping Date
                if (!(newTimeslot.getEndTime().isBefore(this.startTime) || newTimeslot.getStartTime().isAfter(this.endTime))) {
                    return true; //conflict found
                }
            }
            
        }
        return false; //no conflicts found
    }
}