package passwordtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.password.PasswordGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordGenerationTest {
    @InjectMocks
    private PasswordGeneration passwordGeneration;

    @Test
    public void testGeneratePassword() {
        //when
        String result = passwordGeneration.generatePassword();

        //then
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c >= 32 && c < 127);
        }
    }
}
