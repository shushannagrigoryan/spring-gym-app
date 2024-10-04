//package authtest;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.example.auth.TraineeAuth;
//import org.example.auth.TraineeAuthAspect;
//import org.example.exceptions.GymIllegalArgumentException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Value;
//
//@ExtendWith(MockitoExtension.class)
//public class TraineeAuthAspectTest {
//    @Value("${trainee-auth.username}")
//    private String username;
//    @Value("${trainee-auth.password}")
//    private String password;
//    @Mock
//    private TraineeAuth traineeAuth;
//    @InjectMocks
//    private TraineeAuthAspect traineeAuthAspect;
//
//    @Test
//    public void testTraineeAuthenticationSuccess() {
//        //given
//        when(traineeAuth.traineeAuth(username, password)).thenReturn(true);
//
//        //when
//        traineeAuthAspect.traineeAuthentication();
//
//        //then
//        verify(traineeAuth).traineeAuth(username, password);
//    }
//
//    @Test
//    public void testTraineeAuthenticationFailure() {
//        //given
//        doThrow(GymIllegalArgumentException.class).when(traineeAuth)
//                        .traineeAuth(username, password);
//
//        //then
//        assertThrows(GymIllegalArgumentException.class,
//                () -> traineeAuthAspect.traineeAuthentication());
//        verify(traineeAuth).traineeAuth(username, password);
//    }
//}
