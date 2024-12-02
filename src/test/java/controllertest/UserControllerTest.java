package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import org.example.controller.UserController;
import org.example.dto.requestdto.ChangePasswordRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.metrics.UserRequestMetrics;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private UserRequestMetrics userRequestMetrics;
    @InjectMocks
    private UserController userController;

    @Test
    public void testChangePassword() {
        //given
        String username = "A.B";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        ChangePasswordRequestDto requestDto =
            new ChangePasswordRequestDto(oldPassword, newPassword);
        doNothing().when(userRequestMetrics).incrementCounter();

        //when
        ResponseEntity<ResponseDto<Object>> result = userController.changePassword(username, requestDto);

        //then
        verify(userService).changeUserPassword(username, oldPassword, newPassword);
        assertEquals("Successfully changed user password.",
            Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


}
