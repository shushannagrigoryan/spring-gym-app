package org.example.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserEntity user = traineeEntity.getUser();
            session.persist(user);
            session.persist(traineeEntity);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("hibernate exception while creating trainee: {} ", traineeEntity);
            throw e;
        }
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
        Session session = sessionFactory.openSession();
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
            log.error("Exception while updating password of trainee: {}", username);
            throw e;
        }
        log.debug("Successfully updated password of the trainee with username {}", username);
    }

    /**
     * Activates trainee by id.
     *
     * @param trainee trainee to activate
     */
    public void activateTrainee(TraineeEntity trainee) {
        Session session = sessionFactory.getCurrentSession();
        trainee.getUser().setActive(true);
        session.merge(trainee);
        log.debug("Successfully activated trainee: {}", trainee);
    }

    /**
     * Deactivates trainee by id.
     *
     * @param id id of the trainee to deactivate
     */
    public void deactivateTrainee(Long id) {
        log.debug("Deactivating trainee with id: {}", id);

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
            log.error("Exception while deactivating trainee", exception);
            throw new GymDataUpdateException(
                    String.format("Exception while deactivating trainee with id %d", id));
        }

        log.debug("Successfully deactivated trainee with id {}", id);
    }

    /**
     * Updates trainee entity in storage by id.
     * If no trainee is found throws an {@code IllegalIdException}
     *
     * @param id            id of the trainee to be updated
     * @param traineeEntity new {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            TraineeEntity trainee = session.get(TraineeEntity.class, id);

            if (trainee != null) {
                trainee.setAddress(traineeEntity.getAddress());
                trainee.setDateOfBirth(traineeEntity.getDateOfBirth());
            }

            UserEntity user = session.get(UserEntity.class, traineeEntity.getUser().getId());
            if (user != null) {
                user.setFirstName(traineeEntity.getUser().getFirstName());
                user.setLastName(traineeEntity.getUser().getLastName());
                user.setUsername(traineeEntity.getUser().getUsername());
                user.setPassword(traineeEntity.getUser().getPassword());
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("hibernate exception");
            throw new GymDataUpdateException(String.format(
                    "Exception while updating trainee with id: %d", id));
        }
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            //delete trainee
            String hqlDeleteTrainee = "DELETE from TraineeEntity t WHERE t.user.username = :username";
            //            session.createMutationQuery(hqlDeleteTrainee)
            //                    .setParameter("username", username)
            //                    .executeUpdate();
            //
            //            //delete corresponding user
            //            String hqlDeleteUser = "DELETE from UserEntity u WHERE u.username = :username";
            //            session.createMutationQuery(hqlDeleteUser)
            //                    .setParameter("username", username)
            //                    .executeUpdate();

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("hibernate exception");
            throw new GymDataAccessException(String.format("Failed to retrieve trainee with username: %s", username));
        }

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
            //            Join<TrainingEntity, TraineeEntity> traineeJoin = trainingRoot
            //                    .join("trainee", JoinType.INNER)
            //                    .join("user", JoinType.INNER);
            //
            //            Join<TrainingEntity, TrainerEntity> trainerJoin = trainingRoot
            //                    .join("trainer", JoinType.INNER)
            //                    .join("user", JoinType.INNER);
            //
            //
            //            List<Predicate> predicates = new ArrayList<>();
            //
            //            predicates.add(criteriaBuilder.equal(traineeJoin.get("username"), traineeUsername));
            //
            //            if (fromDate != null && toDate != null) {
            //                predicates.add(criteriaBuilder
            //                .between(trainingRoot.get("trainingDate"), fromDate, toDate));
            //            }
            //
            //            if (trainingTypeId != null) {
            //                predicates.add(criteriaBuilder
            //                .equal(trainingRoot.get("trainingType").get("id"), trainingTypeId));
            //            }
            //
            //            if (trainerUsername != null) {
            //                predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
            //            }
            //
            //            criteriaQuery.select(trainingRoot)
            //                    .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            //
            //            trainings = session.createQuery(criteriaQuery).getResultList();
        } catch (RuntimeException e) {
            log.error("hibernate exception");
            throw new GymDataAccessException("Failed to retrieve trainee's trainings by given criteria.");
        } finally {
            assert session != null;
            session.close();
        }

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

    /**
     * Updates trainee's training list.
     *
     * @param traineeUsername          trainee username
     * @param trainingsUpdatedTrainers map with key: training id to update, value: the new trainer
     */
    public void updateTraineesTrainersList(String traineeUsername, Map<Long, TrainerEntity> trainingsUpdatedTrainers) {
        List<TrainingEntity> traineeTrainings = getTraineeTrainingsList(traineeUsername);

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            for (TrainingEntity training : traineeTrainings) {
                Long trainingId = training.getId();

                if (trainingsUpdatedTrainers.containsKey(trainingId)) {
                    TrainingEntity trainingToUpdate = session.get(TrainingEntity.class, trainingId);

                    trainingToUpdate.setTrainer(trainingsUpdatedTrainers.get(trainingId));
                    session.merge(trainingToUpdate);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("hibernate exception");
            throw new GymDataUpdateException(String.format(
                    "Failed to update trainersList by trainee username: %s", traineeUsername));
        }
        log.debug("Successfully updated trainersList by trainee username: {}", traineeUsername);
    }

}
