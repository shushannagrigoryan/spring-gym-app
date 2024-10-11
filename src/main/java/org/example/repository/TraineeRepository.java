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
