package source;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends RegisteredUser{
    private String activityType;
    private List <String> cityAvailabilities;
    // TO-DO: add offerings attributes

    public Instructor(String name, String phoneNumber, String activityType, List<String> cityAvailabilities){
        super(name, phoneNumber);
        this.activityType = activityType;
        this.cityAvailabilities = cityAvailabilities;
    }
}
