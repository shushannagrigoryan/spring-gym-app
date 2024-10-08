package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ChangePasswordDto;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;
    /**
     * Setting dependencies.
     */

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET request to login a user.
     *
     * @param username username of the user
     * @param password password of the user
     */
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestHeader("username") String username,
                                        @RequestHeader("password") String password) {
        log.debug("Request to login a user.");
        userService.login(username, password);
        return new ResponseEntity<>("Successfully logged in.", HttpStatus.OK);
    }

    /**
     * PUT request to change user password.
     *
     * @param changePasswordDto request contains:
     *                          username(required)
     *                          password(required)
     *                          newPassword(required)
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        log.debug("Request to change password of user with username: {}", changePasswordDto.getUsername());
        userService.changeUserPassword(changePasswordDto.getUsername(),
                changePasswordDto.getNewPassword());
        return new ResponseEntity<>("Successfully changed user password.", HttpStatus.OK);
    }

}
