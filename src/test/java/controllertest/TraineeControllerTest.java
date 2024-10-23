package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.example.controller.TraineeController;
import org.example.dto.requestdto.TraineeCreateRequestDto;
import org.example.dto.requestdto.TraineeUpdateRequestDto;
import org.example.dto.requestdto.TraineeUpdateTrainersRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TraineeProfileResponseDto;
import org.example.dto.responsedto.TraineeResponseDto;
import org.example.dto.responsedto.TraineeUpdateResponseDto;
import org.example.dto.responsedto.TrainerProfileDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TraineeProfileMapper;
import org.example.mapper.TrainerMapper;
import org.example.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TraineeProfileMapper traineeProfileMapper;
    @InjectMocks
    private TraineeController traineeController;

    @Test
    public void testRegisterTraineeSuccess() {
        //given
        TraineeCreateRequestDto traineeCreateRequestDto = new TraineeCreateRequestDto();
        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeMapper.dtoToEntity(traineeCreateRequestDto)).thenReturn(traineeEntity);
        TraineeEntity registered = new TraineeEntity();
        when(traineeService.registerTrainee(traineeEntity)).thenReturn(registered);
        TraineeResponseDto response = new TraineeResponseDto();
        when(traineeMapper.entityToResponseDto(registered)).thenReturn(response);

        //when
        ResponseEntity<ResponseDto<TraineeResponseDto>> result =  traineeController.registerTrainee(traineeCreateRequestDto);

        //then
        verify(traineeService).registerTrainee(traineeEntity);
        verify(traineeMapper).entityToResponseDto(registered);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, Objects.requireNonNull(result.getBody()).getPayload());
    }

    @Test
    public void testChangeActiveStatusSuccess() {
        //given
        String username = "A.B";
        boolean isActive = true;
        UserChangeActiveStatusRequestDto requestDto =
                new UserChangeActiveStatusRequestDto(username, isActive);
        when(traineeService.changeActiveStatus(username, isActive)).thenReturn("Success.");

        //when
        ResponseEntity<String> response = traineeController.changeActiveStatus(requestDto);

        //then
        verify(traineeService).changeActiveStatus(username, isActive);
        assertEquals("Success.", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testDeleteTraineeByUsernameSuccess() {
        //given
        String username = "A.A";
        doNothing().when(traineeService).deleteTraineeByUsername(username);

        //when
        ResponseEntity<String> result = traineeController.deleteTrainee(username);

        //then
        verify(traineeService).deleteTraineeByUsername(username);
        assertEquals("Successfully deleted trainee", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    public void testUpdateTraineesTrainerList() {
        //given
        String username = "A.A";
        TraineeUpdateTrainersRequestDto requestDto = new TraineeUpdateTrainersRequestDto(
                username, new ArrayList<>());
        TrainerEntity trainer = new TrainerEntity();
        List<String> list = new ArrayList<>();
        when(traineeService.updateTraineesTrainerList(username, list)).thenReturn(Set.of(trainer));
        TrainerProfileDto trainerProfileDto = new TrainerProfileDto();
        when(trainerMapper.entityToProfileDto(trainer)).thenReturn(trainerProfileDto);


        //when
        ResponseEntity<List<TrainerProfileDto>> result =
                traineeController.updateTraineesTrainerList(username, requestDto);

        //then
        verify(traineeService).updateTraineesTrainerList(username, list);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(trainerProfileDto), result.getBody());
    }

    @Test
    public void getTraineeProfile() {
        //given
        String username = "A.A";
        TraineeProfileResponseDto response = new TraineeProfileResponseDto();
        TraineeEntity trainee = new TraineeEntity();
        when(traineeService.getTraineeProfile(username)).thenReturn(trainee);
        when(traineeProfileMapper.entityToProfileDto(trainee)).thenReturn(response);

        //when
        ResponseEntity<ResponseDto<TraineeProfileResponseDto>> result =
                traineeController.getTraineeProfile(username);

        //then
        verify(traineeService).getTraineeProfile(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, Objects.requireNonNull(result.getBody()).getPayload());
    }

    @Test
    public void testUpdateTrainee() {
        //given
        String username = "A.A";
        String firstName = "B";
        String lastName = "C";
        LocalDate dateOfBirth = LocalDate.now();
        String address = "newAddress";
        Boolean isActive = false;
        TraineeUpdateRequestDto requestDto =
                new TraineeUpdateRequestDto(username, firstName, lastName, dateOfBirth, address, isActive);
        TraineeEntity trainee = new TraineeEntity();
        when(traineeMapper.updateDtoToEntity(requestDto)).thenReturn(trainee);
        TraineeEntity updatedTrainee = new TraineeEntity();
        when(traineeService.updateTrainee(trainee)).thenReturn(updatedTrainee);
        TraineeUpdateResponseDto traineeResponse = new TraineeUpdateResponseDto();
        when(traineeProfileMapper
                .entityToUpdatedDto(updatedTrainee)).thenReturn(traineeResponse);

        //when
        ResponseEntity<TraineeUpdateResponseDto> result = traineeController.updateTrainee(requestDto);

        //then
        verify(traineeMapper).updateDtoToEntity(requestDto);
        verify(traineeService).updateTrainee(trainee);
        verify(traineeProfileMapper).entityToUpdatedDto(updatedTrainee);
        assertEquals(traineeResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }



}
