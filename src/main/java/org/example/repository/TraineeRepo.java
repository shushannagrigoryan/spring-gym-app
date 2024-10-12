package org.example.repository;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TraineeRepo {
    private final SessionFactory sessionFactory;


    public TraineeRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /**
     * Adding trainee to database.
     *
     * @param traineeEntity {@code TraineeEntity} to be added to storage
     */
    public TraineeEntity save(TraineeEntity traineeEntity) {
        log.debug("Saving trainee.");
        Session session = sessionFactory.getCurrentSession();
        return session.merge(traineeEntity);
    }

    /**
     * Gets trainee by username.
     *
     * @param username username of the trainee
     * @return {@code Optional<TraineeEntity>}
     */
    public Optional<TraineeEntity> findByUsername(String username) {
        log.debug("Getting trainee with username: {}", username);
        TraineeEntity trainee;
        Session session = sessionFactory.getCurrentSession();
        String hql = "from TraineeEntity t where t.user.username = :username";
        Query<TraineeEntity> query = session.createQuery(hql, TraineeEntity.class);
        query.setParameter("username", username);
        trainee = query.uniqueResult();
        return Optional.ofNullable(trainee);
    }

    /**
     * Gets trainee by id.
     *
     * @param id id of the trainee
     * @return {@code TraineeEntity}
     */
    public Optional<TraineeEntity> findById(Long id) {
        log.debug("Getting trainee with id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(TraineeEntity.class, id));
    }

    /**
     * Changes the password of the trainee by username.
     *
     * @param username username of the trainee
     */
    public void changeTraineePassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from TraineeEntity t where t.user.username =:username";
        TraineeEntity trainee = session.createQuery(hql, TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        trainee.getUser().setPassword(password);
        session.merge(trainee);

        log.debug("Successfully updated password of the trainee with username {}", username);
    }

    /**
     * Activates trainee.
     *
     * @param trainee trainee to activate
     */
    public void activateTrainee(TraineeEntity trainee) {
        log.debug("Activating trainee :{}", trainee);
        Session session = sessionFactory.getCurrentSession();
        trainee.getUser().setActive(true);
        session.merge(trainee);
        log.debug("Successfully activated trainee: {}", trainee);
    }

    /**
     * Deactivates trainee.
     *
     * @param trainee trainee to deactivate
     */
    public void deactivateTrainee(TraineeEntity trainee) {
        log.debug("Deactivating trainee :{}", trainee);
        Session session = sessionFactory.getCurrentSession();
        trainee.getUser().setActive(false);
        session.merge(trainee);
        log.debug("Successfully deactivated trainee: {}", trainee);
    }

    /**
     * Updates trainee entity in storage by id.
     * If no trainee is found throws an {@code IllegalIdException}
     *
     * @param id            id of the trainee to be updated
     * @param traineeEntity new {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(traineeEntity);
        log.debug("Updating trainee with id: {} with {}", id, traineeEntity);
    }


    /**
     * Deletes the trainee by username.
     * If no trainee is found throws an {@code GymIllegalUsernameException}.
     *
     * @param username username of the trainee to be deleted
     */
    public void deleteByUsername(String username) {
        log.debug("Deleting trainee with username: {}", username);

        Session session = sessionFactory.getCurrentSession();
        String hql = "from TraineeEntity t where t.user.username =:username";
        TraineeEntity trainee = session
                .createQuery(hql, TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        session.remove(trainee.getUser());
        session.remove(trainee);
        log.debug("Successfully deleted trainee with username: {}", username);
    }

    /**
     * Deletes the trainee by id.
     * If no trainee is found throws an {@code GymIllegalUsernameException}.
     *
     * @param id id of the trainee to be deleted
     */
    public void deleteById(Long id) {
        log.debug("Deleting trainee with id: {}", id);

        Session session = sessionFactory.getCurrentSession();
        String hql = "from TraineeEntity t where t.id =:id";
        TraineeEntity trainee = session
                .createQuery(hql, TraineeEntity.class)
                .setParameter("id", id)
                .uniqueResult();
        session.remove(trainee.getUser());
        session.remove(trainee);
        log.debug("Successfully deleted trainee with id: {}", id);
    }

}
