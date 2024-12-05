package Hospital.Entities;

import java.time.LocalDate;

public class Prescription {
    private final String prescriptionId;
    private final Patient patient;
    private final Doctor doctor;
    private final String medication;
    private final String dosage;
    private final String frequency;
    private final int duration; // in days
    private LocalDate startDate;
    private boolean isActive;
    private static int prescriptionCounter = 0;

    public Prescription(Patient patient, Doctor doctor, String medication,
                       String dosage, String frequency, int duration) {
        this.prescriptionId = generatePrescriptionId();
        this.patient = patient;
        this.doctor = doctor;
        this.medication = medication;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.startDate = LocalDate.now();
        this.isActive = true;
    }

    private synchronized String generatePrescriptionId() {
        return String.format("RX%04d", ++prescriptionCounter);
    }

    // Getters
    public String getPrescriptionId() { return prescriptionId; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getMedication() { return medication; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public int getDuration() { return duration; }
    public LocalDate getStartDate() { return startDate; }
    public boolean isActive() { return isActive; }

    // Methods
    public void deactivate() {
        this.isActive = false;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(startDate.plusDays(duration));
    }

    public LocalDate getEndDate() {
        return startDate.plusDays(duration);
    }

    public int getRemainingDays() {
        if (isExpired()) return 0;
        return (int) (getEndDate().toEpochDay() - LocalDate.now().toEpochDay());
    }

    @Override
    public String toString() {
        return String.format("Prescription ID: %s, Medication: %s, Dosage: %s, Frequency: %s, " +
                           "Duration: %d days, Active: %s, Remaining: %d days",
            prescriptionId, medication, dosage, frequency, duration, 
            isActive ? "Yes" : "No", getRemainingDays());
    }
} 