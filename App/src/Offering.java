import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Offering {
    public int id;
    private boolean isAvailableToPublic;
    private Instructor assignedInstructor;
    private Lesson lesson;
    private Location location;
    private List<Booking> bookings;
    Database db = new Database();

    public Offering(Lesson lesson, Location location) throws ClassNotFoundException, SQLException {
        this.lesson = lesson;
        this.location = location;
        this.isAvailableToPublic = false;

        // Add Lesson timeslot in Location schedule
        // TO-DO: make sure that before creating an offering to check if timeslots are overlapping first
        location.getSchedule().addTimeSlot(lesson.getTimeslot());

        id = db.addOffering(lesson.getId(), location.getId(), isAvailableToPublic);
    }

    public void assignOffering(Instructor instructor) throws ClassNotFoundException, SQLException{
        this.assignedInstructor = instructor;
        this.isAvailableToPublic = true;
        db.assignOffering(instructor.getId());
        // TO-DO: move catalog
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
    }

    public int getId(){
        return id;
    }

    public List<Booking> getBookings(){
        return bookings;
    }

    public Lesson getLesson(){
        return lesson;
    }

    public Timeslot getLessonTimeslot(){
        return lesson.getTimeslot();
    }

    public boolean getIsAvailableToPublic(){
        return isAvailableToPublic;
    }

    public void setIsAvailableToPublic(boolean availability){
        this.isAvailableToPublic = availability;
        // TO-DO: change in db
    }

    public Location geLocation(){
        return location;
    }
}
