package source;
import java.util.ArrayList;
import java.util.List;

public class Client extends RegisteredUser{
    private int age;
    private List<Booking> bookings;
    private Schedule schedule; // TO-DO: add schedule initialization

    public Client(String name, String phoneNumber, int age){
        super(name, phoneNumber);
        this.age = age;
    }

    public void addBooking(Offering offering){
        if(offering.getIsAvailableToPublic()){

            //TO-DO: check Client's schedule
            Booking booking = new Booking(this, offering);
            bookings.add(booking);
            offering.addBooking(booking);

            // Check if lesson is full
            if (offering.getBookings().size() == offering.getLesson().getCapacity()) offering.setIsAvailableToPublic(false);
        }
    }
}
