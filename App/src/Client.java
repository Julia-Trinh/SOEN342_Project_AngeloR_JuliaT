import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Client extends RegisteredUser{
    private int age;
    private List<Booking> bookings;
    private Schedule schedule; // TO-DO: add schedule initialization
    Database db = new Database();

    public Client(String username, String password, String name, int phoneNumber, int age) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.age = age;
        db.addClient(username, password, name, phoneNumber, age);
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
