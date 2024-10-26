package source;

public class Booking {
    private Client client;
    private Timeslot timeslot;

    public Booking(Client client, Timeslot timeSlot) {
        this.client = client;
        this.timeslot = timeSlot;
    }

    public Client getClient() {
        return client;
    }

    public Timeslot getTimeSlot() {
        return timeslot;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "client=" + client +
                ", timeSlot=" + timeslot +
                '}';
    }
}