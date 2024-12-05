package Hospital.Utils;

import Hospital.Interfaces.Id;
import Hospital.Enums.UserRole;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class IdGenerator implements Id {
    private static IdGenerator instance;
    private static final ConcurrentHashMap<UserRole, Integer> counters = new ConcurrentHashMap<>();

    static {
        for (UserRole role : UserRole.values()) {
            counters.put(role, 0);
        }
    }

    private IdGenerator() {}

    public static IdGenerator getInstance() {
        if (instance == null) {
            synchronized (IdGenerator.class) {
                if (instance == null) {
                    instance = new IdGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String generateId(UserRole role) {
        int currentCount = counters.compute(role, (key, val) -> val + 1);
        return String.format("%s%04d", role.getPrefix(), currentCount);
    }

    @Override
    public String isValidId(String id) {
        if (id == null || id.isEmpty()) {
            return "";
        }

        for (UserRole role : UserRole.values()) {
            String pattern = "^" + role.getPrefix() + "\\d{4}$";
            if (Pattern.matches(pattern, id)) {
                return id;
            }
        }
        return "";
    }
} 