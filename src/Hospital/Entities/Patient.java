package Hospital.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private final String patientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String bloodType;
    private List<String> allergies;
    private List<String> medicalHistory;
    private static int patientCounter = 0;

    public Patient(String firstName, String lastName, String email, String phoneNumber,
                  LocalDate dateOfBirth, String bloodType) {
        this.patientId = generatePatientId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.allergies = new ArrayList<>();
        this.medicalHistory = new ArrayList<>();
    }

    private synchronized String generatePatientId() {
        return String.format("PAT%04d", ++patientCounter);
    }

    // Getters
    public String getPatientId() { return patientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getBloodType() { return bloodType; }
    public List<String> getAllergies() { return new ArrayList<>(allergies); }
    public List<String> getMedicalHistory() { return new ArrayList<>(medicalHistory); }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    // Additional methods
    public void addAllergy(String allergy) {
        this.allergies.add(allergy);
    }

    public void addMedicalHistory(String record) {
        this.medicalHistory.add(record);
    }

    @Override
    public String toString() {
        return String.format("Patient ID: %s, Name: %s %s, Blood Type: %s", 
            patientId, firstName, lastName, bloodType);
    }
} 