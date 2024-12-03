package securitytest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;
import org.example.dto.responsedto.ResponseDto;
import org.example.exceptions.GymAuthenticationException;
import org.example.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private AuthenticationException failed;
    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void attemptAuthenticationSuccess() {
        //given
        when(request.getHeader("username")).thenReturn("user");
        when(request.getHeader("password")).thenReturn("pass");

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken("user", "pass");
        when(authenticationManager.authenticate(authRequest)).thenReturn(authentication);

        //when
        jwtAuthFilter.attemptAuthentication(request, response);

        //then
        verify(authenticationManager).authenticate(authRequest);
    }

    @Test
    void attemptAuthenticationFail() {
        //given
        when(request.getHeader("username")).thenReturn(null);
        when(request.getHeader("password")).thenReturn(null);

        //then
        assertThrows(GymAuthenticationException.class, () -> jwtAuthFilter.attemptAuthentication(request, response),
            "Bad credentials.");

    }

    @Test
    void testUnsuccessfulAuthentication() throws Exception {
        //given
        when(response.getWriter()).thenReturn(printWriter);

        //when
        jwtAuthFilter.unsuccessfulAuthentication(request, response, failed);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
    }

    @Test
    void testUnsuccessfulAuthenticationDisabledException() throws Exception {
        //given
        when(response.getWriter()).thenReturn(printWriter);
        DisabledException disabledException = new DisabledException("Too many failed attempts");

        // when
        jwtAuthFilter.unsuccessfulAuthentication(request, response, disabledException);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
    }
}
