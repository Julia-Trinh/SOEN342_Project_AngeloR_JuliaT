package source;

public class Booking {
    private Client client;
    private Offering offering;

    public Booking(Client client, Offering offering) {
        this.client = client;
        this.offering = offering;
    }
}