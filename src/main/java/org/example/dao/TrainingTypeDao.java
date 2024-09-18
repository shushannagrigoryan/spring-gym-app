package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
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
     * Adding training type to database.
     *
     * @param trainingTypeEntity {@code TrainingTypeEntity} to be added to storage
     */
    public void createTrainingType(TrainingTypeEntity trainingTypeEntity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(trainingTypeEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }
        log.debug("Saving training type: {} to storage", trainingTypeEntity);
    }

    /**  Get training type by name.*/
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



}
