package source;

public class Client extends RegisteredUser{
    private int age;
    // TO-DO: add offerings attributes

    public Client(String name, String phoneNumber, int age){
        super(name, phoneNumber);
        this.age = age;
    }
}
