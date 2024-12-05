package Hospital.Services;

import Hospital.Entities.Person;
import Hospital.Enums.Permission;
import Hospital.Enums.UserRole;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RBACService {
    private static RBACService instance;
    private final Map<UserRole, Set<Permission>> rolePermissions;

    private RBACService() {
        this.rolePermissions = new HashMap<>();
        initializeRolePermissions();
    }

    public static synchronized RBACService getInstance() {
        if (instance == null) {
            instance = new RBACService();
        }
        return instance;
    }

    private void initializeRolePermissions() {
        // Admin permissions
        Set<Permission> adminPermissions = new HashSet<>();
        for (Permission permission : Permission.values()) {
            adminPermissions.add(permission);
        }
        rolePermissions.put(UserRole.ADMIN, adminPermissions);

        // Doctor permissions
        Set<Permission> doctorPermissions = new HashSet<>();
        doctorPermissions.add(Permission.VIEW_PATIENTS);
        doctorPermissions.add(Permission.VIEW_APPOINTMENTS);
        doctorPermissions.add(Permission.MODIFY_APPOINTMENT);
        doctorPermissions.add(Permission.VIEW_MEDICAL_RECORDS);
        doctorPermissions.add(Permission.CREATE_MEDICAL_RECORD);
        doctorPermissions.add(Permission.EDIT_MEDICAL_RECORD);
        doctorPermissions.add(Permission.VIEW_PRESCRIPTIONS);
        doctorPermissions.add(Permission.CREATE_PRESCRIPTION);
        doctorPermissions.add(Permission.MODIFY_PRESCRIPTION);
        rolePermissions.put(UserRole.DOCTOR, doctorPermissions);

        // Nurse permissions
        Set<Permission> nursePermissions = new HashSet<>();
        nursePermissions.add(Permission.VIEW_PATIENTS);
        nursePermissions.add(Permission.VIEW_APPOINTMENTS);
        nursePermissions.add(Permission.VIEW_MEDICAL_RECORDS);
        nursePermissions.add(Permission.VIEW_PRESCRIPTIONS);
        rolePermissions.put(UserRole.NURSE, nursePermissions);

        // Receptionist permissions
        Set<Permission> receptionistPermissions = new HashSet<>();
        receptionistPermissions.add(Permission.VIEW_PATIENTS);
        receptionistPermissions.add(Permission.ADD_PATIENT);
        receptionistPermissions.add(Permission.EDIT_PATIENT);
        receptionistPermissions.add(Permission.VIEW_APPOINTMENTS);
        receptionistPermissions.add(Permission.SCHEDULE_APPOINTMENT);
        receptionistPermissions.add(Permission.CANCEL_APPOINTMENT);
        rolePermissions.put(UserRole.RECEPTIONIST, receptionistPermissions);
    }

    public boolean hasPermission(Person user, Permission permission) {
        if (user == null || permission == null) return false;
        Set<Permission> permissions = rolePermissions.get(user.getRole());
        return permissions != null && permissions.contains(permission);
    }

    public Set<Permission> getUserPermissions(Person user) {
        if (user == null) return new HashSet<>();
        return new HashSet<>(rolePermissions.getOrDefault(user.getRole(), new HashSet<>()));
    }

    public boolean checkPermission(Person user, Permission permission) {
        if (!hasPermission(user, permission)) {
            throw new SecurityException("User does not have permission: " + permission);
        }
        return true;
    }

    // Example usage in a service method:
    public void exampleSecuredMethod(Person user, String data) {
        checkPermission(user, Permission.VIEW_MEDICAL_RECORDS);
        // If we get here, the user has permission
        // Proceed with the secured operation
    }
} 