package org.example.repository;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TrainingTypeRepo {
    private final SessionFactory sessionFactory;

    public TrainingTypeRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Get training type by name.
     */
    public TrainingTypeEntity getTrainingTypeByName(String name) {
        log.debug("Getting trainingType with name: {}", name);
        Session session = sessionFactory.getCurrentSession();
        String hql = "from TrainingTypeEntity t where t.trainingTypeName = :name";
        Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }


    /**
     * Get training type by id.
     */
    public Optional<TrainingTypeEntity> findById(Long id) {
        log.debug("Getting trainingType with id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        String hql = "from TrainingTypeEntity t where t.id = :id";
        Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.uniqueResult());
    }

    /** Returns all training types. */
    public List<TrainingTypeEntity> findAll() {
        log.debug("Getting all training types.");
        Session session = sessionFactory.getCurrentSession();
        String hql = "from TrainingTypeEntity";
        Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
        return query.getResultList();
    }
}
