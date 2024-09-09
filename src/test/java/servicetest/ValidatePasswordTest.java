package servicetest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.services.ValidatePassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidatePasswordTest {
    @InjectMocks
    private ValidatePassword validatePassword;

    @Test
    public void testPasswordNotValidNullCase() {
        assertTrue(validatePassword.passwordNotValid(null));
    }

    @Test
    public void testPasswordNotValidInvalidCase() {
        //given
        String password = "invalidPassword";

        //then
        assertTrue(validatePassword.passwordNotValid(password));
    }

    @Test
    public void testPasswordNotValidValidCase() {
        //given
        String password = "26$%pass=i";

        //then
        assertFalse(validatePassword.passwordNotValid(password));
    }



}
