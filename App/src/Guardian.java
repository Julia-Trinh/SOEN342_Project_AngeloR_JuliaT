import java.sql.SQLException;

public class Guardian extends Client{
    private String guardianName;
    private String relationshipWithYouth;
    private int guardianAge;
    Database db = Database.getInstance();

    public Guardian(String username, String password, String youthName, int phoneNumber, int age, String guardianName, String relationshipWithYouth, int guardianAge) throws ClassNotFoundException, SQLException, InterruptedException{
        super(username, password, youthName, phoneNumber, age, true);
        this.guardianName = guardianName;
        this.relationshipWithYouth = relationshipWithYouth;
        this.guardianAge = guardianAge;
        this.id = db.addClient(username, password, youthName, phoneNumber, age, this.schedule.getId(), true, guardianName, relationshipWithYouth, guardianAge);
    }

    // If retrieved from db
    public Guardian(int id, String username, String password, String name, int phoneNumber, int age, Schedule schedule, String guardianName, String relationshipWithYouth, int guardianAge) throws ClassNotFoundException, SQLException{
        super(id, username, password, name, phoneNumber, age, schedule);
        this.guardianName = guardianName;
        this.relationshipWithYouth = relationshipWithYouth;
        this.guardianAge = guardianAge;
    }

    public int getId(){
        return id;
    }

    public String getGuardianName(){
        return this.guardianName;
    }
}
