package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.UserAuth;
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
    private final UserAuth userAuth;

    /**
     * Setting dependencies.
     */
    public UserController(UserService userService,
                          UserAuth userAuth) {
        this.userService = userService;
        this.userAuth = userAuth;
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
        userAuth.userAuth(username, password);
        userService.login(username, password);
        return new ResponseEntity<>("Successfully logged in.", HttpStatus.OK);
    }

    /**
     * change user password.
     */
    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        log.debug("Request to change password of user with username: {}", changePasswordDto.getUsername());
        //TODO validation with old password
        int result = userService.changeUserPassword(changePasswordDto.getUsername(),
                changePasswordDto.getNewPassword());

        System.out.println(result);
    }

}
