package org.example.repository;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymDataAccessException;
import org.example.exceptions.GymDataUpdateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserEntity user = trainerEntity.getUser();
            session.persist(user);
            session.persist(trainerEntity);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw e;
        }
    }


    /**
     * Gets trainer by username.
     *
     * @param username username of the trainer
     * @return {@code Optional<TrainerEntity>}
     */
    public TrainerEntity getTrainerByUsername(String username) {
        log.debug("Getting trainer with username: {}", username);
        Session session = sessionFactory.openSession();
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
        Session session = sessionFactory.openSession();
        return session.get(TrainerEntity.class, id);
    }

    /**
     * Changes the password of the trainer by username.
     *
     * @param username username of the trainer
     */
    public void changeTrainerPassword(String username, String password) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            //            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            //
            //            CriteriaUpdate<UserEntity> criteriaUpdate =
            //                    criteriaBuilder.createCriteriaUpdate(UserEntity.class);
            //
            //            Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);
            //
            //            criteriaUpdate.set("password", password)
            //                    .where(criteriaBuilder.equal(root.get("username"), username));
            //
            //            session.createMutationQuery(criteriaUpdate).executeUpdate();

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("Exception while updating the password of the trainer: {} ", username);
            throw e;
        }
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            TrainerEntity trainer = session.get(TrainerEntity.class, id);

            if (trainer != null) {
                trainer.setSpecialization(trainerEntity.getSpecialization());
            }

            UserEntity user = session.get(UserEntity.class, trainerEntity.getUser().getId());
            if (user != null) {
                user.setFirstName(trainerEntity.getUser().getFirstName());
                user.setLastName(trainerEntity.getUser().getLastName());
                user.setUsername(trainerEntity.getUser().getUsername());
                user.setPassword(trainerEntity.getUser().getPassword());
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataUpdateException(String.format(
                    "Exception while updating trainer with id: %d", id));
        }
        log.debug("Updating trainer with id: {} with {}", id, trainerEntity);
    }

    /**
     * Activates trainer by id.
     *
     * @param id id of the trainer to activate
     */
    public void activateTrainer(Long id) {
        log.debug("Activating trainer with id: {}", id);

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            //
            //            CriteriaUpdate<UserEntity> criteriaUpdate =
            //                    criteriaBuilder.createCriteriaUpdate(UserEntity.class);
            //
            //            Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);
            //
            //            criteriaUpdate.set("isActive", true)
            //                    .where(criteriaBuilder.equal(root.get("id"), id));
            //
            //            session.createMutationQuery(criteriaUpdate).executeUpdate();

            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("Hibernate exception");
            log.error("Exception while activating trainer", exception);
            throw new GymDataUpdateException(
                    String.format("Exception while activating trainer with id %d", id));
        }

        log.debug("Successfully activated trainer with id {}", id);
    }

    /**
     * Deactivates trainer by id.
     *
     * @param id id of the trainer to deactivate
     */
    public void deactivateTrainer(Long id) {
        log.debug("Deactivating trainer with id: {}", id);

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            //
            //            CriteriaUpdate<UserEntity> criteriaUpdate =
            //                    criteriaBuilder.createCriteriaUpdate(UserEntity.class);
            //
            //            Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);
            //
            //            criteriaUpdate.set("isActive", false)
            //                    .where(criteriaBuilder.equal(root.get("id"), id));
            //
            //            session.createMutationQuery(criteriaUpdate).executeUpdate();

            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("Hibernate exception");
            log.error("Exception while deactivating trainer", exception);
            throw new GymDataUpdateException(
                    String.format("Exception while deactivating trainer with id %d", id));
        }

        log.debug("Successfully deactivated trainer with id {}", id);
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

        Session session = null;
        List<TrainingEntity> trainings = null;
        try {
            session = sessionFactory.openSession();
            //            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            //            CriteriaQuery<TrainingEntity> criteriaQuery
            //            = criteriaBuilder.createQuery(TrainingEntity.class);
            //
            //            Root<TrainingEntity> trainingRoot = criteriaQuery.from(TrainingEntity.class);
            //
            //            Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot
            //                    .join("trainer", JoinType.INNER)
            //                    .join("user", JoinType.INNER);
            //
            //            Join<TrainingEntity, TrainerEntity> traineeJoin = trainingRoot
            //                    .join("trainee", JoinType.INNER)
            //                    .join("user", JoinType.INNER);
            //
            //
            //            List<Predicate> predicates = new ArrayList<>();
            //
            //            predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
            //
            //            if (fromDate != null && toDate != null) {
            //                predicates.add(criteriaBuilder
            //                .between(trainingRoot.get("trainingDate"), fromDate, toDate));
            //            }
            //
            //            if (traineeUsername != null) {
            //                predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
            //            }
            //
            //            criteriaQuery.select(trainingRoot)
            //                    .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            //
            //            trainings = session.createQuery(criteriaQuery).getResultList();
        } catch (RuntimeException e) {
            log.debug("hibernate exception");
            throw new GymDataAccessException("Failed to retrieve trainer's trainings by given criteria.");
        } finally {
            assert session != null;
            session.close();
        }

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
        Session session = null;
        List<TrainerEntity> trainers = null;
        try {
            session = sessionFactory.openSession();

            //            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            //            CriteriaQuery<TrainerEntity> criteriaQuery = criteriaBuilder.createQuery(TrainerEntity.class);
            //
            //            Root<TrainerEntity> trainerRoot = criteriaQuery.from(TrainerEntity.class);
            //
            //            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            //            Root<TrainingEntity> trainingRoot = subquery.from(TrainingEntity.class);
            //
            //            Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot.join("trainee", JoinType.INNER)
            //                    .join("user", JoinType.INNER);
            //
            //            subquery.select(trainingRoot.get("trainer").get("id"))
            //                    .where(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
            //
            //            criteriaQuery.select(trainerRoot)
            //                    .where(criteriaBuilder.not(trainerRoot.get("id").in(subquery)));
            //
            //            trainers = session.createQuery(criteriaQuery).getResultList();
        } catch (RuntimeException e) {
            log.debug("hibernate exception");
            throw new GymDataAccessException(String
                    .format("Failed to retrieve trainers not assigned to trainee: %s",
                            traineeUsername));
        } finally {
            assert session != null;
            session.close();
        }

        log.debug("Successfully retrieved trainers not assigned to trainee: {}", traineeUsername);

        return trainers;
    }

}
