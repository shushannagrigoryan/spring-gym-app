package org.example.username;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.services.UserService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsernameGenerator {
    private final UserService userService;

    /**
     * Injecting dependencies using constructor.
     */
    public UsernameGenerator(UserService userService) {
        this.userService = userService;
    }

    /**
     * Generates username for the user (firstName.lastName)
     * Adds suffix if the username is taken either by a trainee or a trainer.
     *
     * @param firstName firstName of the user
     * @param lastName  lastName of the user
     * @return the username
     */

    public String generateUsername(String firstName, String lastName) {
        log.debug("Generating username for firstName: {}, lastName: {}", firstName, lastName);

        String username = firstName.concat(".").concat(lastName);

        Optional<UserEntity> user = userService.getUserByUsername(username);

        if (user.isPresent()) {
            log.debug("Username taken.");
            return username + getSuffix(username);
        }
        return username;
    }


    /**
     * Generates suffix for the given username which is not present both for trainees and trainers.
     *
     * @param username username for which suffix is generated
     * @return the suffix
     */
    public Long getSuffix(String username) {
        log.debug("Generating suffix for username: {}", username);
        List<String> allUsernames = userService.getAllUsernamesWithPrefix(username);

        Long suffix = allUsernames.stream()
            .map(u -> u.substring(username.length()))
            .filter(s -> !s.isEmpty()).map(Long::valueOf)
            .max(Long::compareTo).orElse(0L);

        return suffix + 1;
    }

}
