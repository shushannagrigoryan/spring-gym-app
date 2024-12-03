package securitytest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import org.example.dto.responsedto.ResponseDto;
import org.example.security.CustomAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtValidationException;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationEntryPointTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    void testCommenceJwtValidationException() throws Exception {
        //given
        when(response.getWriter()).thenReturn(printWriter);
        OAuth2Error error = new OAuth2Error("1");
        when(authException.getCause()).thenReturn(new JwtValidationException("Invalid JWT",
            List.of(error)));

        //when
        customAuthenticationEntryPoint.commence(request, response, authException);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
    }

    @Test
    void testCommence_WithGeneralAuthenticationException() throws Exception {
        //given
        when(response.getWriter()).thenReturn(printWriter);
        when(authException.getMessage()).thenReturn("Authentication failed");

        //when
        customAuthenticationEntryPoint.commence(request, response, authException);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
    }
}
