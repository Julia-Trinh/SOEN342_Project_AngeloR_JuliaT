package source;
import java.util.ArrayList;
import java.util.List;

public class Client extends RegisteredUser{
    private int age;
    private List<Booking> bookings;

    public Client(String name, String phoneNumber, int age){
        super(name, phoneNumber);
        this.age = age;
    }

    public void addBooking(Offering offering){
        // TO-DO: check lesson capacity and offerings of that lesson to see if client can still book
        Booking booking = new Booking(this, offering);
        bookings.add(booking);
        offering.addBooking(booking);
    }
}
