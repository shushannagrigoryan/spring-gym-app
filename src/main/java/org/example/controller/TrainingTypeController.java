package org.example.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingTypeResponseDto;
import org.example.entity.TrainingTypeEntity;
import org.example.mapper.TrainingTypeMapper;
import org.example.services.TrainingTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/training-types")
@Slf4j
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    /** Setting dependencies. */
    public TrainingTypeController(TrainingTypeService trainingTypeService,
                                  TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDto>> getTrainingTypes() {
        log.debug("Request to get all training types.");
        List<TrainingTypeEntity> trainingTypes = trainingTypeService.getAllTrainingTypes();
        return new ResponseEntity<>(trainingTypes.stream()
                .map(trainingTypeMapper::entityToResponseDto).toList(),
                HttpStatus.OK);
    }
}
