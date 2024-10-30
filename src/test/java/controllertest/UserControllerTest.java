package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import org.example.controller.UserController;
import org.example.dto.requestdto.ChangePasswordRequestDto;
import org.example.dto.responsedto.ResponseDto;
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
    @InjectMocks
    private UserController userController;

    @Test
    public void testLoginSuccess() {
        //given
        String username = "A";
        String password = "B";
        doNothing().when(userService).login(username, password);

        //when
        ResponseEntity<ResponseDto<Object>> result = userController.login(username, password);

        //then
        verify(userService).login(username, password);
        assertEquals("Successfully logged in.", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testChangePassword() {
        //given
        String username = "A.B";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        ChangePasswordRequestDto requestDto =
                new ChangePasswordRequestDto(username, oldPassword, newPassword);

        //when
        ResponseEntity<ResponseDto<Object>> result = userController.changePassword(requestDto);

        //then
        verify(userService).changeUserPassword(username, oldPassword, newPassword);
        assertEquals("Successfully changed user password.",
                Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }




}
