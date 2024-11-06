import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Organization {
    private int id;
    private String name;
    private List<Offering> offerings;
    private List<Location> locations;
    Database db = Database.getInstance();

    public Organization(String name) throws ClassNotFoundException, SQLException{
        this.name = name;
        id = db.addOrganization(name);
    }

    // If retrieved from db
    public Organization(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }
}
