package securitytest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.example.security.TrainerPermissionEvaluator;
import org.example.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
public class TrainerPermissionEvaluatorTest {
    private static final SimpleGrantedAuthority ROLE_TRAINER = new SimpleGrantedAuthority("ROLE_TRAINER");
    @Mock
    private TraineeService traineeService;
    @Mock
    private Authentication authentication;
    @Mock
    private Serializable targetId;
    @InjectMocks
    private TrainerPermissionEvaluator trainerPermissionEvaluator;

    @Test
    public void testHasPermissionFalse() {
        //when
        boolean result = trainerPermissionEvaluator.hasPermission(authentication, "username",
            new Object());

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
        when(traineeService.getAssignedTrainees(trainerUsername)).thenReturn(List.of(traineeUsername));

        //when
        boolean result = trainerPermissionEvaluator.hasPermission(authentication, traineeUsername, new Object());

        //then
        assertTrue(result);
    }

    @Test
    public void testHasPermission() {
        //when
        boolean result =
            trainerPermissionEvaluator.hasPermission(authentication, targetId, null, null);

        //then
        assertFalse(result);
    }
}
