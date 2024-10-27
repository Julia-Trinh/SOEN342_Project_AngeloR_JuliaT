package source;

public class Administrator extends RegisteredUser{
    
    public Administrator(String name, String phoneNumber){
        super(name, phoneNumber);
    }

    public void createOffering(){
        //TODO (?)
        //needs connection to organization
        //maybe should have connection to organizations catalog to manage it
    }
}
