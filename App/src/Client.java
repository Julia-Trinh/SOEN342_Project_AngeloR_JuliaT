import java.sql.SQLException;
import java.util.List;

public class Client extends RegisteredUser{
    protected  int age;
    private List<Booking> bookings;
    protected Schedule schedule;
    Database db = Database.getInstance();

    public Client(String username, String password, String name, int phoneNumber, int age) throws ClassNotFoundException, SQLException, InterruptedException{
        super(username, password, name, phoneNumber);
        this.age = age;
        this.schedule = new Schedule();
        this.id = db.addClient(username, password, name, phoneNumber, age, this.schedule.getId(), false, "", "", -1);
    }

    // If is guardian
    public Client(String username, String password, String name, int phoneNumber, int age, boolean isGuardian) throws ClassNotFoundException, SQLException, InterruptedException {
        super(username, password, name, phoneNumber);
        this.age = age;
        this.schedule = new Schedule();
    }

    // If retrieved from db
    public Client(int id, String username, String password, String name, int phoneNumber, int age, Schedule schedule) {
        super(username, password, name, phoneNumber);
        this.id = id;
        this.age = age;
        this.schedule = schedule;
    }

    public int getId(){
        return id;
    }

    public List<Booking> getBookings(){
        return bookings;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void addBooking(Offering offering) throws ClassNotFoundException, SQLException, InterruptedException{
        if(offering.getIsAvailableToPublic()){
            Booking booking = new Booking(this, offering);  // added to bookings at creation
        }
    }
}
