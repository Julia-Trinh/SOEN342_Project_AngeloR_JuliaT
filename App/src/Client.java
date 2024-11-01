package src;
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

    public List<Booking> getBookings(){
        return bookings;
    }

    public void addBooking(Offering offering){
        if(offering.getIsAvailableToPublic()){
            //TO-DO: check Client's schedule
            
            Booking booking = new Booking(this, offering);  // added to bookings at creation
        }
    }
}
