package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("select u.username from UserEntity u")
    List<String> getAllUsernames();

    //    /**
    //     * Get all usernames(both trainer and trainee).
    //     */
    //    public List<String> getAllUsernames() {
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "select t.username from UserEntity t";
    //        Query<String> query = session.createQuery(hql, String.class);
    //        return query.getResultList();
    //    }
    //
    //
    //    /**
    //     * Getting user by username.
    //     *
    //     * @param username username of the user
    //     * @return {@code UserEntity} if user exists, else null.
    //     */
    //    public UserEntity getUserByUsername(String username) {
    //        log.debug("Getting user with username: {}", username);
    //        Session session = sessionFactory.getCurrentSession();
    //        String hql = "from UserEntity u where u.username = :username";
    //        Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
    //        query.setParameter("username", username);
    //        return query.uniqueResult();
    //    }

}
