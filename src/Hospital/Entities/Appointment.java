package Hospital.Entities;

import Hospital.Enums.AppointmentStatus;
import java.time.LocalDateTime;

public class Appointment {
    private final String appointmentId;
    private final Patient patient;
    private final Doctor doctor;
    private LocalDateTime dateTime;
    private String purpose;
    private AppointmentStatus status;
    private String notes;
    private static int appointmentCounter = 0;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime dateTime, String purpose) {
        this.appointmentId = generateAppointmentId();
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.purpose = purpose;
        this.status = AppointmentStatus.SCHEDULED;
        this.notes = "";
    }

    private synchronized String generateAppointmentId() {
        return String.format("APT%04d", ++appointmentCounter);
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getPurpose() { return purpose; }
    public AppointmentStatus getStatus() { return status; }
    public String getNotes() { return notes; }

    // Setters
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }

    // Additional methods
    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
        this.doctor.setAvailable(true);
    }

    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
        this.doctor.setAvailable(true);
    }

    public void reschedule(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
        this.status = AppointmentStatus.RESCHEDULED;
    }

    public void markNoShow() {
        this.status = AppointmentStatus.NO_SHOW;
        this.doctor.setAvailable(true);
    }

    public void startAppointment() {
        this.status = AppointmentStatus.IN_PROGRESS;
        this.doctor.setAvailable(false);
    }

    @Override
    public String toString() {
        return String.format("Appointment ID: %s, Patient: %s, Doctor: %s, DateTime: %s, Status: %s",
            appointmentId, patient.getFirstName() + " " + patient.getLastName(),
            doctor.getFirstName() + " " + doctor.getLastName(),
            dateTime, status);
    }
} 