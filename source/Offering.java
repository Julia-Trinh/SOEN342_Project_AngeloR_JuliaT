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

    // TO-DO: make non-available to public when lesson capacity is full
}
