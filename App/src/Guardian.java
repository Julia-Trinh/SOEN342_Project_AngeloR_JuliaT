package source;

public class Guardian extends Client{
    private String guardianName;
    private String relationshipWithYouth;

    public Guardian(String name, String phoneNumber, int age, String guardianName, String relationshipWithYouth){
        super(name, phoneNumber, age);
        this.guardianName = guardianName;
        this.relationshipWithYouth = relationshipWithYouth;
    }
}
