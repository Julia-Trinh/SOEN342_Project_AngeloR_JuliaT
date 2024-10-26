package source;

public class Client extends RegisteredUser{
    int age;

    public Client(String name, String phoneNumber, int age){
        super(name, phoneNumber);
        this.age = age;
    }
}
