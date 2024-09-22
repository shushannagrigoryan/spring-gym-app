package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymDataAccessException;
import org.example.exceptions.GymDataUpdateException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeDao {
    private final SessionFactory sessionFactory;
    private final UserDao userDao;

    public TraineeDao(SessionFactory sessionFactory, UserDao userDao) {
        this.sessionFactory = sessionFactory;
        this.userDao = userDao;
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
            userDao.createUser(user);
            session.persist(traineeEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }
        log.debug("Saving trainee: {} to storage", traineeEntity);
    }

    /**
     * Gets trainee by username.
     *
     * @param username username of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public Optional<TraineeEntity> getTraineeByUsername(String username) {
        log.debug("Getting trainee with username: {}", username);

        TraineeEntity trainee;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM TraineeEntity t WHERE t.user.username = :username";
            Query<TraineeEntity> query = session.createQuery(hql, TraineeEntity.class);
            query.setParameter("username", username);
            trainee = query.uniqueResult();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException(String.format("Failed to retrieve trainee with username: %s", username));
        }
        return Optional.ofNullable(trainee);
    }

    /**
     * Gets trainee by id.
     *
     * @param id id of the trainee
     * @return {@code TraineeEntity}
     */
    public Optional<TraineeEntity> getTraineeById(Long id) {
        log.debug("Getting trainee with id: {}", id);

        TraineeEntity trainee = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainee = session.get(TraineeEntity.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }

        log.debug("Getting trainee with id: {}", id);

        return Optional.ofNullable(trainee);
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

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaUpdate<UserEntity> criteriaUpdate =
                    criteriaBuilder.createCriteriaUpdate(UserEntity.class);

            Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);

            criteriaUpdate.set("password", password)
                    .where(criteriaBuilder.equal(root.get("username"), username));

            session.createMutationQuery(criteriaUpdate).executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("Hibernate exception");
            throw new GymDataUpdateException(
                    String.format(
                            "Exception while updating password of the trainee with username %s",
                            username));
        }
        log.debug("Successfully updated password of the trainee with username {}", username);
    }

    /**
     * Activates trainee by id.
     *
     * @param id id of the trainee to activate
     */
    public void activateTrainee(Long id) {
        log.debug("Activating trainee with id: {}", id);

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaUpdate<UserEntity> criteriaUpdate =
                    criteriaBuilder.createCriteriaUpdate(UserEntity.class);

            Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);

            criteriaUpdate.set("isActive", true)
                    .where(criteriaBuilder.equal(root.get("id"), id));

            session.createMutationQuery(criteriaUpdate).executeUpdate();

            transaction.commit();
        } catch (HibernateException exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("Hibernate exception");
            log.error("Exception while activating trainee", exception);
            throw new GymDataUpdateException(
                    String.format("Exception while activating trainee with id %d", id));
        }

        log.debug("Successfully activated trainee with id {}", id);
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
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaUpdate<UserEntity> criteriaUpdate =
                    criteriaBuilder.createCriteriaUpdate(UserEntity.class);

            Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);

            criteriaUpdate.set("isActive", false)
                    .where(criteriaBuilder.equal(root.get("id"), id));

            session.createMutationQuery(criteriaUpdate).executeUpdate();

            transaction.commit();
        } catch (HibernateException exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("Hibernate exception");
            log.error("Exception while deactivating trainee", exception);
            throw new GymDataUpdateException(
                    String.format("Exception while deactivating trainee with id %d", id));
        }

        log.debug("Successfully deactivated trainee with id {}", id);
    }

    //
    //    /**
    //     * Deletes the trainee entity from the storage by id.
    //     * If no trainee is found throws an {@code IllegalIdException}.
    //     *
    //     * @param id id of the trainee to be deleted
    //     */
    //    public void deleteTraineeById(Long id) {
    //        if (dataStorage.getTraineeStorage().containsKey(id)) {
    //            log.debug("Deleting trainee with id: {} from storage", id);
    //            dataStorage.getTraineeStorage().remove(id);
    //        } else {
    //            log.debug("No trainee with id: {}", id);
    //            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
    //        }
    //    }
    //

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
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
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
        //        if (dataStorage.getTraineeStorage().containsKey(id)) {
        //            log.debug("Deleting trainee with id: {} from storage", id);
        //            dataStorage.getTraineeStorage().remove(id);
        //        } else {
        //            log.debug("No trainee with id: {}", id);
        //            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        //        }

        log.debug("Deleting trainee with username: {}", username);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            //delete trainee
            String hqlDeleteTrainee = "DELETE from TraineeEntity t WHERE t.user.username = :username";
            session.createMutationQuery(hqlDeleteTrainee)
                    .setParameter("username", username)
                    .executeUpdate();

            //delete corresponding user
            String hqlDeleteUser = "DELETE from UserEntity u WHERE u.username = :username";
            session.createMutationQuery(hqlDeleteUser)
                    .setParameter("username", username)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException(String.format("Failed to retrieve trainee with username: %s", username));
        }

        log.debug("Successfully deleted trainee with username: {}", username);
    }

    /**
     * Returns trainees trainings list by trainee username and given criteria.
     *
     * @param traineeUsername username of the trainee
     * @param fromDate training fromDate
     * @param toDate training toDate
     * @param trainingTypeId training type
     * @param trainerUsername trainer username
     * @return {@code List<TrainingEntity>}
     */

    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
                                                            LocalDate toDate, Long trainingTypeId,
                                                            String trainerUsername) {

        Session session = null;
        Transaction transaction = null;
        List<TrainingEntity> trainings;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<TrainingEntity> criteriaQuery = criteriaBuilder.createQuery(TrainingEntity.class);

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
                predicates.add(criteriaBuilder.between(trainingRoot.get("trainingDate"), fromDate, toDate));
            }

            if (trainingTypeId != null) {
                predicates.add(criteriaBuilder.equal(trainingRoot.get("trainingType").get("id"), trainingTypeId));
            }

            if (trainerUsername != null) {
                predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerUsername));
            }

            criteriaQuery.select(trainingRoot)
                    .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            trainings = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException("Failed to retrieve trainee's trainings by given criteria.");
        } finally {
            assert session != null;
            session.close();
        }

        log.debug("Successfully retrieved trainee's trainings by given criteria");

        return trainings;
    }

}
