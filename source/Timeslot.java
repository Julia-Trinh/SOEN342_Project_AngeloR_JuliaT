package source;

public class TimeSlot {
    private String day;
    private String startTime;
    private String endTime;
    private Lesson lesson;

    public TimeSlot(String day, String startTime, String endTime, Lesson lesson) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lesson = lesson;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "day='" + day + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", lesson=" + lesson +
                '}';
    }
}