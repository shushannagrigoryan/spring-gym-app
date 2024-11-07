package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TraineeProfileMapper;
import org.example.mapper.TrainerMapper;
import org.example.metrics.TraineeRequestMetrics;
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
    private final TraineeRequestMetrics traineeRequestMetrics;

    /**
     * Setting dependencies.
     */
    public TraineeController(TraineeService traineeService,
                             TraineeMapper traineeMapper,
                             TraineeProfileMapper traineeProfileMapper,
                             TrainerMapper trainerMapper,
                             TraineeRequestMetrics traineeRequestMetrics) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.traineeProfileMapper = traineeProfileMapper;
        this.trainerMapper = trainerMapper;
        this.traineeRequestMetrics = traineeRequestMetrics;
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
    @PostMapping
    @Operation(description = "Registering a new trainee")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "201",
                description = "Successfully registered a new trainee.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<TraineeResponseDto>> registerTrainee(
        @Valid @RequestBody TraineeCreateRequestDto traineeCreateDto) {
        traineeRequestMetrics.incrementCounter();
        log.debug("Request to register a new trainee: {}", traineeCreateDto);
        TraineeEntity trainee = traineeMapper.dtoToEntity(traineeCreateDto);
        TraineeEntity registeredTrainee = traineeService.registerTrainee(trainee);
        return new ResponseEntity<>(new ResponseDto<>(traineeMapper.entityToResponseDto(registeredTrainee),
            "Successfully registered a new trainee."), HttpStatus.CREATED);
    }

    /**
     * GET request to get trainee profile.
     *
     * @param username of the trainee
     * @return {@code TraineeProfileResponseDto}
     */
    @GetMapping("/{username}")
    @Operation(description = "Getting trainee profile")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully received trainee profile.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<TraineeProfileResponseDto>> getTraineeProfile(
        @PathVariable("username") String username) {
        traineeRequestMetrics.incrementCounter();
        log.debug("Request to get trainee profile with username: {}", username);
        TraineeEntity trainee = traineeService.getTraineeProfile(username);

        return new ResponseEntity<>(new ResponseDto<>(traineeProfileMapper.entityToProfileDto(trainee),
            "Successfully retrieved trainee profile."), HttpStatus.OK);
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
    @PutMapping()
    @Operation(description = "Updating trainee profile")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully updated trainee profile.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<TraineeUpdateResponseDto>> updateTrainee(
        @Valid @RequestBody TraineeUpdateRequestDto trainee) {
        traineeRequestMetrics.incrementCounter();
        log.debug("Request to update trainee with username: {}", trainee.getUsername());
        TraineeEntity traineeEntity = traineeMapper.updateDtoToEntity(trainee);
        TraineeEntity updatedTrainee = traineeService.updateTrainee(traineeEntity);
        TraineeUpdateResponseDto traineeResponse = traineeProfileMapper
            .entityToUpdatedDto(updatedTrainee);

        return new ResponseEntity<>(new ResponseDto<>(traineeResponse,
            "Successfully updated trainee profile."), HttpStatus.OK);
    }

    /**
     * PATCH request to activate/deactivate a trainee.
     *
     * @param activeStatusRequestDto {@code UserChangeActiveStatusRequestDto}: username(required)
     *                               isActive(required)
     */
    @PatchMapping("activation")
    @Operation(description = "Activating/Deactivating trainee")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully changes trainee active status."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<Object>> changeActiveStatus(@Valid @RequestBody UserChangeActiveStatusRequestDto
                                                                      activeStatusRequestDto) {
        traineeRequestMetrics.incrementCounter();
        log.debug("Request to change the active status of trainee with username: {} to {}",
            activeStatusRequestDto.getUsername(), activeStatusRequestDto.getIsActive());
        String response = traineeService.changeActiveStatus(activeStatusRequestDto.getUsername(),
            activeStatusRequestDto.getIsActive());
        return new ResponseEntity<>(new ResponseDto<>(null, response), HttpStatus.OK);
    }

    /**
     * DELETE request to delete a trainee.
     *
     * @param username of the trainee
     */
    @DeleteMapping("/{username}")
    @Operation(description = "Deleting trainee profile")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully deleted trainee profile."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<Object>> deleteTrainee(@PathVariable(value = "username") String username) {
        traineeRequestMetrics.incrementCounter();
        log.debug("Request to delete trainee with username: {}", username);
        traineeService.deleteTraineeByUsername(username);
        return new ResponseEntity<>(
            new ResponseDto<>(null, "Successfully deleted trainee"), HttpStatus.OK);
    }

    /**
     * PUT request to updates trainee's trainer list.
     *
     * @param updateTrainersDto {@code TraineeUpdateTrainersRequestDto}:username of the trainee
     *                          list of trainers
     * @return {@code Set<TrainerProfileDto>} updated trainers set
     */
    @PutMapping("/{username}/trainers")
    @Operation(description = "Updating trainee trainers list")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully updated trainee's trainers list.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ResponseDto<List<TrainerProfileDto>>> updateTraineesTrainerList(
        @PathVariable(value = "username") String username,
        @Valid @RequestBody TraineeUpdateTrainersRequestDto updateTrainersDto) {
        traineeRequestMetrics.incrementCounter();
        log.debug("Request to update trainee's: {} trainer list with: {}.",
            username, updateTrainersDto.getTrainers());

        List<TrainerProfileDto> payload = traineeService.updateTraineesTrainerList(username,
                updateTrainersDto.getTrainers())
            .stream().map(trainerMapper::entityToProfileDto)
            .toList();
        return new ResponseEntity<>(new ResponseDto<>(payload,
            "Successfully updated trainee trainers list."), HttpStatus.OK);
    }
}
