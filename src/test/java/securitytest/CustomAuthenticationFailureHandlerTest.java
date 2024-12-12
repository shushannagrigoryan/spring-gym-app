package securitytest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;
import org.example.dto.responsedto.ResponseDto;
import org.example.security.CustomAuthenticationFailureHandler;
import org.example.services.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationFailureHandlerTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private AuthenticationException failed;
    @Mock
    private LoginAttemptService loginAttemptService;
    @InjectMocks
    private CustomAuthenticationFailureHandler failureHandler;

    @Test
    void testUnsuccessfulAuthentication() throws Exception {
        //given
        String ipAddress = "ipAddress";
        when(response.getWriter()).thenReturn(printWriter);
        doNothing().when(loginAttemptService).loginFailed(ipAddress);
        when(request.getRemoteAddr()).thenReturn(ipAddress);

        //when
        failureHandler.onAuthenticationFailure(request, response, failed);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
    }

    @Test
    void testUnsuccessfulAuthenticationDisabledException() throws Exception {
        //given
        String ipAddress = "ipAddress";
        doNothing().when(loginAttemptService).loginFailed(ipAddress);
        when(response.getWriter()).thenReturn(printWriter);
        DisabledException disabledException = new DisabledException("Too many failed attempts");
        when(request.getRemoteAddr()).thenReturn(ipAddress);

        // when
        failureHandler.onAuthenticationFailure(request, response, disabledException);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
    }


}
