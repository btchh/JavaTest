package Hospital.Entities;

import Hospital.Enums.UserRole;

public class Admin extends Person {
    public Admin(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber, UserRole.ADMIN);
    }
} 