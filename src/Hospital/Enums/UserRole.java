package Hospital.Enums;

public enum UserRole {
    ADMIN("ADM"),
    DOCTOR("DOC"),
    NURSE("NRS"),
    RECEPTIONIST("RCP"),
    PHARMACIST("PHR");

    private final String prefix;

    UserRole(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
} 