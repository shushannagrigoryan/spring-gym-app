//package servicetest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import org.example.entity.UserEntity;
//import org.example.repository.UserRepository;
//import org.example.services.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    public void testGetAllUsernames() {
//        //given
//        List<String> usernames = List.of("A.B", "A.B1", "B.C");
//        when(userRepository.getAllUsernames()).thenReturn(usernames);
//
//        //when
//        List<String> result = userService.getAllUsernames();
//
//        //then
//        assertNotNull(result);
//        assertEquals(usernames, result);
//        verify(userRepository).getAllUsernames();
//    }
//
//    @Test
//    public void testGetUserByUsernameSuccess() {
//        //given
//        String username = "A.B";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        when(userRepository.getUserByUsername(username)).thenReturn(user);
//
//        //when
//        UserEntity result = userService.getUserByUsername(username);
//
//        //then
//        verify(userRepository).getUserByUsername(username);
//        assertNotNull(result);
//        assertEquals(user.getUsername(), result.getUsername());
//    }
//
//    @Test
//    public void testGetUserByUsernameUserNotFound() {
//        //given
//        String username = "A.B";
//        when(userRepository.getUserByUsername(username)).thenReturn(null);
//
//        //when
//        UserEntity result = userService.getUserByUsername(username);
//
//        //then
//        verify(userRepository).getUserByUsername(username);
//        assertNull(result);
//    }
//}
