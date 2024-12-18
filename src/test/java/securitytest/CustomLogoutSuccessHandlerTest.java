package securitytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;
import org.example.dto.responsedto.ResponseDto;
import org.example.security.CustomLogoutSuccessHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class CustomLogoutSuccessHandlerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;
    @Mock
    private PrintWriter writer;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Test
    void testOnLogoutSuccess() throws Exception {
        //given
        when(response.getWriter()).thenReturn(writer);

        //when
        logoutSuccessHandler.onLogoutSuccess(request, response, authentication);

        //then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<ResponseDto<String>> responseCaptor = ArgumentCaptor.forClass(ResponseDto.class);
        verify(objectMapper).writeValue(any(Writer.class), responseCaptor.capture());

        ResponseDto<String> responseDto = responseCaptor.getValue();
        assertEquals("Successfully logged out.", responseDto.getMessage());
    }
}
