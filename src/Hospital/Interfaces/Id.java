package Hospital.Interfaces;

import Hospital.Enums.UserRole;

public interface Id {
    String generateId(UserRole role);
    String isValidId(String id);
}