//package authtest;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//import org.example.auth.UserAuth;
//import org.example.entity.UserEntity;
//import org.example.exceptions.GymAuthenticationException;
//import org.example.services.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//public class UserAuthTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserAuth userAuth;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testTraineeAuthSuccess() {
//        //given
//        String username = "A.A";
//        String password = "password12";
//        UserEntity user = new UserEntity();
//        user.setPassword(password);
//        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
//
//        //when
//        userAuth.userAuth(username, password);
//
//        //then
//        verify(userService).getUserByUsername(username);
//    }
//
//    @Test
//    void testTraineeAuthInvalidUsername() {
//        //given
//        String username = "A.A";
//        when(userService.getUserByUsername(username)).thenReturn(Optional.empty());
//
//        //then
//        assertThrows(GymAuthenticationException.class,
//                () -> userAuth.userAuth(username, "password12"));
//        verify(userService).getUserByUsername(username);
//    }
//
//    @Test
//    void testTraineeAuthInvalidPassword() {
//        // Given
//        String username = "A.A";
//        String realPassword = "password12";
//
//        String wrongPassword = "password34";
//
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        user.setPassword(realPassword);
//
//        //when
//        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
//
//        // then
//        assertThrows(GymAuthenticationException.class,
//                () -> userAuth.userAuth(username, wrongPassword),
//                String.format("Incorrect password for trainee with username: %s", username));
//        verify(userService).getUserByUsername(username);
//    }
//
//    @Test
//    void testNullUsernameOrPassword() {
//        // Given
//        String realPassword = "password12";
//
//        String wrongPassword = "password34";
//
//        UserEntity user = new UserEntity();
//        user.setPassword(realPassword);
//
//        // then
//        assertThrows(GymAuthenticationException.class,
//                () -> userAuth.userAuth(null, wrongPassword),
//                "Please enter username and password.");
//        verify(userService, times(0)).getUserByUsername(null);
//    }
//}
