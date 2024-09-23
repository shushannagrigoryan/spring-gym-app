package org.example.dao;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymDataAccessException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingDao {
    private final SessionFactory sessionFactory;

    public TrainingDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Generates id for the training entity and adds that entity to the storage map.
     *
     * @param trainingEntity {@code TrainingEntity} to be added to storage
     */
    public void createTraining(TrainingEntity trainingEntity) {
        log.debug("Saving training: {} to storage", trainingEntity);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(trainingEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw e;
        }
    }


    /**
     * Gets training by id.
     *
     * @param id id of the training
     * @return {@code Optional<TrainingEntity>}
     */
    public Optional<TrainingEntity> getTrainingById(Long id) {
        log.debug("Getting training with id: {}", id);

        TrainingEntity training;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            training = session.get(TrainingEntity.class, id);
            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException(String.format(
                    "Failed to retrieve training with id: %d", id));
        }
        return Optional.ofNullable(training);
    }


    /**
     * Gets all trainings.
     *
     * @return {@code Optional<TrainingEntity>}
     */
    public List<TrainingEntity> getAllTrainings() {
        log.debug("Getting all trainings.");

        List<TrainingEntity> trainings;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM TrainingEntity";
            Query<TrainingEntity> query = session.createQuery(hql, TrainingEntity.class);
            trainings = query.getResultList();

        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException("Failed to retrieve all trainings");
        }
        return trainings;
    }

}
