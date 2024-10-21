package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TrainingCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns trainees trainings list by trainee username and given criteria.
     *
     * @param traineeUsername username of the trainee
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param trainingTypeId  training type
     * @param trainerUsername trainer username
     * @return {@code Set<TrainingEntity>}
     */

    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
                                                            LocalDate toDate, Long trainingTypeId,
                                                            String trainerUsername) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> criteriaQuery
                = criteriaBuilder.createQuery(TrainingEntity.class);

        Root<TrainingEntity> trainingRoot = criteriaQuery.from(TrainingEntity.class);

        Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot
                .join("trainee", JoinType.INNER)
                .join("user", JoinType.INNER);

        Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot
                .join("trainer", JoinType.INNER)
                .join("user", JoinType.INNER);


        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));

        if (fromDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), fromDate));
        }

        if (toDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(trainingRoot.get("trainingDate"), toDate));
        }

        if (trainingTypeId != null) {
            predicates.add(criteriaBuilder
                    .equal(trainingRoot.get("trainingType").get("id"), trainingTypeId));
        }

        if (trainerUsername != null && !trainerUsername.isBlank()) {
            predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
        }

        criteriaQuery.select(trainingRoot)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        List<TrainingEntity> trainings = entityManager.createQuery(criteriaQuery).getResultList();
        log.debug("Successfully retrieved trainee's trainings by given criteria");

        return trainings;
    }

    /**
     * Returns trainer trainings list by trainer username and given criteria.
     *
     * @param trainerUsername trainer username
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param traineeUsername trainee username
     * @return {@code List<TrainingEntity>}
     */

    public List<TrainingEntity> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
                                                            LocalDate toDate, String traineeUsername) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> criteriaQuery
                = criteriaBuilder.createQuery(TrainingEntity.class);

        Root<TrainingEntity> trainingRoot = criteriaQuery.from(TrainingEntity.class);

        Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot
                .join("trainer", JoinType.INNER)
                .join("user", JoinType.INNER);

        Join<TrainingEntity, TrainerEntity> traineeJoin = trainingRoot
                .join("trainee", JoinType.INNER)
                .join("user", JoinType.INNER);


        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));

        if (fromDate != null) {
            predicates.add(criteriaBuilder
                    .greaterThanOrEqualTo(trainingRoot.get("trainingDate"), fromDate));
        }

        if (toDate != null) {
            predicates.add(criteriaBuilder
                    .lessThanOrEqualTo(trainingRoot.get("trainingDate"), toDate));
        }

        if (traineeUsername != null && !traineeUsername.isBlank()) {
            predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
        }

        criteriaQuery.select(trainingRoot)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        List<TrainingEntity> trainings = entityManager.createQuery(criteriaQuery).getResultList();
        log.debug("Successfully retrieved trainer's trainings by given criteria");

        return trainings;
    }

}
