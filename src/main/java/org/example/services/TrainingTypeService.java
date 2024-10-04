//package org.example.services;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.entity.TrainingTypeEntity;
//import org.example.exceptions.GymIllegalIdException;
//import org.example.repository.TrainingTypeRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Slf4j
//public class TrainingTypeService {
//    private final TrainingTypeRepository trainingTypeRepository;
//
//    /**
//     * Injecting dependencies using constructor.
//     */
//    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
//        this.trainingTypeRepository = trainingTypeRepository;
//    }
//
//
//    /**
//     * Gets the training type by id in the service layer.
//     * If no training type is found throws an {@code GymIllegalIdException}
//     *
//     * @param id id of the training type to get
//     * @return the {@code Optional<TrainingTypeEntity>}
//     */
//    @Transactional
//    public TrainingTypeEntity getTrainingTypeById(Long id) {
//        log.debug("Retrieving trainingType by id: {}", id);
//        TrainingTypeEntity trainingType = trainingTypeRepository.getTrainingTypeById(id);
//        if (trainingType == null) {
//            throw new GymIllegalIdException(String.format("No training type with id: %d", id));
//        }
//        log.debug("Successfully retrieved training type by id: {}", id);
//        return trainingType;
//
//    }
//
//
//}
