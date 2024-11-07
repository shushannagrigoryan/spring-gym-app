package org.example.password;

import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordGeneration {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final char START_CHARACTER = 33;
    private static final char END_CHARACTER = 127;

    /**
     * Generating random 10 length password.
     */
    public String generatePassword() {
        log.debug("Generating a random password.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append((char) SECURE_RANDOM.nextInt(START_CHARACTER, END_CHARACTER));
        }
        return stringBuilder.toString();
    }
}
