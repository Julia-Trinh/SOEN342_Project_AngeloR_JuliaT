package source;
import java.util.ArrayList;
import java.util.List;

public class Offering {
    private boolean availableForInstructors;
    private boolean isAvailableToPublic;
    private Instructor assignedInstructor;
    private Lesson lesson;
    private Location location;
    private List<Booking> bookings;

    public Offering(Lesson lesson, Location location) {
        this.lesson = lesson;
        this.location = location;
        this.availableForInstructors = false;
        this.isAvailableToPublic = false;

        // Add Lesson timeslot in Location schedule
        location.getSchedule().addTimeSlot(lesson.geTimeslot());
    }

    public void makeAvailableForInstructors() {
        this.availableForInstructors = true;
    }

    public void assignOffering(Instructor instructor){
        this.assignedInstructor = instructor;
        this.availableForInstructors = false;
        this.isAvailableToPublic = true;
        // TO-DO: move catalog
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
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
    }
}
