package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeCreateRequestDto;
import org.example.dto.requestdto.TraineeUpdateRequestDto;
import org.example.dto.requestdto.TraineeUpdateTrainersRequestDto;
import org.example.dto.requestdto.UserChangeActiveStatusRequestDto;
import org.example.dto.responsedto.TraineeProfileResponseDto;
import org.example.dto.responsedto.TraineeResponseDto;
import org.example.dto.responsedto.TraineeUpdateResponseDto;
import org.example.dto.responsedto.TrainerProfileDto;
import org.example.entity.TraineeEntity;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TraineeProfileMapper;
import org.example.mapper.TrainerMapper;
import org.example.services.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainees")
@Slf4j
@Tag(name = "TraineeController")
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TraineeProfileMapper traineeProfileMapper;

    /**
     * Setting dependencies.
     */
    public TraineeController(TraineeService traineeService,
                             TraineeMapper traineeMapper,
                             TraineeProfileMapper traineeProfileMapper,
                             TrainerMapper trainerMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.traineeProfileMapper = traineeProfileMapper;
        this.trainerMapper = trainerMapper;
    }

    /**
     * POST request to register a new trainee.
     *
     * @param traineeCreateDto request contains:
     *                         firstName(required)
     *                         lastName(required)
     *                         dateOfBirth(optional)
     *                         address(optional)
     * @return generated username and password
     */
    @PostMapping()
    @Operation(description = "Registering a new trainee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully registered a new trainee",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TraineeResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    }
    )
    public ResponseEntity<TraineeResponseDto> registerTrainee(
            @Valid @RequestBody TraineeCreateRequestDto traineeCreateDto) {
        log.debug("Request to register a new trainee: {}", traineeCreateDto);
        TraineeEntity trainee = traineeMapper.dtoToEntity(traineeCreateDto);
        TraineeEntity registeredTrainee = traineeService.registerTrainee(trainee);
        return new ResponseEntity<>(traineeMapper.entityToResponseDto(registeredTrainee), HttpStatus.CREATED);
    }

    /**
     * GET request to get trainee profile.
     *
     * @param username of the trainee
     * @return {@code TraineeProfileResponseDto}
     */
    @GetMapping("/{username}")
    @Operation(description = "Getting trainee profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully received trainee profile",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TraineeProfileResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    }
    )
    public ResponseEntity<TraineeProfileResponseDto> getTraineeProfile(
            @PathVariable("username") String username) {
        log.debug("Request to get trainee profile with username: {}", username);
        TraineeEntity trainee = traineeService.getTraineeProfile(username);
        return new ResponseEntity<>(traineeProfileMapper.entityToProfileDto(trainee), HttpStatus.OK);
    }

    /**
     * PUT request to update trainee.
     *
     * @param trainee to update: {@code TraineeUpdateRequestDto}:
     *                username(required)
     *                firstName(required)
     *                lastName(required)
     *                dateOfBirth(optional)
     *                address(optional)
     *                isActive(required)
     * @return {@code TraineeUpdateResponseDto}
     */
    @PutMapping("/update-trainee")
    public ResponseEntity<TraineeUpdateResponseDto> updateTrainee(
            @Valid @RequestBody TraineeUpdateRequestDto trainee) {
        log.debug("Request to update trainee with username: {}", trainee.getUsername());
        TraineeEntity traineeEntity = traineeMapper.updateDtoToEntity(trainee);
        TraineeEntity updatedTrainee = traineeService.updateTrainee(traineeEntity);
        TraineeUpdateResponseDto traineeResponse = traineeProfileMapper
                .entityToUpdatedDto(updatedTrainee);
        return new ResponseEntity<>(traineeResponse, HttpStatus.OK);
    }

    /**
     * PATCH request to activate/deactivate a trainee.
     *
     * @param activeStatusRequestDto {@code UserChangeActiveStatusRequestDto}: username(required)
     *                               isActive(required)
     */
    @PatchMapping("active-status")
    public ResponseEntity<String> changeActiveStatus(@Valid @RequestBody UserChangeActiveStatusRequestDto
                                                             activeStatusRequestDto) {
        log.debug("Request to change the active status of trainee with username: {} to {}",
                activeStatusRequestDto.getUsername(), activeStatusRequestDto.getIsActive());
        String response = traineeService.changeActiveStatus(activeStatusRequestDto.getUsername(),
                activeStatusRequestDto.getIsActive());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * DELETE request to delete a trainee.
     *
     * @param username of the trainee
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable(value = "username") String username) {
        log.debug("Request to delete trainee with username: {}", username);
        traineeService.deleteTraineeByUsername(username);
        return new ResponseEntity<>("Successfully deleted trainee", HttpStatus.OK);
    }

    /**
     * PUT request to updates trainee's trainer list.
     *
     * @param updateTrainersDto {@code TraineeUpdateTrainersRequestDto}:username of the trainee
     *                          list of trainers
     * @return {@code Set<TrainerProfileDto>} updated trainers set
     */
    @PutMapping("/update-trainer-list")
    public ResponseEntity<List<TrainerProfileDto>> updateTraineesTrainerList(
            @Valid @RequestBody TraineeUpdateTrainersRequestDto updateTrainersDto) {
        log.debug("Request to update trainee's: {} trainer list with: {}.",
                updateTrainersDto.getUsername(), updateTrainersDto.getTrainers());

        return new ResponseEntity<>(traineeService.updateTraineesTrainerList(updateTrainersDto.getUsername(),
                        updateTrainersDto.getTrainers())
                .stream().map(trainerMapper::entityToProfileDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }


}
