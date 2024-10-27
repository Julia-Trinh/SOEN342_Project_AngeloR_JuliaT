package source;

public class Booking {
    // TO-DO: initialize bookingDetails
    private BookingDetails bookingDetails;
    private Client client;
    private Offering offering;

    public Booking(Client client, Offering offering) {
        this.client = client;
        this.offering = offering;
    }
}