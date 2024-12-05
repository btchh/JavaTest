package Hospital.Entities;

import Hospital.Enums.UserRole;

public class Receptionist extends Person {
    private String desk;
    private String shift;
    private boolean isAvailable;

    public Receptionist(String firstName, String lastName, String email, String phoneNumber, String desk) {
        super(firstName, lastName, email, phoneNumber, UserRole.RECEPTIONIST);
        this.desk = desk;
        this.shift = "Day"; // Default shift
        this.isAvailable = true;
    }

    // Getters
    public String getDesk() { return desk; }
    public String getShift() { return shift; }
    public boolean isAvailable() { return isAvailable; }

    // Setters
    public void setDesk(String desk) { this.desk = desk; }
    public void setShift(String shift) { this.shift = shift; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("%s, Desk: %s, Shift: %s, Available: %s", 
            super.toString(), desk, shift, isAvailable);
    }
} 