package source;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class Timeslot {
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;

    public Timeslot(List<String> days, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}