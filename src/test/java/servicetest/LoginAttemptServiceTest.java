//package servicetest;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import org.example.entity.LoginAttemptEntity;
//import org.example.entity.UserEntity;
//import org.example.exceptions.GymEntityNotFoundException;
//import org.example.repositories.LoginAttemptRepository;
//import org.example.services.LoginAttemptService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class LoginAttemptServiceTest {
//    private static final int MAX_FAIL_ATTEMPT = 3;
//    private static final int BLOCK_TIME = 5;
//    @Mock
//    private LoginAttemptRepository loginAttemptRepository;
//    @InjectMocks
//    private LoginAttemptService loginAttemptService;
//
//    @Test
//    public void testLoginFailed() {
//        //given
//        String username = "username";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
//        when(loginAttemptRepository.findByUser_Username(user.getUsername())).thenReturn(Optional.of(loginAttempt));
//        when(loginAttemptRepository.save(loginAttempt)).thenReturn(loginAttempt);
//
//        //when
//        loginAttemptService.loginFailed(user);
//
//        //then
//        verify(loginAttemptRepository).findByUser_Username(user.getUsername());
//        verify(loginAttemptRepository).save(loginAttempt);
//    }
//
//    @Test
//    public void testLoginFailedClearingLoginAttempt() {
//        //given
//        String username = "username";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
//        loginAttempt.setLastFailedAttempt(LocalDateTime.now().minusMinutes(2 * BLOCK_TIME));
//        when(loginAttemptRepository.findByUser_Username(user.getUsername())).thenReturn(Optional.of(loginAttempt));
//        when(loginAttemptRepository.save(loginAttempt)).thenReturn(loginAttempt);
//
//        //when
//        loginAttemptService.loginFailed(user);
//
//        //then
//        verify(loginAttemptRepository, times(2)).findByUser_Username(user.getUsername());
//        verify(loginAttemptRepository, times(2)).save(loginAttempt);
//    }
//
//    @Test
//    public void testIsBlockedTrue() {
//        //given
//        String username = "username";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
//        loginAttempt.setFailedCount(MAX_FAIL_ATTEMPT);
//        loginAttempt.setLastFailedAttempt(LocalDateTime.now().plusMinutes(BLOCK_TIME * 2));
//        when(loginAttemptRepository.findByUser_Username(user.getUsername())).thenReturn(Optional.of(loginAttempt));
//
//        //when
//        boolean result = loginAttemptService.isBlocked(user);
//
//        //then
//        assertTrue(result);
//        verify(loginAttemptRepository).findByUser_Username(username);
//    }
//
//    @Test
//    public void testIsBlockedFalse() {
//        //given
//        String username = "username";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        when(loginAttemptRepository.findByUser_Username(user.getUsername())).thenReturn(Optional.empty());
//
//        //when
//        boolean result = loginAttemptService.isBlocked(user);
//
//        //then
//        assertFalse(result);
//        verify(loginAttemptRepository).findByUser_Username(username);
//    }
//
//    @Test
//    public void testClearFailedLoginSuccess() {
//        //given
//        String username = "username";
//        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
//        when(loginAttemptRepository.findByUser_Username(username)).thenReturn(Optional.of(loginAttempt));
//        when(loginAttemptRepository.save(loginAttempt)).thenReturn(loginAttempt);
//
//        //when
//        loginAttemptService.clearFailedLogin(username);
//
//        //then
//        verify(loginAttemptRepository).findByUser_Username(username);
//        verify(loginAttemptRepository).save(loginAttempt);
//    }
//
//    @Test
//    public void testClearFailedLoginFailure() {
//        //given
//        String username = "username";
//        when(loginAttemptRepository.findByUser_Username(username)).thenThrow(GymEntityNotFoundException.class);
//
//        //then
//        assertThrows(GymEntityNotFoundException.class, () -> loginAttemptService.clearFailedLogin(username),
//            "FailedAttemptEntity not found.");
//    }
//}
