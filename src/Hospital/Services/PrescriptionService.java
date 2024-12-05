package Hospital.Services;

import Hospital.Entities.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionService {
    private static PrescriptionService instance;
    private final List<Prescription> prescriptions;

    private PrescriptionService() {
        this.prescriptions = new ArrayList<>();
    }

    public static synchronized PrescriptionService getInstance() {
        if (instance == null) {
            instance = new PrescriptionService();
        }
        return instance;
    }

    // Create new prescription
    public Prescription createPrescription(Patient patient, Doctor doctor, String medication,
                                         String dosage, String frequency, int duration) {
        // Check for active prescriptions of the same medication
        boolean hasActivePrescription = prescriptions.stream()
            .filter(p -> p.getPatient().equals(patient))
            .filter(p -> p.getMedication().equals(medication))
            .anyMatch(p -> p.isActive() && !p.isExpired());

        if (hasActivePrescription) {
            throw new IllegalStateException("Patient already has an active prescription for this medication");
        }

        Prescription prescription = new Prescription(patient, doctor, medication, 
                                                  dosage, frequency, duration);
        prescriptions.add(prescription);
        return prescription;
    }

    // Get patient's active prescriptions
    public List<Prescription> getPatientActivePrescriptions(Patient patient) {
        return prescriptions.stream()
            .filter(p -> p.getPatient().equals(patient))
            .filter(p -> p.isActive() && !p.isExpired())
            .collect(Collectors.toList());
    }

    // Get patient's prescription history
    public List<Prescription> getPatientPrescriptionHistory(Patient patient) {
        return prescriptions.stream()
            .filter(p -> p.getPatient().equals(patient))
            .collect(Collectors.toList());
    }

    // Get doctor's prescriptions
    public List<Prescription> getDoctorPrescriptions(Doctor doctor) {
        return prescriptions.stream()
            .filter(p -> p.getDoctor().equals(doctor))
            .collect(Collectors.toList());
    }

    // Find prescription by ID
    public Prescription findPrescriptionById(String prescriptionId) {
        return prescriptions.stream()
            .filter(p -> p.getPrescriptionId().equals(prescriptionId))
            .findFirst()
            .orElse(null);
    }

    // Deactivate prescription
    public void deactivatePrescription(String prescriptionId) {
        Prescription prescription = findPrescriptionById(prescriptionId);
        if (prescription != null) {
            prescription.deactivate();
        }
    }

    // Get expiring prescriptions
    public List<Prescription> getExpiringPrescriptions(int daysThreshold) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return prescriptions.stream()
            .filter(p -> p.isActive() && !p.isExpired())
            .filter(p -> !p.getEndDate().isAfter(thresholdDate))
            .collect(Collectors.toList());
    }

    // Get expired prescriptions
    public List<Prescription> getExpiredPrescriptions() {
        return prescriptions.stream()
            .filter(Prescription::isExpired)
            .collect(Collectors.toList());
    }

    // Get all prescriptions
    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions);
    }
} 