package validatorstest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintValidatorContext;
import org.example.validators.DateTimeFormatValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DateTimeFormatValidatorTest {
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @InjectMocks
    private DateTimeFormatValidator dateTimeFormatValidator;

    @Test
    public void testIsValidNull() {
        //when
        boolean result = dateTimeFormatValidator.isValid(null, constraintValidatorContext);

        //then
        assertTrue(result);
    }

    @Test
    public void testIsValidSuccess() {
        //given
        String dateTime = "2024-12-04T12:07:00";

        //when
        boolean result = dateTimeFormatValidator.isValid(dateTime, constraintValidatorContext);

        //then
        assertTrue(result);
    }

    @Test
    public void testIsValidFailure() {
        //given
        String dateTime = "2024-12-04T12";

        //when
        boolean result = dateTimeFormatValidator.isValid(dateTime, constraintValidatorContext);

        //then
        assertFalse(result);
    }
}
