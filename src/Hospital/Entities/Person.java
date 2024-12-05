package Hospital.Entities;

import Hospital.Enums.UserRole;
import Hospital.Utils.IdGenerator;

public abstract class Person {
    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private final UserRole role;

    protected Person(String firstName, String lastName, String email, String phoneNumber, UserRole role) {
        this.id = IdGenerator.getInstance().generateId(role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public UserRole getRole() { return role; }

    // Setters (except for id and role which are final)
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s %s, Role: %s", 
            id, firstName, lastName, role);
    }
} 