import java.sql.SQLException;

public class Booking {
    private int id;
    private Client client;
    private Offering offering;
    Database db = Database.getInstance();

    public Booking(Client client, Offering offering) throws ClassNotFoundException, SQLException, InterruptedException {
        this.client = client;
        this.offering = offering;

        // Add to associated classes
        client.getBookings().add(this);
        offering.addBooking(this);

        // Check if lesson is full
        if (offering.getBookings().size() == offering.getLesson().getCapacity()) offering.setIsAvailableToPublic(false);

        id = db.addBooking(client.getId(), offering.getId());
    }

    public int getId(){
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Offering getOffering() {
        return offering;
    }
}