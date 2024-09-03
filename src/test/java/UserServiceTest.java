import org.example.dto.TraineeDto;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private UserService userService;

    @Test
    public void testGenerateUsernameNotTaken(){
        String firstName = "Jack";
        String lastName = "Smith";
        String expectedUsername = "JackSmith";

        when(traineeService.getTraineeByUsername(expectedUsername)).thenReturn(null);
        when(trainerService.getTrainerByUsername(expectedUsername)).thenReturn(null);

        String actualUsername = userService.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, actualUsername);
    }

}
