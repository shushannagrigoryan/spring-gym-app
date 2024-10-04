package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingCreateDto;
import org.example.services.TrainingService;
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

    @PostMapping
    public void createTraining(@RequestBody TrainingCreateDto trainingCreateDto) {
        log.debug("Creating new training: {}", trainingCreateDto);
        trainingService.createTraining(trainingCreateDto);
    }
}
