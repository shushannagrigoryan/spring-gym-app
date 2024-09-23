package org.example.dao;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymDataAccessException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
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
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM TrainingTypeEntity t WHERE t.trainingTypeName = :name";
            Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
            query.setParameter("name", name);
            trainingType = query.uniqueResult();
        } catch (HibernateException e) {
            log.debug("hibernate exception");
        }
        return trainingType;
    }


    /**
     * Get training type by id.
     */
    public Optional<TrainingTypeEntity> getTrainingTypeById(Long id) {
        log.debug("Getting trainingType with id: {}", id);

        TrainingTypeEntity trainingType;
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM TrainingTypeEntity t WHERE t.id = :id";
            Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
            query.setParameter("id", id);
            trainingType = query.uniqueResult();
        } catch (HibernateException e) {
            log.debug("hibernate exception");
            throw new GymDataAccessException(String.format(
                    "Exception while getting training type by id: %d", id));
        }
        return Optional.ofNullable(trainingType);
    }


}
