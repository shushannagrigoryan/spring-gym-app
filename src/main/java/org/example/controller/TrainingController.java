package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingCreateRequestDto;
import org.example.services.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainings")
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;

    /** Setting dependencies. */
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
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
}
