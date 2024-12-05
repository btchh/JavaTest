package Hospital.Enums;

public enum AppointmentStatus {
    SCHEDULED("Appointment has been scheduled"),
    CONFIRMED("Appointment has been confirmed"),
    IN_PROGRESS("Appointment is currently in progress"),
    COMPLETED("Appointment has been completed"),
    CANCELLED("Appointment has been cancelled"),
    RESCHEDULED("Appointment has been rescheduled"),
    NO_SHOW("Patient did not show up for appointment");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 