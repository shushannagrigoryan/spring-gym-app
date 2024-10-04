//package authtest;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.example.auth.TrainerAuth;
//import org.example.auth.TrainerAuthAspect;
//import org.example.exceptions.GymIllegalArgumentException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Value;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerAuthAspectTest {
//    @Value("${trainer-auth.username}")
//    private String username;
//    @Value("${trainer-auth.password}")
//    private String password;
//    @Mock
//    private TrainerAuth trainerAuth;
//    @InjectMocks
//    private TrainerAuthAspect trainerAuthAspect;
//
//    @Test
//    public void testTrainerAuthenticationSuccess() {
//        //given
//        when(trainerAuth.trainerAuth(username, password)).thenReturn(true);
//
//        //when
//        trainerAuthAspect.trainerAuthentication();
//
//        //then
//        verify(trainerAuth).trainerAuth(username, password);
//    }
//
//    @Test
//    public void testTrainerAuthenticationFailure() {
//        //given
//        doThrow(GymIllegalArgumentException.class).when(trainerAuth)
//                .trainerAuth(username, password);
//
//        //then
//        assertThrows(GymIllegalArgumentException.class,
//                () -> trainerAuthAspect.trainerAuthentication());
//        verify(trainerAuth).trainerAuth(username, password);
//    }
//}
