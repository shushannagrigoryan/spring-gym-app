package securitytest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;
import org.example.dto.responsedto.ResponseDto;
import org.example.security.CustomAccessDeniedHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
public class CustomAccessDeniedHandlerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private CustomAccessDeniedHandler accessDeniedHandler;


    @Test
    void handle_shouldSetForbiddenStatusAndWriteResponse() throws Exception {
        //given
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        when(response.getWriter()).thenReturn(writer);

        //when
        accessDeniedHandler.handle(request, response, exception);

        //then
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(any(Writer.class), any(ResponseDto.class));

    }
}
