package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainerTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TraineeCriteriaTrainingsResponseDto;
import org.example.dto.responsedto.TrainerCriteriaTrainingsResponseDto;
import org.example.entity.TrainingEntity;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainings")
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    /**
     * Setting dependencies.
     */
    public TrainingController(TrainingService trainingService,
                              TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }


    /**
     * POST request to add new training.
     *
     * @param trainingDto training to create: {@code TrainingCreateDto}
     */
    @PostMapping()
    @Operation(description = "Creating new training.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created new training."),
        @ApiResponse(responseCode = "401", description = "Authentication error",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "405", description = "Method is not allowed.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    }
    )
    public ResponseEntity<ResponseDto<Object>> createTraining(
            @Valid @RequestBody TrainingCreateRequestDto trainingDto) {
        log.debug("Request to create new training: {}", trainingDto);
        trainingService.createTraining(trainingDto);
        return new ResponseEntity<>(new ResponseDto<>(null,
                "Successfully created a new training."), HttpStatus.OK);
    }

    /**
     * GET request to get trainee trainings by given criteria.
     *
     * @param trainingsDto {@code TraineeTrainingsFilterRequestDto} the given criteria
     * @return {@code List<TraineeCriteriaTrainingsResponseDto>}
     */
    @GetMapping("/trainees/{username}")
    @Operation(description = "Getting trainee trainings.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully got trainee trainings.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(responseCode = "401", description = "Authentication error",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "405", description = "Method is not allowed.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    }
    )
    public ResponseEntity<ResponseDto<List<TraineeCriteriaTrainingsResponseDto>>> getTraineeTrainingsFilter(
            @PathVariable("username") String username,
            @Valid @RequestBody TraineeTrainingsFilterRequestDto trainingsDto) {
        log.debug("Request for getting trainee's: {} trainings by filter"
                        + "(dateFrom: {} , dateTo: {}, trainerName: {}, trainingType: {})",
                username, trainingsDto.getFromDate(), trainingsDto.getToDate(),
                trainingsDto.getTrainerUsername(), trainingsDto.getTrainingType());

        List<TrainingEntity> trainings = trainingService.getTraineeTrainingsByFilter(trainingsDto);
        List<TraineeCriteriaTrainingsResponseDto> payload =
                trainings.stream().map(trainingMapper::traineeTrainingsEntityToCriteriaDto)
                        .toList();

        return new ResponseEntity<>(new ResponseDto<>(payload, "Successfully retrieved trainee trainings"),
                HttpStatus.OK);
    }

    /**
     * GET request to get trainer trainings by given criteria.
     *
     * @param trainingsDto {@code TrainerTrainingsFilterRequestDto} the given criteria
     * @return {@code List<TrainerCriteriaTrainingsResponseDto>}
     */
    @GetMapping("/trainers/{username}")
    @Operation(description = "Getting trainer trainings.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully got trainer trainings.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(responseCode = "401", description = "Authentication error",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "405", description = "Method is not allowed.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    }
    )
    public ResponseEntity<ResponseDto<List<TrainerCriteriaTrainingsResponseDto>>> getTrainerTrainingsFilter(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerTrainingsFilterRequestDto trainingsDto) {
        log.debug("Request for getting trainer's: {} trainings by filter"
                        + "(dateFrom: {} , dateTo: {}, traineeName: {})",
                username, trainingsDto.getFromDate(), trainingsDto.getToDate(),
                trainingsDto.getTraineeUsername());

        List<TrainingEntity> trainings = trainingService.getTrainerTrainingsByFilter(trainingsDto);
        List<TrainerCriteriaTrainingsResponseDto> payload =
                trainings.stream().map(trainingMapper::trainerTrainingsEntityToCriteriaDto)
                        .toList();
        return new ResponseEntity<>(new ResponseDto<>(payload, "Successfully retrieved trainer trainings"),
                HttpStatus.OK);


    }
}
