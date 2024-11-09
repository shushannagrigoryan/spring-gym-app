package usernamegeneration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String expectedUsername = "John.Smith";


        //when(userService.getUserByUsername(expectedUsername)).thenReturn(Optional.empty());
        when(userService.getUsernameMaxIndex(firstName, lastName)).thenReturn(null);

        String generatedUsername = usernameGenerator.generateUsername(user);

        assertEquals(expectedUsername, generatedUsername);
        verify(userService).getUsernameMaxIndex(firstName, lastName);
    }

    @Test
    void testGenerateUsername_UsernameTaken() {
        String firstName = "John";
        String lastName = "Smith";
        String usernameWithSuffix = "John.Smith1";
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);

        when(userService.getUsernameMaxIndex(firstName, lastName)).thenReturn(0L);

        String generatedUsername = usernameGenerator.generateUsername(user);

        assertEquals(usernameWithSuffix, generatedUsername);
        verify(userService).getUsernameMaxIndex(firstName, lastName);
    }
}
