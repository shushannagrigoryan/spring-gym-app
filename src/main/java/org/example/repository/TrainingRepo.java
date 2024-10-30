//package org.example.repository;
//
//import java.util.List;
//import java.util.Optional;
//import lombok.extern.slf4j.Slf4j;
//import org.example.entity.TrainingEntity;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@Slf4j
//public class TrainingRepository {
//    private final SessionFactory sessionFactory;
//
//    public TrainingRepository(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    /**
//     * Saves training.
//     *
//     * @param trainingEntity {@code TrainingEntity} to be added to storage
//     */
//    public TrainingEntity save(TrainingEntity trainingEntity) {
//        log.debug("Saving training: {} to storage", trainingEntity);
//        Session session = sessionFactory.getCurrentSession();
//        return session.merge(trainingEntity);
//    }
//
//
//    /**
//     * Gets training by id.
//     *
//     * @param id id of the training
//     * @return {@code Optional<TrainingEntity>}
//     */
//    public Optional<TrainingEntity> findById(Long id) {
//        log.debug("Getting training with id: {}", id);
//        Session session = sessionFactory.getCurrentSession();
//        return Optional.ofNullable(session.get(TrainingEntity.class, id));
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
//}
