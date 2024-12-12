package securitytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TokenEntity;
import org.example.entity.UserEntity;
import org.example.security.CustomAuthenticationSuccessHandler;
import org.example.services.JwtService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationSuccessHandlerTest {
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
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private CustomAuthenticationSuccessHandler successHandler;

    @Test
    public void testSuccessfulAuthenticationEntityNotFound() {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUsername(username)).thenThrow(EntityNotFoundException.class);

        //then
        assertThrows(EntityNotFoundException.class,
            () -> successHandler.onAuthenticationSuccess(request, response, authentication),
            "Entity not found");

    }

    @Test
    public void testSuccessfulAuthenticationAlreadyLoggedIn() throws IOException {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
        String auth = "Bearer token";
        when(request.getHeader("Authorization")).thenReturn(auth);
        StringWriter stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        when(jwtService.isValid(auth.substring(7), username)).thenReturn(true);

        //when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        //then
        verify(authentication).getName();
        verify(userService).getUserByUsername(username);
        verify(jwtService).isValid(auth.substring(7), username);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<ResponseDto<String>> captor = ArgumentCaptor.forClass(ResponseDto.class);
        verify(objectMapper).writeValue(any(Writer.class), captor.capture());
        ResponseDto<String> responseDto = captor.getValue();
        assertEquals("User is already logged in. Please use your valid token or log out before logging in again.",
            responseDto.getMessage());
    }

    @Test
    public void testSuccessfulAuthentication() throws IOException {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));

        StringWriter stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        String tokenVal = "token";
        when(jwtService.generateToken(authentication)).thenReturn(tokenVal);
        doNothing().when(jwtService).saveGeneratedToken(any(TokenEntity.class));

        //when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        //then
        verify(authentication).getName();
        verify(userService).getUserByUsername(username);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<ResponseDto<String>> captor = ArgumentCaptor.forClass(ResponseDto.class);
        verify(objectMapper).writeValue(any(Writer.class), captor.capture());
        ResponseDto<String> responseDto = captor.getValue();
        assertEquals("Successfully logged in.", responseDto.getMessage());

    }

}
