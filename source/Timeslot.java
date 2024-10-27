package source;
import java.util.ArrayList;
import java.util.List;

public class Timeslot {
    private List<String> days;
    private String startTime;
    private String endTime;
    private Lesson lesson;

    public Timeslot(List<String> days, String startTime, String endTime, Lesson lesson) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lesson = lesson;
    }

}