package sg.edu.nus.iss.mini_project.model;

import java.util.List;

public class Member {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    
    // Lists to store events (empty initially, will be populated later)
    private List<String> hostingEvents;  // Event IDs hosted by the member
    private List<String> attendingEvents; // Event IDs the member is attending

    // Default constructor for creating a member via form or file
    public Member() {
        this.hostingEvents = null;
        this.attendingEvents = null;
    }

    // Constructor for creating a member with initial values
    public Member(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.hostingEvents = null;   // Can be added later
        this.attendingEvents = null; // Can be added later
    }


    public Member(String firstName, String lastName, String email, String password, List<String> hostingEvents, List<String> attendingEvents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.hostingEvents = hostingEvents;
        this.attendingEvents = attendingEvents;
    }

    

    @Override
    public String toString() {
        return "Member{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", hostingEvents=" + hostingEvents +
                ", attendingEvents=" + attendingEvents +
                '}';
    }



    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getHostingEvents() {
        return hostingEvents;
    }

    public void setHostingEvents(List<String> hostingEvents) {
        this.hostingEvents = hostingEvents;
    }

    public List<String> getAttendingEvents() {
        return attendingEvents;
    }

    public void setAttendingEvents(List<String> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }
}