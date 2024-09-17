package org.example.dao;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
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
     * Generates id for the trainee entity and adds that entity to the storage map.
     *
     * @param traineeEntity {@code TraineeEntity} to be added to storage
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        System.out.println("traineeEntity = " + traineeEntity);
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
    public TraineeEntity getTraineeByUsername(String username) {
        log.debug("Getting trainee with username: {}", username);

        TraineeEntity trainee = null;
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            String hql = "FROM TraineeEntity t WHERE t.user.username = :username";
            Query<TraineeEntity> query = session.createQuery(hql, TraineeEntity.class);
            query.setParameter("username", username);
            trainee = query.uniqueResult();

        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }
        return trainee;
    }

    /**
     * Gets trainee by id.
     *
     * @param id id of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public TraineeEntity getTraineeById(Long id) {
        log.debug("Getting trainee with id: {}", id);
        Session session = sessionFactory.openSession();
        TraineeEntity trainee = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            trainee = session.get(TraineeEntity.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        } finally {
            session.close();
        }

        log.debug("Getting trainee with id: {}", id);

        return trainee;
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
    //    /**
    //     * Updates trainee entity in storage by id.
    //     * If no trainee is found throws an {@code IllegalIdException}
    //     *
    //     * @param id id of the trainee to be updated
    //     * @param traineeEntity new {@code TraineeEntity} to update with
    //     */
    //    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
    //        if (!dataStorage.getTraineeStorage().containsKey(id)) {
    //            log.debug("No trainee with id: {}", id);
    //            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
    //        }
    //
    //        log.debug("Updating trainee with id: {} with {}", id, traineeEntity);
    //        dataStorage.getTraineeStorage().put(id, traineeEntity);
    //        dataStorage.getTraineeStorageUsernameKey().put(traineeEntity.getUsername(), traineeEntity);
    //    }
}
