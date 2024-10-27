package source;

public class Booking {
    private BookingDetails bookingDetails;
    private Client client;
    private Offering offering;

    public Booking(Client client, Offering offering) {
        this.client = client;
        this.offering = offering;
        this.bookingDetails = new BookingDetails();
    }

    public Client getClient() {
        return client;
    }

    public Offering getOffering() {
        return offering;
    }
}