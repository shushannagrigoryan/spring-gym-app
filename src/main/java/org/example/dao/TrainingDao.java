package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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


    //    /**
    //     * Gets training by id.
    //     *
    //     * @param id id of the training
    //     * @return {@code Optional<TrainingEntity>}
    //     */
    //    public Optional<TrainingEntity> getTrainingById(Long id) {
    //        log.debug("Getting training with id: {}", id);
    //        return Optional.ofNullable(dataStorage.getTrainingStorage().get(id));
    //    }
}
