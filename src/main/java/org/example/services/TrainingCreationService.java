package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingCreationService {
    private final TrainingRepository trainingRepository;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;


    /**
     * Injecting dependencies using constructor.
     */
    public TrainingCreationService(TrainingRepository trainingRepository,
                           TraineeService traineeService,
                           TrainerService trainerService,
                           TrainingTypeService trainingTypeService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Creates training in service layer.
     * If either trainee or trainer is not found throws an {@code GymIllegalIdException}
     *
     * @param trainingEntity {@code TrainingEntity} to create the trainee
     */
    public void createTraining(TrainingEntity trainingEntity) {
        log.debug("Creating training : {}", trainingEntity);

        trainingTypeService.getTrainingTypeById(trainingEntity.getTrainingTypeId());
        trainerService.getTrainerById(trainingEntity.getTrainerId());
        traineeService.getTraineeById(trainingEntity.getTraineeId());

        trainingRepository.createTraining(trainingEntity);
        log.debug("Successfully created new training with id: {}", trainingEntity.getId());
    }

}
