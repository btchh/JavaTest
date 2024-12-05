package Hospital.Enums;

public enum Permission {
    // Patient Management
    VIEW_PATIENTS,
    ADD_PATIENT,
    EDIT_PATIENT,
    DELETE_PATIENT,

    // Appointment Management
    VIEW_APPOINTMENTS,
    SCHEDULE_APPOINTMENT,
    CANCEL_APPOINTMENT,
    MODIFY_APPOINTMENT,

    // Medical Records
    VIEW_MEDICAL_RECORDS,
    CREATE_MEDICAL_RECORD,
    EDIT_MEDICAL_RECORD,
    DELETE_MEDICAL_RECORD,

    // Prescription Management
    VIEW_PRESCRIPTIONS,
    CREATE_PRESCRIPTION,
    MODIFY_PRESCRIPTION,
    CANCEL_PRESCRIPTION,

    // Staff Management
    VIEW_STAFF,
    ADD_STAFF,
    EDIT_STAFF,
    DELETE_STAFF,

    // System Administration
    SYSTEM_ADMIN,
    VIEW_LOGS,
    MANAGE_ROLES
} 