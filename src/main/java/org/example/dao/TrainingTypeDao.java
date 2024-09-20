package org.example.dao;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymDataAccessException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingTypeDao {
    private final SessionFactory sessionFactory;

    public TrainingTypeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Get training type by name.
     */
    public TrainingTypeEntity getTrainingTypeByName(String name) {
        log.debug("Getting trainingType with name: {}", name);

        TrainingTypeEntity trainingType = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM TrainingTypeEntity t WHERE t.trainingTypeName = :name";
            Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
            query.setParameter("name", name);
            trainingType = query.uniqueResult();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }
        return trainingType;
    }


    /**
     * Get training type by id.
     */
    public Optional<TrainingTypeEntity> getTrainingTypeById(Long id) {
        log.debug("Getting trainingType with id: {}", id);

        TrainingTypeEntity trainingType = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM TrainingTypeEntity t WHERE t.id = :id";
            Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
            query.setParameter("id", id);
            trainingType = query.uniqueResult();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
            throw new GymDataAccessException(String.format(
                    "Exception while getting training type by id: %d", id));
        }
        return Optional.ofNullable(trainingType);
    }


}
