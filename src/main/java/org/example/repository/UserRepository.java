package org.example.repository;

import java.util.List;
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

    /**
     * Get all usernames(both trainer and trainee).
     */
    public List<String> getAllUsernames() {
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
    public UserEntity getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);
        Session session = sessionFactory.getCurrentSession();
        String hql = "from UserEntity u where u.username = :username";
        Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

}
