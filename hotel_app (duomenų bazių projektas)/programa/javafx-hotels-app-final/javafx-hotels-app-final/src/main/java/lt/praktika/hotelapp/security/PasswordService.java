package lt.praktika.hotelapp.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {
    private static final int LOG_ROUNDS = 12;

    public boolean matches(String rawPassword, String storedHash) {
        if (storedHash == null) return false;
        if (isBCryptHash(storedHash)) return BCrypt.checkpw(rawPassword, storedHash);
        return rawPassword.equals(storedHash);
    }

    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    public boolean needsUpgrade(String storedHash) {
        return storedHash == null || !isBCryptHash(storedHash);
    }

    private boolean isBCryptHash(String value) {
        return value != null && value.startsWith("$2");
    }
}
