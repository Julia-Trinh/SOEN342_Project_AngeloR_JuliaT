import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTest {
    private static Connection con;
    private static boolean hasData = false;

    public ResultSet displayUsers() throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT username, name FROM user");
        if (!res.isBeforeFirst()) { // Check if ResultSet is empty
            System.out.println("No users found in the database.");
        }
        return res;
    }
    

    private void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:database.db");
        initialise();
    }

    private void initialise() throws SQLException {
        if (!hasData) {
            hasData = true;
    
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            if (!res.next()) {
                System.out.println("Building the User table with prepopulated values.");
                state.execute("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(60), name VARCHAR(60));");
    
                // Inserting initial values
                PreparedStatement prep = con.prepareStatement("INSERT INTO user (username, name) VALUES (?, ?);");
                prep.setString(1, "Julia123");
                prep.setString(2, "Julia Trinh");
                prep.execute();
    
                PreparedStatement prep2 = con.prepareStatement("INSERT INTO user (username, name) VALUES (?, ?);");
                prep2.setString(1, "hello");
                prep2.setString(2, "Julia T");
                prep2.execute();
            }
        }
    }

    public void addUser(String username, String name) throws ClassNotFoundException, SQLException {
        if (con == null) {
            getConnection();
        }
    
        PreparedStatement prep = con.prepareStatement("INSERT INTO user (username, name) VALUES (?, ?);");
        prep.setString(1, username); // Change the index to match column order
        prep.setString(2, name);
        prep.execute();
    }
    
}
