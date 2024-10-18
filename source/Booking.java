package source;

public class Booking {
    private Client client;
    private TimeSlot timeSlot;

    public Booking(Client client, TimeSlot timeSlot) {
        this.client = client;
        this.timeSlot = timeSlot;
    }

    public Client getClient() {
        return client;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "client=" + client +
                ", timeSlot=" + timeSlot +
                '}';
    }
}