package securitytest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import org.example.security.TrainerHasPermissionOnTrainee;
import org.example.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
public class TrainerHasPermissionOnTraineeTest {
    private static final SimpleGrantedAuthority ROLE_TRAINER = new SimpleGrantedAuthority("ROLE_TRAINER");
    @Mock
    private TrainingService trainingService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private TrainerHasPermissionOnTrainee trainerHasPermissionOnTrainee;

    @Test
    public void testHasPermissionFalse() {
        //when
        boolean result = trainerHasPermissionOnTrainee.hasPermission(authentication, "username");

        //then
        assertFalse(result);
    }

    @Test
    public void testHasPermissionTrue() {
        //given
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(ROLE_TRAINER);
        when(authentication.getAuthorities()).thenAnswer(x -> authorities);
        String traineeUsername = "trainee";
        String trainerUsername = "trainer";
        when(authentication.getName()).thenReturn(trainerUsername);
        when(trainingService.trainingExists(traineeUsername, trainerUsername)).thenReturn(true);

        //when
        boolean result = trainerHasPermissionOnTrainee.hasPermission(authentication, traineeUsername);

        //then
        assertTrue(result);
    }


}
