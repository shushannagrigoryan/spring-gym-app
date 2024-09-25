package authtest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.auth.TraineeAuth;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.services.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TraineeAuthTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeAuth traineeAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTraineeAuthSuccess() {
        //given
        String username = "A.A";
        String password = "password12";
        UserEntity user = new UserEntity();
        user.setPassword(password);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(traineeService.getTraineeByUsername(username)).thenReturn(trainee);

        //when
        boolean result = traineeAuth.traineeAuth(username, password);

        //then
        assertTrue(result);
        verify(traineeService).getTraineeByUsername(username);
    }

    @Test
    void testTraineeAuthInvalidUsername() {
        //given
        String username = "A.A";
        when(traineeService.getTraineeByUsername(username)).thenReturn(null);

        //then
        assertThrows(GymIllegalArgumentException.class,
                () -> traineeAuth.traineeAuth(username, "password12"));
        verify(traineeService).getTraineeByUsername(username);
    }

    @Test
    void testTraineeAuthInvalidPassword() {
        // Given
        String username = "A.A";
        String realPassword = "password12";

        String wrongPassword = "password34";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(realPassword);

        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);

        //when
        when(traineeService.getTraineeByUsername(username)).thenReturn(trainee);

        // then
        assertThrows(GymIllegalArgumentException.class,
                () -> traineeAuth.traineeAuth(username, wrongPassword),
                String.format("Incorrect password for trainee with username: %s", username));
        verify(traineeService).getTraineeByUsername(username);
    }
}
