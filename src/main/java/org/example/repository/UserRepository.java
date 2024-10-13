package org.example.repository;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /** Save user. */
    public UserEntity save(UserEntity user) {
        log.debug("Saving  user : {}", user);
        Session session = sessionFactory.getCurrentSession();
        UserEntity user1 = session.merge(user);
        session.flush();
        return user1;
    }

    /**
     * Get all usernames(both trainer and trainee).
     */
    public List<String> getAllUsernames() {
        log.debug("Getting All Usernames.");
        Session session = sessionFactory.getCurrentSession();
        String hql = "select t.username from UserEntity t";
        Query<String> query = session.createQuery(hql, String.class);
        return query.getResultList();
    }


    /**
     * Getting user by username.
     *
     * @param username username of the user
     * @return {@code UserEntity} if user exists, else null.
     */
    public Optional<UserEntity> getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);
        Session session = sessionFactory.getCurrentSession();
        String hql = "from UserEntity u where u.username = :username";
        Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
        query.setParameter("username", username);
        return Optional.ofNullable(query.uniqueResult());
    }

    /**
     * Getting user by username and password.
     *
     * @param username username of the user
     * @param password password of the user
     * @return {@code UserEntity} if user exists, else null.
     */
    public Optional<UserEntity> getUserByUsernameAndPassword(String username, String password) {
        log.debug("Getting user with username: {} and password: {}", username, password);
        Session session = sessionFactory.getCurrentSession();
        String hql = "from UserEntity u where u.username = :username and u.password= :password";
        Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return Optional.ofNullable(query.uniqueResult());
    }

    /**
     * Updates user password.
     *
     * @param username username of the user
     * @param password password of the user
     */
    public void updatePassword(String username, String password) {
        log.debug("Updating user password: {}", password);
        Session session = sessionFactory.getCurrentSession();
        String hql = "update UserEntity u set u.password =:password where u.username = :username";
        Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        query.executeUpdate();
    }

    /** Deletes user by id.
     *
     * @param id id of the user
     */
    public void deleteById(Long id) {
        log.debug("Deleting user with id: {}", id);

        Session session = sessionFactory.getCurrentSession();
        UserEntity user = session.get(UserEntity.class, id);
        session.remove(user);
        log.debug("Successfully deleted user with id: {}", id);
    }
}
