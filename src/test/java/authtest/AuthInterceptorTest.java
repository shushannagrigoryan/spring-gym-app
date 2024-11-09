package authtest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import org.example.auth.AuthInterceptor;
import org.example.auth.UserAuth;
import org.example.exceptions.GymAuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    @Mock
    private UserAuth userAuth;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectWriter objectWriter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private AuthInterceptor authInterceptor;


    @Test
    void testWithoutAuth() throws Exception {
        //given
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/gym/trainees");

        //then
        assertTrue(authInterceptor.preHandle(request, response, new Object()));
    }

    @Test
    void testValidCredentials() throws Exception {
        //given
        String username = "username";
        String password = "password";
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("username")).thenReturn(username);
        when(request.getHeader("password")).thenReturn(password);
        doNothing().when(userAuth).userAuth(username, password);

        //then
        assertTrue(authInterceptor.preHandle(request, response, new Object()));
    }

    @Test
    void shouldRejectRequestWithInvalidCredentials() throws Exception {
        //given
        String username = "username";
        String password = "password";
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("username")).thenReturn(username);
        when(request.getHeader("password")).thenReturn(password);
        when(objectMapper.writer()).thenReturn(objectWriter);
        when(objectWriter.withDefaultPrettyPrinter()).thenReturn(objectWriter);

        doThrow(new GymAuthenticationException("Invalid credentials")).when(userAuth)
            .userAuth("username", "password");
        when(response.getWriter()).thenReturn(printWriter);

        //then
        assertFalse(authInterceptor.preHandle(request, response, new Object()));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


}
