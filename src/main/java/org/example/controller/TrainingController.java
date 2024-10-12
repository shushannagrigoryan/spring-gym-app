package org.example.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainerTrainingsFilterRequestDto;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.dto.responsedto.TraineeCriteriaTrainingsResponseDto;
import org.example.dto.responsedto.TrainerCriteriaTrainingsResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping("/add-training")
    public ResponseEntity<String> createTraining(@Valid @RequestBody TrainingCreateRequestDto trainingDto) {
        log.debug("Request to create new training: {}", trainingDto);
        trainingService.createTraining(trainingDto);
        return new ResponseEntity<>("Successfully created a new training.", HttpStatus.OK);
    }

    /**
     * GET request to get trainee trainings by given criteria.
     *
     * @param trainingsDto {@code TraineeTrainingsFilterRequestDto} the given criteria
     * @return {@code List<TraineeCriteriaTrainingsResponseDto>}
     */
    @GetMapping("/trainee-trainings")
    public ResponseEntity<List<TraineeCriteriaTrainingsResponseDto>> getTraineeTrainingsFilter(
            @Valid @RequestBody TraineeTrainingsFilterRequestDto trainingsDto) {
        log.debug("Request for getting trainee's: {} trainings by filter"
                        + "(dateFrom: {} , dateTo: {}, trainerName: {}, trainingType: {})",
                trainingsDto.getTraineeUsername(), trainingsDto.getFromDate(), trainingsDto.getToDate(),
                trainingsDto.getTrainerUsername(), trainingsDto.getTrainingType());

        List<TrainingEntity> trainings = trainingService.getTraineeTrainingsByFilter(trainingsDto);
        return new ResponseEntity<>(trainings.stream().map(trainingMapper::traineeTrainingsEntityToCriteriaDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);


    }

    /**
     * GET request to get trainer trainings by given criteria.
     *
     * @param trainingsDto {@code TrainerTrainingsFilterRequestDto} the given criteria
     * @return {@code List<TrainerCriteriaTrainingsResponseDto>}
     */
    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainerCriteriaTrainingsResponseDto>> getTraineeTrainingsFilter(
            @Valid @RequestBody TrainerTrainingsFilterRequestDto trainingsDto) {
        log.debug("Request for getting trainer's: {} trainings by filter"
                        + "(dateFrom: {} , dateTo: {}, traineeName: {})",
                trainingsDto.getTrainerUsername(), trainingsDto.getFromDate(), trainingsDto.getToDate(),
                trainingsDto.getTraineeUsername());

        List<TrainingEntity> trainings = trainingService.getTrainerTrainingsByFilter(trainingsDto);
        return new ResponseEntity<>(trainings.stream().map(trainingMapper::trainerTrainingsEntityToCriteriaDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);


    }
}
