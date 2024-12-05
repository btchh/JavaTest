package Hospital.Services;

import Hospital.Entities.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordService {
    private static MedicalRecordService instance;
    private final List<MedicalRecord> medicalRecords;

    private MedicalRecordService() {
        this.medicalRecords = new ArrayList<>();
    }

    public static synchronized MedicalRecordService getInstance() {
        if (instance == null) {
            instance = new MedicalRecordService();
        }
        return instance;
    }

    // Create new medical record
    public MedicalRecord createMedicalRecord(Patient patient, Doctor doctor, 
                                           String diagnosis, String treatment, String notes) {
        MedicalRecord record = new MedicalRecord(patient, doctor, diagnosis, treatment, notes);
        medicalRecords.add(record);
        return record;
    }

    // Get patient's medical history
    public List<MedicalRecord> getPatientMedicalHistory(Patient patient) {
        return medicalRecords.stream()
            .filter(record -> record.getPatient().equals(patient))
            .collect(Collectors.toList());
    }

    // Get doctor's medical records
    public List<MedicalRecord> getDoctorMedicalRecords(Doctor doctor) {
        return medicalRecords.stream()
            .filter(record -> record.getDoctor().equals(doctor))
            .collect(Collectors.toList());
    }

    // Get medical records by date range
    public List<MedicalRecord> getMedicalRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        return medicalRecords.stream()
            .filter(record -> !record.getDateTime().isBefore(start) && 
                            !record.getDateTime().isAfter(end))
            .collect(Collectors.toList());
    }

    // Find medical record by ID
    public MedicalRecord findMedicalRecordById(String recordId) {
        return medicalRecords.stream()
            .filter(record -> record.getRecordId().equals(recordId))
            .findFirst()
            .orElse(null);
    }

    // Update medical record
    public void updateMedicalRecord(String recordId, String diagnosis, 
                                  String treatment, String notes) {
        MedicalRecord record = findMedicalRecordById(recordId);
        if (record != null) {
            if (diagnosis != null) record.setDiagnosis(diagnosis);
            if (treatment != null) record.setTreatment(treatment);
            if (notes != null) record.setNotes(notes);
        }
    }

    // Add test result to medical record
    public void addTestResult(String recordId, String testResult) {
        MedicalRecord record = findMedicalRecordById(recordId);
        if (record != null) {
            record.addTestResult(testResult);
        }
    }

    // Add prescription to medical record
    public void addPrescription(String recordId, String prescription) {
        MedicalRecord record = findMedicalRecordById(recordId);
        if (record != null) {
            record.addPrescription(prescription);
        }
    }

    // Get all medical records
    public List<MedicalRecord> getAllMedicalRecords() {
        return new ArrayList<>(medicalRecords);
    }
} 