package org.example.specifications;

import java.time.LocalDateTime;
import org.example.entity.TrainingEntity;
import org.springframework.data.jpa.domain.Specification;

public class TrainingSpecification {
    public static Specification<TrainingEntity> hasTraineeUsername(String traineeUserUsername) {
        return (root, query, cb) ->
                traineeUserUsername == null ? cb.conjunction() : cb.equal(root.get("trainee")
                        .get("user").get("username"), traineeUserUsername);
    }

    public static Specification<TrainingEntity> hasTrainingDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, cb) -> {
            if (fromDate == null && toDate == null) {
                return cb.conjunction();
            } else if (fromDate == null) {
                return cb.lessThanOrEqualTo(root.get("trainingDate"), toDate);
            } else if (toDate == null) {
                return cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDate);
            } else {
                return cb.between(root.get("trainingDate"), fromDate, toDate);
            }
        };
    }

    public static Specification<TrainingEntity> hasTrainingType(Long trainingTypeId) {
        return (root, query, cb) ->
                trainingTypeId == null ? cb.conjunction() : cb.equal(root.get("trainingType").get("id"), trainingTypeId);
    }

    public static Specification<TrainingEntity> hasTrainerUsername(String trainerUserUsername) {
        return (root, query, cb) ->
                trainerUserUsername == null ? cb.conjunction() : cb.equal(root.get("trainer").get("user").get("username"), trainerUserUsername);
    }
}
