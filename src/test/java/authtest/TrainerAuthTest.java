package authtest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.auth.TrainerAuth;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.services.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TrainerAuthTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerAuth trainerAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTrainerAuthSuccess() {
        //given
        String username = "A.A";
        String password = "password12";
        UserEntity user = new UserEntity();
        user.setPassword(password);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(trainerService.getTrainerByUsername(username)).thenReturn(trainer);

        //when
        boolean result = trainerAuth.trainerAuth(username, password);

        //then
        assertTrue(result);
        verify(trainerService).getTrainerByUsername(username);
    }

    @Test
    void testTrainerAuthInvalidUsername() {
        //given
        String username = "A.A";
        when(trainerService.getTrainerByUsername(username)).thenReturn(null);

        //then
        assertThrows(GymIllegalArgumentException.class,
                () -> trainerAuth.trainerAuth(username, "password12"));
        verify(trainerService).getTrainerByUsername(username);
    }

    @Test
    void testTrainerAuthInvalidPassword() {
        // Given
        String username = "A.A";
        String realPassword = "password12";

        String wrongPassword = "password34";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(realPassword);

        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);

        //when
        when(trainerService.getTrainerByUsername(username)).thenReturn(trainer);

        // then
        assertThrows(GymIllegalArgumentException.class,
                () -> trainerAuth.trainerAuth(username, wrongPassword),
                String.format("Incorrect password for trainer with username: %s", username));
        verify(trainerService).getTrainerByUsername(username);
    }
}
