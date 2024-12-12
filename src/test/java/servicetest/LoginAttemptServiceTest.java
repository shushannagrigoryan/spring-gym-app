package servicetest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.example.entity.LoginAttemptEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.repositories.LoginAttemptRepository;
import org.example.services.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginAttemptServiceTest {
    private static final int MAX_FAIL_ATTEMPT = 3;
    private static final int BLOCK_TIME = 5;
    @Mock
    private LoginAttemptRepository loginAttemptRepository;
    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Test
    public void testLoginFailed() {
        //given
        String userIp = "userIp";
        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
        loginAttempt.setUserIp(userIp);
        when(loginAttemptRepository.findByUserIp(userIp)).thenReturn(Optional.of(loginAttempt));
        when(loginAttemptRepository.save(loginAttempt)).thenReturn(loginAttempt);

        //when
        loginAttemptService.loginFailed(userIp);

        //then
        verify(loginAttemptRepository).findByUserIp(userIp);
        verify(loginAttemptRepository).save(loginAttempt);
    }

    @Test
    public void testLoginFailedClearingLoginAttempt() {
        //given
        String userIp = "userIp";
        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
        loginAttempt.setUserIp(userIp);
        loginAttempt.setLastFailedAttempt(LocalDateTime.now().minusMinutes(2 * BLOCK_TIME));
        when(loginAttemptRepository.findByUserIp(userIp)).thenReturn(Optional.of(loginAttempt));
        when(loginAttemptRepository.save(loginAttempt)).thenReturn(loginAttempt);

        //when
        loginAttemptService.loginFailed(userIp);

        //then
        verify(loginAttemptRepository, times(2)).findByUserIp(userIp);
        verify(loginAttemptRepository, times(2)).save(loginAttempt);
    }

    @Test
    public void testIsBlockedTrue() {
        //given
        String userIp = "userIp";
        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
        loginAttempt.setUserIp(userIp);
        loginAttempt.setFailedCount(MAX_FAIL_ATTEMPT);
        loginAttempt.setLastFailedAttempt(LocalDateTime.now().plusMinutes(BLOCK_TIME * 2));
        when(loginAttemptRepository.findByUserIp(userIp)).thenReturn(Optional.of(loginAttempt));

        //when
        boolean result = loginAttemptService.isBlocked(userIp);

        //then
        assertTrue(result);
        verify(loginAttemptRepository).findByUserIp(userIp);
    }

    @Test
    public void testIsBlockedFalse() {
        //given
        String userIp = "userIp";
        when(loginAttemptRepository.findByUserIp(userIp)).thenReturn(Optional.empty());

        //when
        boolean result = loginAttemptService.isBlocked(userIp);

        //then
        assertFalse(result);
        verify(loginAttemptRepository).findByUserIp(userIp);
    }

    @Test
    public void testClearFailedLoginSuccess() {
        //given
        String userIp = "userIp";
        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
        loginAttempt.setUserIp(userIp);
        when(loginAttemptRepository.findByUserIp(userIp)).thenReturn(Optional.of(loginAttempt));
        when(loginAttemptRepository.save(loginAttempt)).thenReturn(loginAttempt);

        //when
        loginAttemptService.clearFailedLogin(userIp);

        //then
        verify(loginAttemptRepository).findByUserIp(userIp);
        verify(loginAttemptRepository).save(loginAttempt);
    }

    @Test
    public void testClearFailedLoginFailure() {
        //given
        String userIp = "userIp";
        when(loginAttemptRepository.findByUserIp(userIp)).thenThrow(GymEntityNotFoundException.class);

        //then
        assertThrows(GymEntityNotFoundException.class, () -> loginAttemptService.clearFailedLogin(userIp),
            "FailedAttemptEntity not found.");
    }
}
