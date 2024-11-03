import java.sql.SQLException;

public class Guardian extends Client{
    private String guardianName;
    private String relationshipWithYouth;
    Database db = new Database();

    public Guardian(String username, String password, String name, int phoneNumber, int age, String guardianName, String relationshipWithYouth) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber, age);
        this.guardianName = guardianName;
        this.relationshipWithYouth = relationshipWithYouth;
        db.addGuardian(username, password, name, phoneNumber, age);
    }
}
