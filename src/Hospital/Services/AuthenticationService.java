package Hospital.Services;

import Hospital.Entities.*;
import Hospital.Enums.UserRole;
import Hospital.Enums.Permission;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static AuthenticationService instance;
    private final Map<String, String> userCredentials; // email -> password
    private final Map<String, Person> loggedInUsers; // email -> user
    private final RBACService rbacService;

    private AuthenticationService() {
        this.userCredentials = new HashMap<>();
        this.loggedInUsers = new HashMap<>();
        this.rbacService = RBACService.getInstance();
        
        // Add default admin account
        String adminEmail = "admin@hospital.com";
        userCredentials.put(adminEmail, "admin123");
        
        // Create and store default admin user
        Admin defaultAdmin = new Admin("System", "Admin", adminEmail, "000-000-0000");
        loggedInUsers.put(adminEmail, defaultAdmin);
    }

    public static synchronized AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public Person registerNewUser(Person admin, String email, String password, 
                                String firstName, String lastName, String phone,
                                UserRole role, String specialization) {
        // Check if admin has permission
        rbacService.checkPermission(admin, Permission.ADD_STAFF);

        // Check if email already exists
        if (userCredentials.containsKey(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user based on role
        Person newUser;
        switch (role) {
            case DOCTOR:
                newUser = new Doctor(firstName, lastName, email, phone, specialization, new java.util.ArrayList<>());
                break;
            case NURSE:
                newUser = new Nurse(firstName, lastName, email, phone, specialization, new java.util.ArrayList<>());
                break;
            case RECEPTIONIST:
                newUser = new Receptionist(firstName, lastName, email, phone, "Front Desk");
                break;
            case ADMIN:
                // Only super admin can create other admins
                if (!admin.getRole().equals(UserRole.ADMIN)) {
                    throw new SecurityException("Only administrators can create admin accounts");
                }
                newUser = new Admin(firstName, lastName, email, phone);
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }

        // Store credentials
        userCredentials.put(email, password);
        // Store user in loggedInUsers map
        loggedInUsers.put(email, newUser);
        return newUser;
    }

    public Person login(String email, String password) {
        // Verify credentials
        String storedPassword = userCredentials.get(email);
        if (storedPassword == null || !storedPassword.equals(password)) {
            throw new SecurityException("Invalid credentials");
        }

        // Get user from appropriate service based on role
        Person user = loggedInUsers.get(email);
        if (user == null) {
            throw new SecurityException("User not found");
        }

        return user;
    }

    public void logout(String email) {
        loggedInUsers.remove(email);
    }

    public boolean isLoggedIn(String email) {
        return loggedInUsers.containsKey(email);
    }

    public void changePassword(Person user, String oldPassword, String newPassword) {
        String email = user.getEmail();
        if (!userCredentials.get(email).equals(oldPassword)) {
            throw new SecurityException("Invalid old password");
        }
        userCredentials.put(email, newPassword);
    }
} 