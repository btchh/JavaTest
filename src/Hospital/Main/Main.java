package Hospital.Main;

import Hospital.Entities.*;
import Hospital.Services.*;
import Hospital.Enums.UserRole;
import Hospital.Enums.Permission;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Doctor> doctors = new ArrayList<>();
    private static final List<Nurse> nurses = new ArrayList<>();
    private static final List<Patient> patients = new ArrayList<>();
    private static final AppointmentService appointmentService = AppointmentService.getInstance();
    private static final MedicalRecordService medicalRecordService = MedicalRecordService.getInstance();
    private static final PrescriptionService prescriptionService = PrescriptionService.getInstance();
    private static final AuthenticationService authService = AuthenticationService.getInstance();
    private static final RBACService rbacService = RBACService.getInstance();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static Person currentUser = null;

    public static void main(String[] args) {
        initializeSampleData();
        
        while (true) {
            if (currentUser == null) {
                displayLoginMenu();
            } else {
                displayMainMenu();
            }
            
            int choice = getIntInput("Enter your choice: ");
            processMenuChoice(choice);
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Hospital Management System ===");
        System.out.println("1. Add New Staff");
        System.out.println("2. Register New Patient");
        System.out.println("3. Schedule Appointment");
        System.out.println("4. View Appointments");
        System.out.println("5. Create Medical Record");
        System.out.println("6. Create Prescription");
        System.out.println("7. View All Records");
        System.out.println("0. Exit");
    }

    private static void displayLoginMenu() {
        System.out.println("\n=== Hospital Management System - Login ===");
        System.out.println("1. Login");
        System.out.println("0. Exit");
    }

    private static void processMenuChoice(int choice) {
        try {
            if (currentUser == null) {
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice");
                }
                return;
            }

            // Main menu choices
            switch (choice) {
                case 1:
                    if (rbacService.hasPermission(currentUser, Permission.ADD_STAFF)) {
                        addNewStaff();
                    } else {
                        System.out.println("Access denied: Insufficient permissions");
                    }
                    break;
                case 2:
                    registerNewPatient();
                    break;
                case 3:
                    scheduleAppointment();
                    break;
                case 4:
                    viewAppointments();
                    break;
                case 5:
                    createMedicalRecord();
                    break;
                case 6:
                    createPrescription();
                    break;
                case 7:
                    viewAllRecords();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } catch (SecurityException e) {
            System.out.println("Access denied: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void login() {
        String email = getStringInput("Enter email: ");
        String password = getStringInput("Enter password: ");
        try {
            currentUser = authService.login(email, password);
            System.out.println("Welcome, " + currentUser.getFirstName() + "!");
        } catch (SecurityException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void logout() {
        if (currentUser != null) {
            authService.logout(currentUser.getEmail());
            currentUser = null;
            System.out.println("Logged out successfully");
        }
    }

    private static void addNewStaff() {
        System.out.println("\n=== Add New Staff ===");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone number: ");
        String password = getStringInput("Enter password: ");
        
        System.out.println("\nSelect role:");
        System.out.println("1. Doctor");
        System.out.println("2. Nurse");
        System.out.println("3. Receptionist");
        System.out.println("4. Admin");
        
        int roleChoice = getIntInput("Enter choice: ");
        UserRole role;
        String specialization = "";
        
        switch (roleChoice) {
            case 1:
                role = UserRole.DOCTOR;
                specialization = getStringInput("Enter specialization: ");
                break;
            case 2:
                role = UserRole.NURSE;
                specialization = getStringInput("Enter department: ");
                break;
            case 3:
                role = UserRole.RECEPTIONIST;
                break;
            case 4:
                role = UserRole.ADMIN;
                break;
            default:
                System.out.println("Invalid role choice");
                return;
        }

        try {
            Person newUser = authService.registerNewUser(currentUser, email, password, 
                firstName, lastName, phone, role, specialization);
            System.out.println("Staff member added successfully! ID: " + newUser.getId());
        } catch (SecurityException e) {
            System.out.println("Access denied: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registerNewPatient() {
        System.out.println("\n=== Register New Patient ===");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone number: ");
        String dateStr = getStringInput("Enter date of birth (dd/MM/yyyy): ");
        String bloodType = getStringInput("Enter blood type: ");

        try {
            LocalDate dob = LocalDate.parse(dateStr, dateFormatter);
            Patient patient = new Patient(firstName, lastName, email, phone, dob, bloodType);
            
            // Add allergies
            while (true) {
                String allergy = getStringInput("Enter allergy (or 'done' to finish): ");
                if (allergy.equalsIgnoreCase("done")) break;
                patient.addAllergy(allergy);
            }

            // Add medical history
            while (true) {
                String history = getStringInput("Enter medical history item (or 'done' to finish): ");
                if (history.equalsIgnoreCase("done")) break;
                patient.addMedicalHistory(history);
            }

            patients.add(patient);
            System.out.println("Patient registered successfully! ID: " + patient.getPatientId());
        } catch (Exception e) {
            System.out.println("Error registering patient: " + e.getMessage());
        }
    }

    private static void createMedicalRecord() {
        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("No patients or doctors available.");
            return;
        }

        System.out.println("\n=== Create Medical Record ===");
        
        // Select patient and doctor
        Patient patient = selectPatient();
        Doctor doctor = selectDoctor();
        if (patient == null || doctor == null) return;

        String diagnosis = getStringInput("Enter diagnosis: ");
        String treatment = getStringInput("Enter treatment plan: ");
        String notes = getStringInput("Enter additional notes: ");

        MedicalRecord record = medicalRecordService.createMedicalRecord(
            patient, doctor, diagnosis, treatment, notes
        );

        // Add test results
        while (true) {
            String result = getStringInput("Enter test result (or 'done' to finish): ");
            if (result.equalsIgnoreCase("done")) break;
            record.addTestResult(result);
        }

        System.out.println("Medical record created successfully! ID: " + record.getRecordId());
    }

    private static void createPrescription() {
        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("No patients or doctors available.");
            return;
        }

        System.out.println("\n=== Create Prescription ===");
        
        // Select patient and doctor
        Patient patient = selectPatient();
        Doctor doctor = selectDoctor();
        if (patient == null || doctor == null) return;

        String medication = getStringInput("Enter medication name: ");
        String dosage = getStringInput("Enter dosage: ");
        String frequency = getStringInput("Enter frequency (e.g., '3 times daily'): ");
        int duration = getIntInput("Enter duration in days: ");

        try {
            Prescription prescription = prescriptionService.createPrescription(
                patient, doctor, medication, dosage, frequency, duration
            );
            System.out.println("Prescription created successfully! ID: " + prescription.getPrescriptionId());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllRecords() {
        System.out.println("\n=== System Records ===");
        
        System.out.println("\nDoctors (" + doctors.size() + "):");
        doctors.forEach(System.out::println);
        
        System.out.println("\nNurses (" + nurses.size() + "):");
        nurses.forEach(System.out::println);
        
        System.out.println("\nPatients (" + patients.size() + "):");
        patients.forEach(System.out::println);
        
        System.out.println("\nAppointments:");
        appointmentService.getAllAppointments().forEach(System.out::println);
        
        System.out.println("\nMedical Records:");
        medicalRecordService.getAllMedicalRecords().forEach(System.out::println);
        
        System.out.println("\nPrescriptions:");
        prescriptionService.getAllPrescriptions().forEach(System.out::println);
    }

    private static Patient selectPatient() {
        System.out.println("\nAvailable Patients:");
        for (int i = 0; i < patients.size(); i++) {
            System.out.println((i + 1) + ". " + patients.get(i));
        }
        int index = getIntInput("Select patient number: ") - 1;
        return (index >= 0 && index < patients.size()) ? patients.get(index) : null;
    }

    private static Doctor selectDoctor() {
        System.out.println("\nAvailable Doctors:");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". " + doctors.get(i));
        }
        int index = getIntInput("Select doctor number: ") - 1;
        return (index >= 0 && index < doctors.size()) ? doctors.get(index) : null;
    }

    private static void initializeSampleData() {
        // Add sample doctors
        doctors.add(new Doctor("John", "Smith", "john.smith@hospital.com", "123-456-7890",
                             "Cardiology", Arrays.asList("MD", "Cardiology Specialist")));
        doctors.add(new Doctor("Sarah", "Jones", "sarah.jones@hospital.com", "123-456-7891",
                             "Pediatrics", Arrays.asList("MD", "Pediatrics Specialist")));

        // Add sample nurses
        nurses.add(new Nurse("Mary", "Johnson", "mary.j@hospital.com", "123-456-7892",
                           "Cardiology", Arrays.asList("RN", "Critical Care")));
        nurses.add(new Nurse("Robert", "Brown", "robert.b@hospital.com", "123-456-7893",
                           "Pediatrics", Arrays.asList("RN", "Pediatric Care")));
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void scheduleAppointment() {
        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("No patients or doctors available.");
            return;
        }

        System.out.println("\n=== Schedule Appointment ===");
        Patient patient = selectPatient();
        Doctor doctor = selectDoctor();
        if (patient == null || doctor == null) return;

        String dateStr = getStringInput("Enter appointment date (dd/MM/yyyy): ");
        String timeStr = getStringInput("Enter appointment time (HH:mm): ");
        String purpose = getStringInput("Enter appointment purpose: ");

        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            LocalDateTime dateTime = date.atTime(
                Integer.parseInt(timeStr.split(":")[0]),
                Integer.parseInt(timeStr.split(":")[1])
            );

            Appointment appointment = appointmentService.scheduleAppointment(
                patient, doctor, dateTime, purpose
            );
            System.out.println("Appointment scheduled successfully!");
            System.out.println(appointment);
        } catch (Exception e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    private static void viewAppointments() {
        System.out.println("\n=== All Appointments ===");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        appointments.forEach(System.out::println);
    }
} 