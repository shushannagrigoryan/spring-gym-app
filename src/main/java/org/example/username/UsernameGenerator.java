package org.example.username;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.services.UserService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UsernameGenerator {
    private final UserService userService;

    /**
     * Generates username for the user (firstName.lastName)
     * Adds suffix if the username is taken either by a trainee or a trainer.
     * Sets the user usernameIndex field to suffix if present otherwise to 0.
     *
     * @param user User to generate username for
     * @return the username
     */

    public String generateUsername(UserEntity user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        log.debug("Generating username for user with firstName: {} and  lastName: {}", firstName, lastName);

        String username = firstName.concat(".").concat(lastName);
        Long usernameMaxIndex = userService.getUsernameMaxIndex(firstName, lastName);

        long index = usernameMaxIndex == null ? 0 : usernameMaxIndex + 1;
        log.debug("Setting username index to: {}", index);
        user.setUsernameIndex(index);

        return index == 0 ? username : username.concat(String.valueOf(usernameMaxIndex + 1));

    }

}
