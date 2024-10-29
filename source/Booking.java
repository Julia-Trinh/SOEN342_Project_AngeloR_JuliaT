package source;

public class Booking {
    private BookingDetails bookingDetails;
    private Client client;
    private Offering offering;

    public Booking(Client client, Offering offering) {
        this.client = client;
        this.offering = offering;
        this.bookingDetails = new BookingDetails();

        // Add to associated classes
        client.getBookings().add(this);
        offering.addBooking(this);

        // Check if lesson is full
        if (offering.getBookings().size() == offering.getLesson().getCapacity()) offering.setIsAvailableToPublic(false);
    }

    public Client getClient() {
        return client;
    }

    public Offering getOffering() {
        return offering;
    }
}