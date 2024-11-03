package controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.example.controller.TrainerController;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.requestdto.TrainerUpdateRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TrainerProfileDto;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.entity.TrainerEntity;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainerProfileMapper;
import org.example.metrics.TrainerRequestMetrics;
import org.example.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TrainerProfileMapper trainerProfileMapper;
    @Mock
    private TrainerRequestMetrics trainerRequestMetrics;
    @InjectMocks
    private TrainerController trainerController;


    @Test
    public void testRegisterTrainerSuccess() {
        //given
        TrainerCreateRequestDto requestDto = new TrainerCreateRequestDto();
        TrainerEntity trainerEntity = new TrainerEntity();
        doNothing().when(trainerRequestMetrics).incrementCounter();
        when(trainerMapper.dtoToEntity(requestDto)).thenReturn(trainerEntity);
        TrainerEntity registered = new TrainerEntity();
        when(trainerService.registerTrainer(trainerEntity)).thenReturn(registered);
        TrainerResponseDto response = new TrainerResponseDto();
        when(trainerMapper.entityToResponseDto(registered)).thenReturn(response);

        //when
        ResponseEntity<ResponseDto<TrainerResponseDto>> result = trainerController.registerTrainer(requestDto);

        //then
        verify(trainerService).registerTrainer(trainerEntity);
        verify(trainerMapper).entityToResponseDto(registered);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, Objects.requireNonNull(result.getBody()).getPayload());
    }



    @Test
    public void testGetActiveTrainersNotAssignedToTrainee() {
        // Given
        String traineeUsername = "A.A";
        TrainerEntity trainer = new TrainerEntity();
        doNothing().when(trainerRequestMetrics).incrementCounter();
        when(trainerService.notAssignedOnTraineeActiveTrainers(traineeUsername)).thenReturn(List.of(trainer));
        TrainerProfileDto trainerProfileDto = new TrainerProfileDto();
        when(trainerMapper.entityToProfileDto(trainer)).thenReturn(trainerProfileDto);

        // When
        ResponseEntity<ResponseDto<Set<TrainerProfileDto>>> result =
                trainerController.notAssignedOnTraineeActiveTrainers(traineeUsername);

        // Then
        assertEquals(Set.of(trainerProfileDto), Objects.requireNonNull(result.getBody()).getPayload());
        verify(trainerService).notAssignedOnTraineeActiveTrainers(traineeUsername);
        verify(trainerMapper).entityToProfileDto(trainer);
    }

    @Test
    public void testChangeActiveStatusSuccess() {
        //given
        String username = "A.B";
        boolean isActive = true;
        UserChangeActiveStatusRequestDto requestDto =
                new UserChangeActiveStatusRequestDto(username, isActive);
        doNothing().when(trainerRequestMetrics).incrementCounter();
        when(trainerService.changeActiveStatus(username, isActive)).thenReturn("Success.");

        //when
        ResponseEntity<ResponseDto<Object>> response = trainerController.changeActiveStatus(requestDto);

        //then
        verify(trainerService).changeActiveStatus(username, isActive);
        assertEquals("Success.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getTrainerProfile() {
        //given
        String username = "A.A";
        TrainerProfileResponseDto response = new TrainerProfileResponseDto();
        TrainerEntity trainer = new TrainerEntity();
        doNothing().when(trainerRequestMetrics).incrementCounter();
        when(trainerService.getTrainerProfile(username)).thenReturn(trainer);
        when(trainerProfileMapper.entityToProfileDto(trainer)).thenReturn(response);

        //when
        ResponseEntity<ResponseDto<TrainerProfileResponseDto>> result =
                trainerController.getTrainerProfile(username);

        //then
        verify(trainerService).getTrainerProfile(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, Objects.requireNonNull(result.getBody()).getPayload());
    }

    @Test
    public void testUpdateTrainer() {
        //given
        String username = "A.A";
        String firstName = "B";
        String lastName = "C";
        Boolean isActive = false;
        Long specialization = 1L;
        TrainerUpdateRequestDto requestDto =
                new TrainerUpdateRequestDto(username, firstName, lastName, specialization, isActive);
        TrainerEntity trainer = new TrainerEntity();
        doNothing().when(trainerRequestMetrics).incrementCounter();
        when(trainerMapper.updateDtoToEntity(requestDto)).thenReturn(trainer);
        TrainerEntity updatedTrainer = new TrainerEntity();
        when(trainerService.updateTrainer(trainer)).thenReturn(updatedTrainer);
        TrainerUpdateResponseDto trainerResponse = new TrainerUpdateResponseDto();
        when(trainerProfileMapper
                .entityToUpdatedDto(updatedTrainer)).thenReturn(trainerResponse);

        //when
        ResponseEntity<ResponseDto<TrainerUpdateResponseDto>> result = trainerController.updateTrainer(requestDto);

        //then
        verify(trainerMapper).updateDtoToEntity(requestDto);
        verify(trainerService).updateTrainer(trainer);
        verify(trainerProfileMapper).entityToUpdatedDto(updatedTrainer);
        assertEquals(trainerResponse, Objects.requireNonNull(result.getBody()).getPayload());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

