package org.example.repository;

import org.example.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {
    //    private final SessionFactory sessionFactory;
    //
    //    public TrainingRepository(SessionFactory sessionFactory) {
    //        this.sessionFactory = sessionFactory;
    //    }
    //
    //    /**
    //     * Generates id for the training entity and adds that entity to the storage map.
    //     *
    //     * @param trainingEntity {@code TrainingEntity} to be added to storage
    //     */
    //    public void createTraining(TrainingEntity trainingEntity) {
    //        log.debug("Saving training: {} to storage", trainingEntity);
    //        Session session = sessionFactory.getCurrentSession();
    //        session.persist(trainingEntity);
    //    }
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
    //    /**
    //     * Gets all trainings.
    //     *
    //     * @return {@code Optional<TrainingEntity>}
    //     */
    //    public List<TrainingEntity> getAllTrainings() {
    //        log.debug("Getting all trainings.");
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from TrainingEntity";
    //        Query<TrainingEntity> query = session.createQuery(hql, TrainingEntity.class);
    //        return query.getResultList();
    //    }
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
    //    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
    //                                                            LocalDate toDate, Long trainingTypeId,
    //                                                            String trainerUsername) {
    //
    //        Session session = sessionFactory.getCurrentSession();
    //
    //        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    //        CriteriaQuery<TrainingEntity> criteriaQuery
    //                = criteriaBuilder.createQuery(TrainingEntity.class);
    //
    //        Root<TrainingEntity> trainingRoot = criteriaQuery.from(TrainingEntity.class);
    //
    //        Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot
    //                .join("trainee", JoinType.INNER)
    //                .join("user", JoinType.INNER);
    //
    //        Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot
    //                .join("trainer", JoinType.INNER)
    //                .join("user", JoinType.INNER);
    //
    //
    //        List<Predicate> predicates = new ArrayList<>();
    //
    //        predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
    //
    //        if (fromDate != null && toDate != null) {
    //            predicates.add(criteriaBuilder
    //                    .between(trainingRoot.get("trainingDate"), fromDate, toDate));
    //        }
    //
    //        if (trainingTypeId != null) {
    //            predicates.add(criteriaBuilder
    //                    .equal(trainingRoot.get("trainingType").get("id"), trainingTypeId));
    //        }
    //
    //        if (trainerUsername != null) {
    //            predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
    //        }
    //
    //        criteriaQuery.select(trainingRoot)
    //                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    //
    //        List<TrainingEntity> trainings = session.createQuery(criteriaQuery).getResultList();
    //        log.debug("Successfully retrieved trainee's trainings by given criteria");
    //
    //        return trainings;
    //    }
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
