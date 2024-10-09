package org.example.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingCreateRequestDto;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingRepository trainingRepository,
                           TrainingTypeService trainingTypeService,
                           TrainerService trainerService,
                           TraineeService traineeService) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    /**
     * Creates training in service layer.
     * If either trainee or trainer is not found throws an {@code GymEntityNotFoundException}
     * Throws GymIllegalIdException if training type does not exist.
     *
     * @param trainingCreateDto {@code TrainingCreateRequestDto} to create the training
     */
    @Transactional
    public void createTraining(TrainingCreateRequestDto trainingCreateDto) {
        log.debug("Creating training : {}", trainingCreateDto);

        TrainingEntity training = new TrainingEntity();
        training.setTrainingName(trainingCreateDto.getTrainingName());
        training.setTrainingDate(trainingCreateDto.getTrainingDate());
        training.setTrainingDuration(trainingCreateDto.getTrainingDuration());
        training.setTrainingType(trainingTypeService.getTrainingTypeById(trainingCreateDto.getTrainingType()));
        training.setTrainee(traineeService.getTraineeByUsername(trainingCreateDto.getTraineeUsername()));
        training.setTrainer(trainerService.getTrainerByUsername(trainingCreateDto.getTrainerUsername()));

        TrainingEntity createdTraining = trainingRepository.save(training);
        log.debug("Successfully created new training with id: {}", createdTraining.getId());
    }

    //    /**
    //     * Gets training by id.
    //     *
    //     * @param id of the training
    //     * @return the {@code TrainingEntity}
    //     */
    //    @Transactional
    //    public TrainingEntity getTrainingById(Long id) {
    //        log.debug("Retrieving training by id: {}", id);
    //        TrainingEntity training = trainingRepository.getTrainingById(id);
    //        if (training == null) {
    //            log.debug("Invalid id for training: {}", id);
    //            throw new GymIllegalIdException(String.format("No training with id: %d", id));
    //        }
    //        log.debug("Getting training with id: {}", id);
    //        return training;
    //    }
    //
    //
    //    /**
    //     * Updates training by id.
    //     *
    //     * @param trainingId     training id
    //     * @param trainingEntity new training data
    //     */
    //    @Transactional
    //    public void updateTraining(Long trainingId, TrainingEntity trainingEntity) {
    //        TrainingEntity training = trainingRepository.getTrainingById(trainingId);
    //        if (training == null) {
    //            throw new GymIllegalIdException(String.format("No training with id: %d", trainingId));
    //        }
    //
    //        training.setTrainingName(trainingEntity.getTrainingName());
    //        training.setTrainingType(trainingTypeService.getTrainingTypeById(trainingEntity.getTrainingTypeId()));
    //        training.setTrainingDate(trainingEntity.getTrainingDate());
    //        training.setTrainingDuration(trainingEntity.getTrainingDuration());
    //        training.setTrainee(traineeService.getTraineeById(trainingEntity.getTraineeId()));
    //        training.setTrainer(trainerService.getTrainerById(trainingEntity.getTrainerId()));
    //
    //        trainingRepository.updateTraining(training);
    //    }
    //
    //    /**
    //     * Returns trainers trainings list by trainer username and given criteria.
    //     *
    //     * @param trainerUsername username of the trainer
    //     * @param fromDate        training fromDate
    //     * @param toDate          training toDate
    //     * @param traineeUsername trainee username
    //     * @return {@code List<TrainingEntity>}
    //     */
    //
    //    @Transactional
    //    public List<TrainingEntity> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
    //                                                            LocalDate toDate, String traineeUsername) {
    //
    //        return trainingRepository.getTrainerTrainingsByFilter(trainerUsername, fromDate,
    //                toDate, traineeUsername);
    //    }
    //
    //    /**
    //     * Returns trainees trainings list by trainee username and given criteria.
    //     *
    //     * @param traineeUsername username of the trainee
    //     * @param fromDate        training fromDate
    //     * @param toDate          training toDate
    //     * @param trainingTypeId  training type
    //     * @param trainerUsername trainer username
    //     * @return {@code List<TrainingEntity>}
    //     */
    //
    //    @Transactional
    //    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
    //                                                            LocalDate toDate, Long trainingTypeId,
    //                                                            String trainerUsername) {
    //        log.debug("Getting trainee trainings by filter");
    //        return trainingRepository.getTraineeTrainingsByFilter(traineeUsername, fromDate,
    //                toDate, trainingTypeId, trainerUsername);
    //    }
}
