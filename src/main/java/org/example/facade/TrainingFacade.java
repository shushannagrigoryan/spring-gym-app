//package org.example.facade;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.example.dto.TrainingDto;
//import org.example.entity.TrainingEntity;
//import org.example.mapper.TrainingMapper;
//import org.example.services.TrainingService;
//import org.example.validation.TrainingValidation;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class TrainingFacade {
//    private final TrainingService trainingService;
//    private final TrainingMapper trainingMapper;
//    private final TrainingValidation trainingValidation;
//
//    /**
//     * Injecting {@code TrainingFacade} dependencies.
//     */
//    public TrainingFacade(TrainingService trainingService,
//                          TrainingMapper trainingMapper,
//                          TrainingValidation trainingValidation) {
//        this.trainingService = trainingService;
//        this.trainingMapper = trainingMapper;
//        this.trainingValidation = trainingValidation;
//    }
//
//
//    /**
//     * Creates a new training in the facade layer.
//     *
//     * @param trainingDto {@code TrainingDto} to create the {@code TrainingEntity}
//     */
//    public void createTraining(TrainingDto trainingDto) {
//        log.info("Request to create training");
//        trainingValidation.validateTraining(trainingDto);
//        trainingService.createTraining(trainingMapper.dtoToEntity(trainingDto));
//        log.info("Successfully created training");
//    }
//
//    /**
//     * Gets the training by id in the facade layer.
//     *
//     * @param id id of the training
//     * @return returns the {@code TrainingDto}
//     */
//    public TrainingDto getTrainingById(Long id) {
//        log.info("Request to retrieve training by id");
//        TrainingEntity trainingDto;
//        trainingDto = trainingService.getTrainingById(id);
//        return trainingMapper.entityToDto(trainingDto);
//    }
//
//    /**
//     * Updates training with the given id.
//     *
//     * @param trainingId  id of the training
//     * @param trainingDto new training data
//     */
//    public void updateTraining(Long trainingId, TrainingDto trainingDto) {
//        log.info("Request to update training with id: {}", trainingId);
//        trainingService.updateTraining(trainingId, trainingMapper.dtoToEntity(trainingDto));
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
//     * @return {@code List<TrainingDto>}
//     */
//    public List<TrainingDto> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
//                                                         LocalDate toDate, Long trainingTypeId,
//                                                         String trainerUsername) {
//        log.debug("Request to get trainees trainings by trainee username: {} "
//                        + "and criteria: fromDate:{} toDate:{} trainingType: {} trainerUsername: {}",
//                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);
//
//        List<TrainingEntity> trainingList = trainingService
//                .getTraineeTrainingsByFilter(traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);
//
//        return trainingList.stream().map(trainingMapper::entityToDto).collect(Collectors.toList());
//    }
//
//    /**
//     * Returns trainers trainings list by trainer username and given criteria.
//     *
//     * @param trainerUsername username of the trainer
//     * @param fromDate        training fromDate
//     * @param toDate          training toDate
//     * @param traineeUsername trainee username
//     * @return {@code List<TrainingDto>}
//     */
//    public List<TrainingDto> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
//                                                         LocalDate toDate, String traineeUsername) {
//        log.debug("Request to get trainers trainings by trainer username: {} "
//                        + "and criteria: fromDate:{} toDate:{} traineeUsername: {}",
//                trainerUsername, fromDate, toDate, traineeUsername);
//
//        List<TrainingEntity> trainingList = trainingService
//                .getTrainerTrainingsByFilter(trainerUsername, fromDate, toDate, traineeUsername);
//
//        return trainingList.stream().map(trainingMapper::entityToDto).collect(Collectors.toList());
//
//    }
//}
