import java.sql.SQLException;
import java.util.List;

public class Client extends RegisteredUser{
    protected  int id;
    protected  int age;
    private List<Booking> bookings;
    protected Schedule schedule;
    Database db = Database.getInstance();

    public Client(String username, String password, String name, int phoneNumber, int age) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.age = age;
        this.schedule = new Schedule();
        id = db.addClient(username, password, name, phoneNumber, age, this.schedule.getId(), false, "", "", -1);
    }

    public Client(String username, String password, String name, int phoneNumber, int age, boolean isGuardian) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.age = age;
        this.schedule = new Schedule();
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
