package Hospital.Entities;

import Hospital.Enums.UserRole;
import java.util.ArrayList;
import java.util.List;

public class Nurse extends Person {
    private String department;
    private List<String> certifications;
    private String shift;
    private boolean isOnDuty;

    public Nurse(String firstName, String lastName, String email, String phoneNumber,
                String department, List<String> certifications) {
        super(firstName, lastName, email, phoneNumber, UserRole.NURSE);
        this.department = department;
        this.certifications = new ArrayList<>(certifications);
        this.shift = "Day"; // Default shift
        this.isOnDuty = true;
    }

    // Getters
    public String getDepartment() { return department; }
    public List<String> getCertifications() { return new ArrayList<>(certifications); }
    public String getShift() { return shift; }
    public boolean isOnDuty() { return isOnDuty; }

    // Setters
    public void setDepartment(String department) { this.department = department; }
    public void setCertifications(List<String> certifications) { 
        this.certifications = new ArrayList<>(certifications); 
    }
    public void setShift(String shift) { this.shift = shift; }
    public void setOnDuty(boolean onDuty) { isOnDuty = onDuty; }

    // Additional methods
    public void addCertification(String certification) {
        this.certifications.add(certification);
    }

    public void removeCertification(String certification) {
        this.certifications.remove(certification);
    }

    @Override
    public String toString() {
        return String.format("%s, Department: %s, Shift: %s, On Duty: %s", 
            super.toString(), department, shift, isOnDuty);
    }
} 