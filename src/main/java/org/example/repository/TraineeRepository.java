package org.example.repository;

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
import org.example.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TraineeRepository {
    private final SessionFactory sessionFactory;

    public TraineeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Adding trainee to database.
     *
     * @param traineeEntity {@code TraineeEntity} to be added to storage
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        Session session = sessionFactory.getCurrentSession();
        UserEntity user = traineeEntity.getUser();
        session.persist(user);
        session.persist(traineeEntity);
    }

    /**
     * Gets trainee by username.
     *
     * @param username username of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public TraineeEntity getTraineeByUsername(String username) {
        log.debug("Getting trainee with username: {}", username);
        TraineeEntity trainee;
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM TraineeEntity t WHERE t.user.username = :username";
        Query<TraineeEntity> query = session.createQuery(hql, TraineeEntity.class);
        query.setParameter("username", username);
        trainee = query.uniqueResult();
        return trainee;
    }

    /**
     * Gets trainee by id.
     *
     * @param id id of the trainee
     * @return {@code TraineeEntity}
     */
    public TraineeEntity getTraineeById(Long id) {
        log.debug("Getting trainee with id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return session.get(TraineeEntity.class, id);
    }

    /**
     * Changes the password of the trainee by username.
     *
     * @param username username of the trainee
     */
    public void changeTraineePassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from TraineeEntity t where t.user.username =:username";
        TraineeEntity trainee = session.createQuery(hql, TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        trainee.getUser().setPassword(password);
        session.merge(trainee);

        log.debug("Successfully updated password of the trainee with username {}", username);
    }

    /**
     * Activates trainee.
     *
     * @param trainee trainee to activate
     */
    public void activateTrainee(TraineeEntity trainee) {
        log.debug("Activating trainee :{}", trainee);
        Session session = sessionFactory.getCurrentSession();
        trainee.getUser().setActive(true);
        session.merge(trainee);
        log.debug("Successfully activated trainee: {}", trainee);
    }

    /**
     * Deactivates trainee.
     *
     * @param trainee trainee to deactivate
     */
    public void deactivateTrainee(TraineeEntity trainee) {
        log.debug("Deactivating trainee :{}", trainee);
        Session session = sessionFactory.getCurrentSession();
        trainee.getUser().setActive(false);
        session.merge(trainee);
        log.debug("Successfully deactivated trainee: {}", trainee);
    }

    /**
     * Updates trainee entity in storage by id.
     * If no trainee is found throws an {@code IllegalIdException}
     *
     * @param id            id of the trainee to be updated
     * @param traineeEntity new {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(traineeEntity);
        log.debug("Updating trainee with id: {} with {}", id, traineeEntity);
    }


    /**
     * Deletes the trainee by username.
     * If no trainee is found throws an {@code GymIllegalUsernameException}.
     *
     * @param username username of the trainee to be deleted
     */
    public void deleteTraineeByUsername(String username) {
        log.debug("Deleting trainee with username: {}", username);

        Session session = sessionFactory.getCurrentSession();
        String hql = "from TraineeEntity t where t.user.username =:username";
        TraineeEntity trainee = session
                .createQuery(hql, TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        session.remove(trainee.getUser());
        session.remove(trainee);
        log.debug("Successfully deleted trainee with username: {}", username);
    }

    /**
     * Returns trainees trainings list by trainee username and given criteria.
     *
     * @param traineeUsername username of the trainee
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param trainingTypeId  training type
     * @param trainerUsername trainer username
     * @return {@code List<TrainingEntity>}
     */

    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
                                                            LocalDate toDate, Long trainingTypeId,
                                                            String trainerUsername) {

        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
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

        if (fromDate != null && toDate != null) {
            predicates.add(criteriaBuilder
                    .between(trainingRoot.get("trainingDate"), fromDate, toDate));
        }

        if (trainingTypeId != null) {
            predicates.add(criteriaBuilder
                    .equal(trainingRoot.get("trainingType").get("id"), trainingTypeId));
        }

        if (trainerUsername != null) {
            predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
        }

        criteriaQuery.select(trainingRoot)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        List<TrainingEntity> trainings = session.createQuery(criteriaQuery).getResultList();
        log.debug("Successfully retrieved trainee's trainings by given criteria");

        return trainings;
    }

    /**
     * Returns trainees trainings.
     *
     * @param traineeUsername trainee username
     * @return {@code List<TrainingEntity>}
     */
    public List<TrainingEntity> getTraineeTrainingsList(String traineeUsername) {
        log.debug("Getting trainings by trainee username: {}", traineeUsername);

        List<TrainingEntity> trainings;
        Session session = sessionFactory.getCurrentSession();
        String hql = "from TrainingEntity t where t.trainee.user.username = :traineeUsername";
        trainings = session.createQuery(hql, TrainingEntity.class)
                .setParameter("traineeUsername", traineeUsername)
                .getResultList();

        log.debug("Successfully retrieved trainings by trainee username: {}", traineeUsername);
        return trainings;
    }
}
