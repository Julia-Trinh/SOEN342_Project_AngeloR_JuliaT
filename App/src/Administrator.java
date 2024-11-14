import java.sql.SQLException;

public class Administrator extends RegisteredUser{
    Database db = Database.getInstance();

    public Administrator(String username, String password, String name, int phoneNumber) throws ClassNotFoundException, SQLException{
        super(username, password, name, phoneNumber);
        this.id = db.addAdmin(username, password, name, phoneNumber);
    }

    // If retrieved from db
    public Administrator (int id, String username, String password, String name, int phoneNumber){
        super(username, password, name, phoneNumber);
        this.id = id;
    }

    public int getId(){
        return id;
    }

    
}
