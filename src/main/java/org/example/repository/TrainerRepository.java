//package org.example.repository;
//
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Join;
//import jakarta.persistence.criteria.JoinType;
//import jakarta.persistence.criteria.Root;
//import jakarta.persistence.criteria.Subquery;
//import java.util.List;
//import java.util.Optional;
//import lombok.extern.slf4j.Slf4j;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.example.entity.UserEntity;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@Slf4j
//public class TrainerRepository {
//    private final SessionFactory sessionFactory;
//
//    /**
//     * Injecting dependencies using constructor.
//     */
//    public TrainerRepository(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    /**
//     * Saving trainer.
//     *
//     * @param trainerEntity {@code TrainerEntity} to be added to storage
//     */
//    public TrainerEntity save(TrainerEntity trainerEntity) {
//        log.debug("Saving trainer.");
//        Session session = sessionFactory.getCurrentSession();
//        session.persist(trainerEntity.getUser());
//        return session.merge(trainerEntity);
//    }
//
//
//    /**
//     * Gets trainer by username.
//     *
//     * @param username username of the trainer
//     * @return {@code Optional<TrainerEntity>}
//     */
//    public Optional<TrainerEntity> findByUsername(String username) {
//        log.debug("Getting trainer with username: {}", username);
//        Session session = sessionFactory.getCurrentSession();
//        TrainerEntity trainer;
//        String hql = "from TrainerEntity t where t.user.username = :username";
//        Query<TrainerEntity> query = session.createQuery(hql, TrainerEntity.class);
//        query.setParameter("username", username);
//        trainer = query.uniqueResult();
//
//        return Optional.ofNullable(trainer);
//    }
//
//    /**
//     * Gets trainer by id.
//     *
//     * @param id id of the trainer
//     * @return {@code TrainerEntity}
//     */
//    public Optional<TrainerEntity> findById(Long id) {
//        log.debug("Getting trainer with id: {}", id);
//        Session session = sessionFactory.getCurrentSession();
//        return Optional.ofNullable(session.get(TrainerEntity.class, id));
//    }
//
//
//    /**
//     * Updates trainer entity in storage by id.
//     * If no trainer is found throws an {@code IllegalIdException}
//     *
//     * @param id            id of the trainer to be updated
//     * @param trainerEntity new {@code TrainerEntity} to update with
//     */
//    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
//        Session session = sessionFactory.getCurrentSession();
//        session.merge(trainerEntity);
//        log.debug("Updating trainer with id: {} with {}", id, trainerEntity);
//    }
//
//    /**
//     * Getting trainers list that are not assigned to the given trainee.
//     *
//     * @param traineeUsername trainee username.
//     * @return {@code List<TrainerEntity>}
//     */
//    public List<TrainerEntity> getTrainersNotAssignedToTraineeActiveTrainers(String traineeUsername) {
//        Session session = sessionFactory.getCurrentSession();
//        List<TrainerEntity> trainers;
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        CriteriaQuery<TrainerEntity> criteriaQuery = criteriaBuilder.createQuery(TrainerEntity.class);
//
//        Root<TrainerEntity> trainerRoot = criteriaQuery.from(TrainerEntity.class);
//
//        Join<TrainerEntity, UserEntity> userJoin = trainerRoot.join("user", JoinType.INNER);
//
//
//        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//        Root<TrainingEntity> trainingRoot = subquery.from(TrainingEntity.class);
//
//        Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot.join("trainee", JoinType.INNER)
//                .join("user", JoinType.INNER);
//
//        subquery.select(trainingRoot.get("trainer").get("id"))
//                .where(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
//
//        criteriaQuery.select(trainerRoot).where(criteriaBuilder.and(
//                                criteriaBuilder.not(trainerRoot.get("id").in(subquery)),
//                                criteriaBuilder.equal(userJoin.get("isActive"), true)));
//
//
//        trainers = session.createQuery(criteriaQuery).getResultList();
//
//        log.debug("Successfully retrieved trainers not assigned to trainee: {}", traineeUsername);
//
//        return trainers;
//    }
//}
