package source;

public class Location {
    private String name;
    private String city;
    private Schedule schedule;

    public Location(String name, String city) {
        this.name = name;
        this.city = city;
        this.schedule = new Schedule();
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}