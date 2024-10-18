package source;

public class Lesson {
    private String type;
    private boolean isGroup;
    private Instructor instructor;

    public Lesson(String type, boolean isGroup, Instructor instructor) {
        this.type = type;
        this.isGroup = isGroup;
        this.instructor = instructor;
    }

    public String getType() {
        return type;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "type='" + type + '\'' +
                ", isGroup=" + isGroup +
                ", instructor=" + instructor +
                '}';
    }
}