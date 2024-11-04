import java.util.ArrayList;
import java.util.List;

// Singleton
public class Catalog {
    private static Catalog instance = null;

    private List<Administrator> adminCatalog;
    private List<Instructor> instructorCatalog;
    private List<Client> clientCatalog;
    private List<Organization> organizationCatalog;
    private List<Lesson> lessonCatalog;
    private List<Location> locationCatalog;
    private List<Booking> bookingCatalog;
    private List<Offering> unassignedOfferingCatalog;
    private List<Offering> assignedOfferingCatalog;
    

    private Catalog(){
    }

    public static Catalog getInstance(){
        if (instance == null){
            instance = new Catalog();
        }
        return instance;
    }

    public void addAdmin(Administrator admin){
        if (adminCatalog == null){
            adminCatalog = new ArrayList<>();
        }
        adminCatalog.add(admin);
    }

    public List<Administrator> getAdmin(){
        return adminCatalog;
    }

    public void addInstructor(Instructor instructor){
        if (instructorCatalog == null){
            instructorCatalog = new ArrayList<>();
        }
        instructorCatalog.add(instructor);
    }

    public List<Instructor> getInstructor(){
        return instructorCatalog;
    }

    public void addClient(Client client){
        if (clientCatalog == null){
            clientCatalog = new ArrayList<>();
        }
        clientCatalog.add(client);
    }

    public List<Client> getClients(){
        return clientCatalog;
    }

    public void addOrganization(Organization organization){
        if (organizationCatalog == null){
            organizationCatalog = new ArrayList<>();
        }
        organizationCatalog.add(organization);
    }

    public List<Organization> getOrganizationCatalog(){
        return organizationCatalog;
    }

    public void addLesson(Lesson lesson){
        if (lessonCatalog == null){
            lessonCatalog = new ArrayList<>();
        }
        lessonCatalog.add(lesson);
    }

    public List<Lesson> getLessonCatalog(){
        return lessonCatalog;
    }

    public void addLocation(Location location){
        if (locationCatalog == null){
            locationCatalog = new ArrayList<>();
        }
        locationCatalog.add(location);
    }

    public List<Location> getLocationCatalog(){
        return locationCatalog;
    }

    public void addBooking(Booking booking){
        if (bookingCatalog == null){
            bookingCatalog = new ArrayList<>();
        }
        bookingCatalog.add(booking);
    }

    public List<Booking> getBookingCatalog(){
        return bookingCatalog;
    }

    public void addUnassignedOffering(Offering offering){
        if (unassignedOfferingCatalog == null){
            unassignedOfferingCatalog = new ArrayList<>();
        }
        unassignedOfferingCatalog.add(offering);
    }

    public List<Offering> getUnassignedOffering(){
        return unassignedOfferingCatalog;
    }

    public void addAssignedOffering(Offering offering){
        if (assignedOfferingCatalog == null){
            assignedOfferingCatalog = new ArrayList<>();
        }
        assignedOfferingCatalog.add(offering);
    }

    public List<Offering> getAssignedOffering(){
        return assignedOfferingCatalog;
    }
}
