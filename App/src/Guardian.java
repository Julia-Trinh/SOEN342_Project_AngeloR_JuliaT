import java.sql.SQLException;

public class Guardian extends Client{
    private int id;
    private String guardianName;
    private String relationshipWithYouth;
    private int guardianAge;
    Database db = Database.getInstance();

    public Guardian(String username, String password, String youthName, int phoneNumber, int age, String guardianName, String relationshipWithYouth, int guardianAge) throws ClassNotFoundException, SQLException{
        super(username, password, youthName, phoneNumber, age, true);
        this.guardianName = guardianName;
        this.relationshipWithYouth = relationshipWithYouth;
        this.guardianAge = guardianAge;
        id = db.addClient(username, password, youthName, phoneNumber, age, this.schedule.getId(), true, guardianName, relationshipWithYouth, guardianAge);
    }

    public int getId(){
        return id;
    }
}
