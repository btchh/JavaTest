package Hospital.Services;

import Hospital.Entities.Appointment;
import Hospital.Entities.Doctor;
import Hospital.Entities.Patient;
import Hospital.Enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private static AppointmentService instance;
    private final List<Appointment> appointments;

    private AppointmentService() {
        this.appointments = new ArrayList<>();
    }

    public static synchronized AppointmentService getInstance() {
        if (instance == null) {
            instance = new AppointmentService();
        }
        return instance;
    }

    public Appointment scheduleAppointment(Patient patient, Doctor doctor, 
                                         LocalDateTime dateTime, String purpose) {
        if (!isDoctorAvailable(doctor, dateTime)) {
            throw new IllegalStateException("Doctor is not available at the specified time");
        }

        Appointment appointment = new Appointment(patient, doctor, dateTime, purpose);
        appointments.add(appointment);
        doctor.setAvailable(false);
        return appointment;
    }

    public void cancelAppointment(String appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.cancel();
        }
    }

    public void completeAppointment(String appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.complete();
        }
    }

    public void rescheduleAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null && isDoctorAvailable(appointment.getDoctor(), newDateTime)) {
            appointment.reschedule(newDateTime);
        }
    }

    public List<Appointment> getDoctorAppointments(Doctor doctor) {
        return appointments.stream()
            .filter(a -> a.getDoctor().equals(doctor))
            .collect(Collectors.toList());
    }

    public List<Appointment> getPatientAppointments(Patient patient) {
        return appointments.stream()
            .filter(a -> a.getPatient().equals(patient))
            .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointments.stream()
            .filter(a -> a.getStatus() == status)
            .collect(Collectors.toList());
    }

    private Appointment findAppointmentById(String appointmentId) {
        return appointments.stream()
            .filter(a -> a.getAppointmentId().equals(appointmentId))
            .findFirst()
            .orElse(null);
    }

    private boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        return appointments.stream()
            .filter(a -> a.getDoctor().equals(doctor))
            .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED 
                     && a.getStatus() != AppointmentStatus.COMPLETED)
            .noneMatch(a -> a.getDateTime().equals(dateTime));
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
} 