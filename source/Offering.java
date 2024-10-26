package source;
import java.util.ArrayList;
import java.util.List;

public class Offering {
    private boolean availableForInstructors;
    private Instructor assignedInstructor;
    private Lesson lesson;
    private Location location;
    private List<Booking> bookings;

    public Offering(Lesson lesson, Location location) {
        this.lesson = lesson;
        this.location = location;
        this.availableForInstructors = false;
    }

    public void makeAvailableForInstructors() {
        this.availableForInstructors = true;
    }

    public void assignOffering(Instructor instructor){
        this.assignedInstructor = instructor;
        // TO-DO: move catalog
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
    }
}
