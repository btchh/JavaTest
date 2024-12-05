package Hospital.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private final String recordId;
    private final Patient patient;
    private final Doctor doctor;
    private LocalDateTime dateTime;
    private String diagnosis;
    private String treatment;
    private List<String> prescriptions;
    private List<String> testResults;
    private String notes;
    private static int recordCounter = 0;

    public MedicalRecord(Patient patient, Doctor doctor, String diagnosis, 
                        String treatment, String notes) {
        this.recordId = generateRecordId();
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = LocalDateTime.now();
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.prescriptions = new ArrayList<>();
        this.testResults = new ArrayList<>();
    }

    private synchronized String generateRecordId() {
        return String.format("MR%04d", ++recordCounter);
    }

    // Getters
    public String getRecordId() { return recordId; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public List<String> getPrescriptions() { return new ArrayList<>(prescriptions); }
    public List<String> getTestResults() { return new ArrayList<>(testResults); }
    public String getNotes() { return notes; }

    // Setters
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public void setNotes(String notes) { this.notes = notes; }

    // Additional methods
    public void addPrescription(String prescription) {
        this.prescriptions.add(prescription);
    }

    public void addTestResult(String testResult) {
        this.testResults.add(testResult);
    }

    @Override
    public String toString() {
        return String.format("Record ID: %s, Patient: %s %s, Doctor: %s %s, Date: %s, Diagnosis: %s",
            recordId, patient.getFirstName(), patient.getLastName(),
            doctor.getFirstName(), doctor.getLastName(),
            dateTime, diagnosis);
    }
} 