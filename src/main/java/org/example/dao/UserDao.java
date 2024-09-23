package org.example.dao;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymDataAccessException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDao {
    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * creates userEntity.
     */
    public void createUser(UserEntity userEntity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(userEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.debug("hibernate exception");
        }
        log.debug("Saving user: {} to storage", userEntity);
    }

    /**
     * Get all usernames(both trainer and trainee).
     */
    public List<String> getAllUsernames() {
        List<String> allUsernames = null;
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT t.username FROM UserEntity t";
            Query<String> query = session.createQuery(hql, String.class);
            allUsernames = query.getResultList();
        } catch (HibernateException e) {
            log.debug("hibernate exception");
        }
        return allUsernames;
    }


    /**
     * Getting user by username.
     *
     * @param username username of the user
     * @return {@code UserEntity} if user exists, else null.
     */
    public Optional<UserEntity> getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);

        Optional<UserEntity> user;
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM UserEntity u WHERE u.username = :username";
            Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
            query.setParameter("username", username);
            user = Optional.ofNullable(query.uniqueResult());

        } catch (HibernateException e) {
            log.debug("hibernate exception");
            throw new GymDataAccessException(
                    String.format("Failed to retrieve user with username: %s", username));
        }
        return user;
    }

}
