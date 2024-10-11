package org.example.repository;

import java.time.LocalDate;
import java.util.Set;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {

    Set<TrainingEntity> getTrainingEntitiesByTraineeAndTrainingDateIsBetweenAndTrainerEqualsAndTrainingType_Id(
            TraineeEntity traineeEntity, LocalDate fromDate,
            LocalDate toDate, TrainerEntity trainerEntity, Long trainingType);



    //
    //
    //    /**
    //     * Gets training by id.
    //     *
    //     * @param id id of the training
    //     * @return {@code Optional<TrainingEntity>}
    //     */
    //    public TrainingEntity getTrainingById(Long id) {
    //        log.debug("Getting training with id: {}", id);
    //        Session session = sessionFactory.getCurrentSession();
    //        return session.get(TrainingEntity.class, id);
    //    }
    //
    //

    //
    //    /**Updating given training.
    //     *
    //     * @param training new training data
    //     */
    //
    //    public void updateTraining(TrainingEntity training) {
    //        log.debug("Request to update training: {}", training);
    //        Session session = sessionFactory.getCurrentSession();
    //        session.merge(training);
    //    }
    //

    //
    //    /**
    //     * Returns trainees trainings.
    //     *
    //     * @param traineeUsername trainee username
    //     * @return {@code List<TrainingEntity>}
    //     */
    //    public List<TrainingEntity> getTraineeTrainingsList(String traineeUsername) {
    //        log.debug("Getting trainings by trainee username: {}", traineeUsername);
    //
    //        List<TrainingEntity> trainings;
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from TrainingEntity t where t.trainee.user.username = :traineeUsername";
    //        trainings = session.createQuery(hql, TrainingEntity.class)
    //                .setParameter("traineeUsername", traineeUsername)
    //                .getResultList();
    //
    //        log.debug("Successfully retrieved trainings by trainee username: {}", traineeUsername);
    //        return trainings;
    //    }
    //
    //    /**
    //     * Returns trainer trainings list by trainer username and given criteria.
    //     *
    //     * @param trainerUsername trainer username
    //     * @param fromDate        training fromDate
    //     * @param toDate          training toDate
    //     * @param traineeUsername trainee username
    //     * @return {@code List<TrainingEntity>}
    //     */
    //
    //    public List<TrainingEntity> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
    //                                                            LocalDate toDate, String traineeUsername) {
    //        List<TrainingEntity> trainings;
    //
    //        Session session = sessionFactory.getCurrentSession();
    //        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    //        CriteriaQuery<TrainingEntity> criteriaQuery
    //                = criteriaBuilder.createQuery(TrainingEntity.class);
    //
    //        Root<TrainingEntity> trainingRoot = criteriaQuery.from(TrainingEntity.class);
    //
    //        Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot
    //                .join("trainer", JoinType.INNER)
    //                .join("user", JoinType.INNER);
    //
    //        Join<TrainingEntity, TrainerEntity> traineeJoin = trainingRoot
    //                .join("trainee", JoinType.INNER)
    //                .join("user", JoinType.INNER);
    //
    //
    //        List<Predicate> predicates = new ArrayList<>();
    //
    //        predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
    //
    //        if (fromDate != null && toDate != null) {
    //            predicates.add(criteriaBuilder
    //                    .between(trainingRoot.get("trainingDate"), fromDate, toDate));
    //        }
    //
    //        if (traineeUsername != null) {
    //            predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
    //        }
    //
    //        criteriaQuery.select(trainingRoot)
    //                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    //
    //        trainings = session.createQuery(criteriaQuery).getResultList();
    //        log.debug("Successfully retrieved trainer's trainings by given criteria");
    //
    //        return trainings;
    //    }
}
