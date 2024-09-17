package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDao {
    private final SessionFactory sessionFactory;
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createUser(UserEntity userEntity) {
        System.out.println("userEntity = " + userEntity);

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

    //    /**
    //     * Generates username for the user (firstName.lastName)
    //     * Adds suffix if the username is taken either by a trainee or a trainer.
    //     *
    //     * @param firstName firstName of the user
    //     * @param lastName lastName of the user
    //     * @return the username
    //     */
    //    public String generateUsername(String firstName, String lastName) {
    //        log.debug("Generating username for firstName: {}, lastName: {}", firstName, lastName);
    //
    //        String username = firstName.concat(".").concat(lastName);
    //
    //        TraineeDto trainee = traineeService.getTraineeByUsername(username);
    //        TrainerDto trainer = trainerService.getTrainerByUsername(username);
    //
    //        if ((trainee != null) || (trainer != null)) {
    //            log.debug("Username taken.");
    //            return username + getSuffix(username);
    //        }
    //        return username;
    //    }
    //
    //
    //    /**
    //     * Generates suffix for the given username which is not present both in trainee and trainer maps.
    //     *
    //     * @param username username for which suffix is generated
    //     * @return the suffix
    //     */
    //    private Long getSuffix(String username) {
    //        log.debug("Generating suffix for username: {}", username);
    //
    //        Long maxTraineeSuffix = dataStorage.getTraineeStorage().values()
    //                .stream()
    //                .map(TraineeEntity::getUsername)
    //                .filter(u -> u.startsWith(username))
    //                .map(key -> key.substring(username.length()))
    //                .filter(s -> !s.isEmpty()).map(Long::valueOf)
    //                .max(Long::compareTo).orElse(-1L);
    //
    //        Long maxTrainerSuffix = dataStorage.getTrainerStorage().values()
    //                .stream()
    //                .map(TrainerEntity::getUsername)
    //                .filter(u -> u.startsWith(username))
    //                .map(key -> key.substring(username.length()))
    //                .filter(s -> !s.isEmpty())
    //                .map(Long::valueOf)
    //                .max(Long::compareTo).orElse(-1L);
    //
    //        long suffix = maxTraineeSuffix > maxTrainerSuffix ? maxTraineeSuffix : maxTrainerSuffix;
    //
    //        return suffix + 1;
    //    }
    //
    //    /** Generating random 10 length password. */
    //    public String generatePassword() {
    //        log.debug("Generating a random password.");
    //        StringBuilder stringBuilder = new StringBuilder();
    //        SecureRandom secureRandom = new SecureRandom();
    //        for (int i = 0; i < 10; i++) {
    //            stringBuilder.append((char) secureRandom.nextInt(32, 127));
    //        }
    //        return stringBuilder.toString();
    //    }
}
