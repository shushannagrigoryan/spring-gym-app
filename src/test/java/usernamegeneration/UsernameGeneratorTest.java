package usernamegeneration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.example.services.UserService;
import org.example.username.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UsernameGeneratorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsernameGenerator usernameGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateUsername_UsernameNotTaken() {
        String firstName = "John";
        String lastName = "Smith";
        String expectedUsername = "John.Smith";

        when(userService.getUserByUsername(expectedUsername)).thenReturn(Optional.empty());

        String generatedUsername = usernameGenerator.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, generatedUsername);
        verify(userService).getUserByUsername(expectedUsername);
    }

    @Test
    void testGenerateUsername_UsernameTaken() {
        String firstName = "John";
        String lastName = "Smith";
        String username = "John.Smith";
        String usernameWithSuffix = "John.Smith1";

        when(userService.getUserByUsername(username)).thenReturn(Optional.of(new UserEntity()));

        when(userService.getAllUsernamesWithPrefix(username)).thenReturn(List.of("John.Smith"));

        String generatedUsername = usernameGenerator.generateUsername(firstName, lastName);

        assertEquals(usernameWithSuffix, generatedUsername);
        verify(userService).getUserByUsername(username);
    }

    @Test
    void testGetSuffix_1() {
        String username = "John.Smith";

        when(userService.getAllUsernamesWithPrefix(username)).thenReturn(List.of());

        Long suffix = usernameGenerator.getSuffix(username);

        assertEquals(1L, suffix);
        verify(userService).getAllUsernamesWithPrefix(username);
    }

    @Test
    void testGetSuffix_ExistingSuffix() {
        String username = "John.Smith";
        List<String> allUsernames = Arrays.asList("John.Smith", "John.Smith1", "John.Smith2");

        when(userService.getAllUsernamesWithPrefix(username)).thenReturn(allUsernames);

        Long suffix = usernameGenerator.getSuffix(username);

        assertEquals(3L, suffix);
        verify(userService).getAllUsernamesWithPrefix(username);
    }
}
