public class RegisteredUser {
    protected String username;
    protected String password;
    protected String name;
    protected int phoneNumber;

    public RegisteredUser(String username, String password, String name, int phoneNumber){
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
