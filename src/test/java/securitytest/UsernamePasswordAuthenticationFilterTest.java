//package securitytest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.util.List;
//import java.util.Optional;
//import org.example.dto.responsedto.ResponseDto;
//import org.example.entity.TokenEntity;
//import org.example.entity.UserEntity;
//import org.example.exceptions.GymAuthenticationException;
//import org.example.security.UsernamePasswordAuthenticationFilter;
//import org.example.services.JwtService;
//import org.example.services.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//
//@ExtendWith(MockitoExtension.class)
//public class UsernamePasswordAuthenticationFilterTest {
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    @Mock
//    private ObjectMapper objectMapper;
//    @Mock
//    private Authentication authentication;
//    @Mock
//    private PrintWriter printWriter;
//    @Mock
//    private AuthenticationException failed;
//    @Mock
//    private UserService userService;
//    @Mock
//    private FilterChain chain;
//    @Mock
//    private JwtService jwtService;
//    @InjectMocks
//    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
//
//    @Test
//    void attemptAuthenticationSuccess() {
//        //given
//        when(request.getHeader("username")).thenReturn("user");
//        when(request.getHeader("password")).thenReturn("pass");
//
//        UsernamePasswordAuthenticationToken authRequest =
//            new UsernamePasswordAuthenticationToken("user", "pass");
//        when(authenticationManager.authenticate(authRequest)).thenReturn(authentication);
//
//        //when
//        usernamePasswordAuthenticationFilter.attemptAuthentication(request, response);
//
//        //then
//        verify(authenticationManager).authenticate(authRequest);
//    }
//
//    @Test
//    void attemptAuthenticationFail() {
//        //given
//        when(request.getHeader("username")).thenReturn(null);
//        when(request.getHeader("password")).thenReturn(null);
//
//        //then
//        assertThrows(GymAuthenticationException.class,
//            () -> usernamePasswordAuthenticationFilter.attemptAuthentication(request, response),
//            "Bad credentials.");
//
//    }
//
//    @Test
//    void testUnsuccessfulAuthentication() throws Exception {
//        //given
//        when(response.getWriter()).thenReturn(printWriter);
//
//        //when
//        usernamePasswordAuthenticationFilter.unsuccessfulAuthentication(request, response, failed);
//
//        //then
//        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        verify(response).setContentType("application/json");
//        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
//    }
//
//    @Test
//    void testUnsuccessfulAuthenticationDisabledException() throws Exception {
//        //given
//        when(response.getWriter()).thenReturn(printWriter);
//        DisabledException disabledException = new DisabledException("Too many failed attempts");
//
//        // when
//        usernamePasswordAuthenticationFilter.unsuccessfulAuthentication(request, response, disabledException);
//
//        //then
//        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        verify(response).setContentType("application/json");
//        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));
//    }
//
//    @Test
//    public void testSuccessfulAuthenticationEntityNotFound() {
//        //given
//        String username = "user";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        when(authentication.getName()).thenReturn(username);
//        when(userService.getUserByUsername(username)).thenThrow(EntityNotFoundException.class);
//
//        //then
//        assertThrows(EntityNotFoundException.class,
//            () -> usernamePasswordAuthenticationFilter
//                .successfulAuthentication(request, response, chain, authentication),
//            "Entity not found");
//
//    }
//
//    @Test
//    public void testSuccessfulAuthenticationAlreadyLoggedIn() throws IOException {
//        //given
//        String username = "user";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        when(authentication.getName()).thenReturn(username);
//        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
//
//        StringWriter stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//        TokenEntity token = new TokenEntity();
//        when(jwtService.findNonRevokedTokensByUser(username)).thenReturn(List.of(token));
//        when(jwtService.isTokenExpired(token.getToken())).thenReturn(false);
//
//        //when
//        usernamePasswordAuthenticationFilter.successfulAuthentication(request, response, chain, authentication);
//
//        //then
//        verify(authentication).getName();
//        verify(userService).getUserByUsername(username);
//        verify(jwtService).findNonRevokedTokensByUser(username);
//        verify(jwtService).isTokenExpired(token.getToken());
//
//        @SuppressWarnings("unchecked")
//        ArgumentCaptor<ResponseDto<String>> captor = ArgumentCaptor.forClass(ResponseDto.class);
//        verify(objectMapper).writeValue(any(Writer.class), captor.capture());
//        ResponseDto<String> responseDto = captor.getValue();
//        assertEquals("User is already logged in. Please use your valid token or log out before logging in again.",
//            responseDto.getMessage());
//    }
//
//    @Test
//    public void testSuccessfulAuthentication() throws IOException {
//        //given
//        String username = "user";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        when(authentication.getName()).thenReturn(username);
//        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
//
//        StringWriter stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//        TokenEntity token = new TokenEntity();
//        when(jwtService.findNonRevokedTokensByUser(username)).thenReturn(List.of(token));
//        when(jwtService.isTokenExpired(token.getToken())).thenReturn(true);
//        String tokenVal = "token";
//        when(jwtService.generateToken(authentication)).thenReturn(tokenVal);
//        doNothing().when(jwtService).saveGeneratedToken(any(TokenEntity.class));
//
//        //when
//        usernamePasswordAuthenticationFilter.successfulAuthentication(request, response, chain, authentication);
//
//        //then
//        verify(authentication).getName();
//        verify(userService).getUserByUsername(username);
//        verify(jwtService).findNonRevokedTokensByUser(username);
//        verify(jwtService).isTokenExpired(token.getToken());
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(response).setContentType("application/json");
//
//        @SuppressWarnings("unchecked")
//        ArgumentCaptor<ResponseDto<String>> captor = ArgumentCaptor.forClass(ResponseDto.class);
//        verify(objectMapper).writeValue(any(Writer.class), captor.capture());
//        ResponseDto<String> responseDto = captor.getValue();
//        assertEquals("Successfully logged in.", responseDto.getMessage());
//
//    }
//}
