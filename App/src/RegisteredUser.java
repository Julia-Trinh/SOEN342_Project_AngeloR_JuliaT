public class RegisteredUser {
    private String username;
    private String password;
    private String name;
    private int phoneNumber;

    public RegisteredUser(String username, String password, String name, int phoneNumber){
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
