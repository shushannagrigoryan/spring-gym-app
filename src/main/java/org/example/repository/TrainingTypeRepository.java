package org.example.repository;

import org.example.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, Long> {
    //    private final SessionFactory sessionFactory;
    //
    //    public TrainingTypeRepository(SessionFactory sessionFactory) {
    //        this.sessionFactory = sessionFactory;
    //    }
    //
    //
    //    /**
    //     * Get training type by name.
    //     */
    //    public TrainingTypeEntity getTrainingTypeByName(String name) {
    //        log.debug("Getting trainingType with name: {}", name);
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from TrainingTypeEntity t where t.trainingTypeName = :name";
    //        Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
    //        query.setParameter("name", name);
    //        return query.uniqueResult();
    //    }
    //
    //
    //    /**
    //     * Get training type by id.
    //     */
    //    public TrainingTypeEntity getTrainingTypeById(Long id) {
    //        log.debug("Getting trainingType with id: {}", id);
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from TrainingTypeEntity t where t.id = :id";
    //        Query<TrainingTypeEntity> query = session.createQuery(hql, TrainingTypeEntity.class);
    //        query.setParameter("id", id);
    //        return query.uniqueResult();
    //    }


}
