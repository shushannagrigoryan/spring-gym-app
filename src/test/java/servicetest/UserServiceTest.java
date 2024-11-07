package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.metrics.UserMetrics;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMetrics userMetrics;
    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsernames() {
        //given
        List<String> usernames = List.of("A.B", "A.B1", "B.C");
        when(userRepository.findAllUsernames()).thenReturn(usernames);

        //when
        List<String> result = userService.getAllUsernames();

        //then
        assertNotNull(result);
        assertEquals(usernames, result);
        verify(userRepository).findAllUsernames();
    }

    @Test
    public void testGetUserByUsernameSuccess() {
        //given
        String username = "A.B";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //when
        Optional<UserEntity> result = userService.getUserByUsername(username);

        //then
        verify(userRepository).findByUsername(username);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
    }

    @Test
    public void testGetUserByUsernameUserNotFound() {
        //given
        String username = "A.B";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //when
        Optional<UserEntity> result = userService.getUserByUsername(username);

        //then
        verify(userRepository).findByUsername(username);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRegisterUser() {
        //given
        UserEntity user = new UserEntity();
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(userMetrics).incrementCounter();

        //when
        userService.save(user);

        //then
        verify(userRepository).save(user);
    }

    @Test
    public void testLoginSuccess() {
        //given
        String username = "A";
        String password = "B";
        when(userRepository.findByUsernameAndPassword(username, password))
            .thenReturn(Optional.of(new UserEntity()));

        //when
        userService.login(username, password);

        //then
        verify(userRepository).findByUsernameAndPassword(username, password);
    }

    @Test
    public void testLoginFailure() {
        //given
        String username = "A";
        String password = "B";
        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        //then
        assertThrows(GymEntityNotFoundException.class,
            () -> userService.login(username, password),
            "Invalid username and password.");
    }

    @Test
    public void testChangeUserPassword() {
        //given
        String username = "A.B";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(oldPassword);
        when(userRepository.findByUsernameAndPassword(username, oldPassword))
            .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        //when
        userService.changeUserPassword(username, oldPassword, newPassword);

        //then
        verify(userRepository).findByUsernameAndPassword(username, oldPassword);
        verify(userRepository).save(user);

    }

    @Test
    public void testDeleteUser() {
        //given
        UserEntity user = new UserEntity();
        doNothing().when(userRepository).deleteById(user.getId());

        //when
        userService.deleteUser(user);

        //then
        verify(userRepository).deleteById(user.getId());

    }
}
