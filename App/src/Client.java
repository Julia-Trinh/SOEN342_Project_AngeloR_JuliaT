import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Client extends RegisteredUser{
    private int id;
    private int age;
    private List<Booking> bookings;
    protected Schedule schedule;
    Database db = Database.getInstance();

    public Client(String username, String password, String name, int phoneNumber, int age) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.age = age;
        this.schedule = new Schedule();
        id = db.addClient(username, password, name, phoneNumber, age, this.schedule.getId(), false, "", "", -1);
    }

    public int getId(){
        return id;
    }

    public List<Booking> getBookings(){
        return bookings;
    }

    public void addBooking(Offering offering) throws ClassNotFoundException, SQLException{
        if(offering.getIsAvailableToPublic()){
            //TO-DO: check Client's schedule
            
            Booking booking = new Booking(this, offering);  // added to bookings at creation
        }
    }
}
