package Hospital.Entities;

import Hospital.Enums.UserRole;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String specialization;
    private List<String> qualifications;
    private boolean isAvailable;
    private String schedule;

    public Doctor(String firstName, String lastName, String email, String phoneNumber,
                 String specialization, List<String> qualifications) {
        super(firstName, lastName, email, phoneNumber, UserRole.DOCTOR);
        this.specialization = specialization;
        this.qualifications = new ArrayList<>(qualifications);
        this.isAvailable = true;
        this.schedule = "9AM-5PM"; // Default schedule
    }

    // Getters
    public String getSpecialization() { return specialization; }
    public List<String> getQualifications() { return new ArrayList<>(qualifications); }
    public boolean isAvailable() { return isAvailable; }
    public String getSchedule() { return schedule; }

    // Setters
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setQualifications(List<String> qualifications) { 
        this.qualifications = new ArrayList<>(qualifications); 
    }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    // Additional methods
    public void addQualification(String qualification) {
        this.qualifications.add(qualification);
    }

    public void removeQualification(String qualification) {
        this.qualifications.remove(qualification);
    }

    @Override
    public String toString() {
        return String.format("%s, Specialization: %s, Available: %s", 
            super.toString(), specialization, isAvailable);
    }
} 