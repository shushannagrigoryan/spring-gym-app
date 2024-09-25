package org.example.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TrainingRepository {
    private final SessionFactory sessionFactory;

    public TrainingRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Generates id for the training entity and adds that entity to the storage map.
     *
     * @param trainingEntity {@code TrainingEntity} to be added to storage
     */
    public void createTraining(TrainingEntity trainingEntity) {
        log.debug("Saving training: {} to storage", trainingEntity);
        Session session = sessionFactory.getCurrentSession();
        session.persist(trainingEntity);
    }


    /**
     * Gets training by id.
     *
     * @param id id of the training
     * @return {@code Optional<TrainingEntity>}
     */
    public TrainingEntity getTrainingById(Long id) {
        log.debug("Getting training with id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return session.get(TrainingEntity.class, id);
    }


    /**
     * Gets all trainings.
     *
     * @return {@code Optional<TrainingEntity>}
     */
    public List<TrainingEntity> getAllTrainings() {
        log.debug("Getting all trainings.");
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM TrainingEntity";
        Query<TrainingEntity> query = session.createQuery(hql, TrainingEntity.class);
        return query.getResultList();
    }

    /**Updating given training.
     *
     * @param training new training data
     */

    public void updateTraining(TrainingEntity training) {
        log.debug("Request to update training: {}", training);
        Session session = sessionFactory.getCurrentSession();
        session.merge(training);
    }
}
