package validationtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.example.dto.TraineeDto;
import org.example.validation.TraineeValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeValidationTest {
    @InjectMocks
    private TraineeValidation traineeValidation;

    @Test
    public void testValidateTraineeSuccess() {
        //given
        TraineeDto traineeDto = new TraineeDto("A", "B", LocalDate.now(), "address");

        assertDoesNotThrow(() -> traineeValidation.validateTrainee(traineeDto));
    }

    @Test
    public void testValidateTraineeNullFirstName() {
        //given
        TraineeDto traineeDto = new TraineeDto(null, "B", LocalDate.now(), "address");

        assertThrows(RuntimeException.class,
                () -> traineeValidation.validateTrainee(traineeDto),
                "Trainee firstName is required.");
    }

    @Test
    public void testValidateTraineeNullLastName() {
        //given
        TraineeDto traineeDto = new TraineeDto("A", null, LocalDate.now(), "address");

        assertThrows(RuntimeException.class,
                () -> traineeValidation.validateTrainee(traineeDto),
                "Trainee lastName is required.");
    }

    @Test
    public void testValidateTraineeNullDateOfBirth() {
        //given
        TraineeDto traineeDto = new TraineeDto("A", "B", null, "address");

        assertThrows(RuntimeException.class,
                () -> traineeValidation.validateTrainee(traineeDto),
                "Trainee dateOfBirth is required.");
    }

    @Test
    public void testValidateTraineeNullAddress() {
        //given
        TraineeDto traineeDto = new TraineeDto("A", "B", LocalDate.now(), null);

        assertThrows(RuntimeException.class,
                () -> traineeValidation.validateTrainee(traineeDto), "Address  is required.");
    }

}
