package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymDataAccessException;
import org.example.exceptions.GymDataUpdateException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerDao {
    private final SessionFactory sessionFactory;
    private final UserDao userDao;
    private final TrainingTypeDao trainingTypeDao;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerDao(SessionFactory sessionFactory,
                      UserDao userDao,
                      TrainingTypeDao trainingTypeDao) {
        this.sessionFactory = sessionFactory;
        this.userDao = userDao;
        this.trainingTypeDao = trainingTypeDao;
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
            userDao.createUser(user);

            //            Optional<TrainingTypeEntity> trainingType =
            //                    trainingTypeDao.getTrainingTypeById(trainerEntity.getSpecialization().getId());
            //trainingType.ifPresent(trainerEntity::setSpecialization);

            session.persist(trainerEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }

        log.debug("Saving trainer: {} to storage", trainerEntity);
    }


    /**
     * Gets trainer by username.
     *
     * @param username username of the trainer
     * @return {@code Optional<TrainerEntity>}
     */
    public Optional<TrainerEntity> getTrainerByUsername(String username) {
        log.debug("Getting trainer with username: {}", username);

        TrainerEntity trainer = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM TrainerEntity t WHERE t.user.username = :username";
            Query<TrainerEntity> query = session.createQuery(hql, TrainerEntity.class);
            query.setParameter("username", username);
            trainer = query.uniqueResult();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException(String.format("Failed to retrieve trainer with username: %s", username));
        }
        return Optional.ofNullable(trainer);
    }

    /**
     * Gets trainer by id.
     *
     * @param id id of the trainer
     * @return {@code TrainerEntity}
     */
    public Optional<TrainerEntity> getTrainerById(Long id) {
        log.debug("Getting trainer with id: {}", id);
        TrainerEntity trainer = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            trainer = session.get(TrainerEntity.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }

        log.debug("Getting trainer with id: {}", id);

        return Optional.ofNullable(trainer);
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
                    String.format("Exception while updating password of the trainer with username %S", username));
        }
        log.debug("Successfully updated password of the trainer with username {}", username);
    }

    //    /**
    //     * Updates trainer entity in storage by id.
    //     * If no trainer is found throws an {@code IllegalIdException}
    //     *
    //     * @param id id of the trainer to be updated
    //     * @param trainerEntity new {@code TrainerEntity} to update with
    //     */
    //    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
    //        if (!dataStorage.getTrainerStorage().containsKey(id)) {
    //            log.debug("No trainer with id: {}", id);
    //            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
    //        }
    //
    //        log.debug("Updating trainer with id: {} with {}", id, trainerEntity);
    //        dataStorage.getTrainerStorage().put(id, trainerEntity);
    //        dataStorage.getTrainerStorageUsernameKey().put(trainerEntity.getUsername(), trainerEntity);
    //    }

}
