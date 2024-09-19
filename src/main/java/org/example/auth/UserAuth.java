package org.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDao;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserAuth {
    private final UserDao userDao;

    /**
     * Injecting dependencies using constructor.
     */
    public UserAuth(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Authentication for user by username and password.
     *
     * @param username username of the User
     * @param password password of the user
     * @return true if user exists, else false.
     */
    public boolean userAuth(String username, String password) {
        UserEntity user = userDao.getUserByUsername(username);
        if (user == null) {
            log.debug("No user with username: {}", username);
            return false;
        }
        return user.getPassword().equals(password);
    }

}
