package org.example.password;

import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordGeneration {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int startChar = 33;
    private static final int endChar = 127;

    /**
     * Generating random 10 length password.
     */
    public String generatePassword() {
        log.debug("Generating a random password.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append((char) secureRandom.nextInt(startChar, endChar));
        }
        return stringBuilder.toString();
    }
}
