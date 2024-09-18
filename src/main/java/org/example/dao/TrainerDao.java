package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
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

    /**Injecting dependencies using constructor. */
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
            TrainingTypeEntity trainingTypeEntity = trainerEntity.getSpecialization();

            TrainingTypeEntity trainingType =
                trainingTypeDao.getTrainingTypeByName(trainingTypeEntity.getTrainingTypeName());
            if (trainingType == null) {
                trainingTypeDao.createTrainingType(trainingTypeEntity);
            } else {
                trainerEntity.setSpecialization(trainingType);
            }

            userDao.createUser(user);
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
     * @return {@code TrainerEntity}
     */
    public TrainerEntity getTrainerByUsername(String username) {
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
        }
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

        return trainer;
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
