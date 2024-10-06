package org.example.repository;

import java.util.Optional;
import org.example.entity.TraineeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends CrudRepository<TraineeEntity, Long> {
    Optional<TraineeEntity> findByUser_Username(String username);


    //    @Query("from TraineeEntity t join UserEntity u on t.user = u where u.username =:username")
    //    Optional<TraineeEntity> getByUsername(String username);








    //    /**
    //     * Adding trainee to database.
    //     *
    //     * @param traineeEntity {@code TraineeEntity} to be added to storage
    //     */
    //    public void createTrainee(TraineeEntity traineeEntity) {
    //        Session session = sessionFactory.getCurrentSession();
    //        UserEntity user = traineeEntity.getUser();
    //        session.persist(user);
    //        session.persist(traineeEntity);
    //    }
    //
    //    /**
    //     * Gets trainee by username.
    //     *
    //     * @param username username of the trainee
    //     * @return {@code Optional<TraineeEntity>}
    //     */
    //    public TraineeEntity getTraineeByUsername(String username) {
    //        log.debug("Getting trainee with username: {}", username);
    //        TraineeEntity trainee;
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from TraineeEntity t where t.user.username = :username";
    //        Query<TraineeEntity> query = session.createQuery(hql, TraineeEntity.class);
    //        query.setParameter("username", username);
    //        trainee = query.uniqueResult();
    //        return trainee;
    //    }
    //
    //    /**
    //     * Gets trainee by id.
    //     *
    //     * @param id id of the trainee
    //     * @return {@code TraineeEntity}
    //     */
    //    public TraineeEntity getTraineeById(Long id) {
    //        log.debug("Getting trainee with id: {}", id);
    //        Session session = sessionFactory.getCurrentSession();
    //        return session.get(TraineeEntity.class, id);
    //    }
    //
    //    /**
    //     * Changes the password of the trainee by username.
    //     *
    //     * @param username username of the trainee
    //     */
    //    public void changeTraineePassword(String username, String password) {
    //        Session session = sessionFactory.getCurrentSession();
    //
    //        String hql = "from TraineeEntity t where t.user.username =:username";
    //        TraineeEntity trainee = session.createQuery(hql, TraineeEntity.class)
    //                .setParameter("username", username)
    //                .uniqueResult();
    //        trainee.getUser().setPassword(password);
    //        session.merge(trainee);
    //
    //        log.debug("Successfully updated password of the trainee with username {}", username);
    //    }
    //
    //    /**
    //     * Activates trainee.
    //     *
    //     * @param trainee trainee to activate
    //     */
    //    public void activateTrainee(TraineeEntity trainee) {
    //        log.debug("Activating trainee :{}", trainee);
    //        Session session = sessionFactory.getCurrentSession();
    //        trainee.getUser().setActive(true);
    //        session.merge(trainee);
    //        log.debug("Successfully activated trainee: {}", trainee);
    //    }
    //
    //    /**
    //     * Deactivates trainee.
    //     *
    //     * @param trainee trainee to deactivate
    //     */
    //    public void deactivateTrainee(TraineeEntity trainee) {
    //        log.debug("Deactivating trainee :{}", trainee);
    //        Session session = sessionFactory.getCurrentSession();
    //        trainee.getUser().setActive(false);
    //        session.merge(trainee);
    //        log.debug("Successfully deactivated trainee: {}", trainee);
    //    }
    //
    //    /**
    //     * Updates trainee entity in storage by id.
    //     * If no trainee is found throws an {@code IllegalIdException}
    //     *
    //     * @param id            id of the trainee to be updated
    //     * @param traineeEntity new {@code TraineeEntity} to update with
    //     */
    //    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
    //        Session session = sessionFactory.getCurrentSession();
    //        session.merge(traineeEntity);
    //        log.debug("Updating trainee with id: {} with {}", id, traineeEntity);
    //    }
    //
    //
    //    /**
    //     * Deletes the trainee by username.
    //     * If no trainee is found throws an {@code GymIllegalUsernameException}.
    //     *
    //     * @param username username of the trainee to be deleted
    //     */
    //    public void deleteTraineeByUsername(String username) {
    //        log.debug("Deleting trainee with username: {}", username);
    //
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from TraineeEntity t where t.user.username =:username";
    //        TraineeEntity trainee = session
    //                .createQuery(hql, TraineeEntity.class)
    //                .setParameter("username", username)
    //                .uniqueResult();
    //        session.remove(trainee.getUser());
    //        session.remove(trainee);
    //        log.debug("Successfully deleted trainee with username: {}", username);
    //    }


}
