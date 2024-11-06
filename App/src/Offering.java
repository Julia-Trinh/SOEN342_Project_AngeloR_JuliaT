import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Offering {
    public int id;
    private boolean isAvailableToPublic;
    private Instructor assignedInstructor;
    private Lesson lesson;
    private Location location;
    private Timeslot timeslot;
    private List<Booking> bookings;
    Database db = Database.getInstance();

    public Offering(Lesson lesson, Location location, Timeslot timeslot) throws ClassNotFoundException, SQLException {
        this.lesson = lesson;
        this.location = location;
        this.timeslot = timeslot;
        this.isAvailableToPublic = false;
        id = db.addOffering(lesson.getId(), location.getId(), timeslot.getId(), isAvailableToPublic);
    }

    public void assignOffering(Instructor instructor) throws ClassNotFoundException, SQLException{
        this.assignedInstructor = instructor;
        this.isAvailableToPublic = true;
        db.assignOffering(instructor.getId());
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

    public boolean getIsAvailableToPublic(){
        return isAvailableToPublic;
    }

    public void setIsAvailableToPublic(boolean availability){
        this.isAvailableToPublic = availability;
        // TO-DO: change in db
    }

    public Location getLocation(){
        return location;
    }

    public Timeslot getTimeslot(){
        return timeslot;
    }
}
