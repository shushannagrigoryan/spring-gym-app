package org.example.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TrainerRepository {
    private final SessionFactory sessionFactory;
    private final UserRepository userRepository;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerRepository(SessionFactory sessionFactory,
                             UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }

    /**
     * Adding trainer to database.
     *
     * @param trainerEntity {@code TrainerEntity} to be added to storage
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        Session session = sessionFactory.getCurrentSession();
        UserEntity user = trainerEntity.getUser();
        session.persist(user);
        session.persist(trainerEntity);
    }


    /**
     * Gets trainer by username.
     *
     * @param username username of the trainer
     * @return {@code Optional<TrainerEntity>}
     */
    public TrainerEntity getTrainerByUsername(String username) {
        log.debug("Getting trainer with username: {}", username);
        Session session = sessionFactory.getCurrentSession();
        TrainerEntity trainer;
        String hql = "from TrainerEntity t where t.user.username = :username";
        Query<TrainerEntity> query = session.createQuery(hql, TrainerEntity.class);
        query.setParameter("username", username);
        trainer = query.uniqueResult();

        return trainer;
    }

    /**
     * Gets trainer by id.
     *
     * @param id id of the trainer
     * @return {@code TrainerEntity}
     */
    public TrainerEntity getTrainerById(Long id) {
        log.debug("Getting trainer with id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return session.get(TrainerEntity.class, id);
    }

    /**
     * Changes the password of the trainer by username.
     *
     * @param username username of the trainer
     */
    public void changeTrainerPassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from TrainerEntity t where t.user.username =:username";
        TrainerEntity trainer = session.createQuery(hql, TrainerEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        trainer.getUser().setPassword(password);
        session.merge(trainer);
        log.debug("Successfully updated password of the trainer with username {}", username);
    }

    /**
     * Updates trainer entity in storage by id.
     * If no trainer is found throws an {@code IllegalIdException}
     *
     * @param id            id of the trainer to be updated
     * @param trainerEntity new {@code TrainerEntity} to update with
     */
    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(trainerEntity);
        log.debug("Updating trainer with id: {} with {}", id, trainerEntity);
    }

    /**
     * Activates trainer.
     *
     * @param trainer trainer to activate
     */
    public void activateTrainer(TrainerEntity trainer) {
        log.debug("Activating trainer : {}", trainer);
        Session session = sessionFactory.getCurrentSession();
        trainer.getUser().setActive(true);
        session.merge(trainer);
        log.debug("Successfully activated trainer: {}", trainer);
    }

    /**
     * Deactivates trainer.
     *
     * @param trainer id of the trainer to deactivate
     */
    public void deactivateTrainer(TrainerEntity trainer) {
        log.debug("Deactivating trainer: {}", trainer);
        Session session = sessionFactory.getCurrentSession();
        trainer.getUser().setActive(false);
        session.merge(trainer);
        log.debug("Successfully deactivated trainer: {}", trainer);
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
        List<TrainingEntity> trainings;

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
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

        if (fromDate != null && toDate != null) {
            predicates.add(criteriaBuilder
                    .between(trainingRoot.get("trainingDate"), fromDate, toDate));
        }

        if (traineeUsername != null) {
            predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
        }

        criteriaQuery.select(trainingRoot)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        trainings = session.createQuery(criteriaQuery).getResultList();
        log.debug("Successfully retrieved trainer's trainings by given criteria");

        return trainings;
    }

    /**
     * Getting trainers list that are not assigned to the given trainee.
     *
     * @param traineeUsername trainee username.
     * @return {@code List<TrainerEntity>}
     */
    public List<TrainerEntity> getTrainersNotAssignedToTrainee(String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();
        List<TrainerEntity> trainers;
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<TrainerEntity> criteriaQuery = criteriaBuilder.createQuery(TrainerEntity.class);

        Root<TrainerEntity> trainerRoot = criteriaQuery.from(TrainerEntity.class);

        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<TrainingEntity> trainingRoot = subquery.from(TrainingEntity.class);

        Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot.join("trainee", JoinType.INNER)
                .join("user", JoinType.INNER);

        subquery.select(trainingRoot.get("trainer").get("id"))
                .where(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));

        criteriaQuery.select(trainerRoot)
                .where(criteriaBuilder.not(trainerRoot.get("id").in(subquery)));

        trainers = session.createQuery(criteriaQuery).getResultList();

        log.debug("Successfully retrieved trainers not assigned to trainee: {}", traineeUsername);

        return trainers;
    }

}
