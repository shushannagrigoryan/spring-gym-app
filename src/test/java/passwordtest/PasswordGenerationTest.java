package passwordtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;
import org.example.password.PasswordGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordGenerationTest {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@#$%^&*+-=]{10}$");

    @InjectMocks
    private PasswordGeneration passwordGeneration;

    @Test
    public void testGeneratePassword() {
        //when
        String result = passwordGeneration.generatePassword();

        //then
        assertEquals(10, result.length());
        assertTrue(PASSWORD_PATTERN.matcher(result).matches());
    }
}
