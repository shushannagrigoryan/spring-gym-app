package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ChangePasswordDto;
import org.example.dto.LoginRequestDto;
import org.example.entity.UserEntity;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    /** Setting dependencies. */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** login. */
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.debug("Request to login.");
        userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        return new ResponseEntity<>("Successfully logged in.", HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        log.debug("Request to change password of user with username: {}", changePasswordDto.getUsername());
        int result = userService.changeUserPassword(changePasswordDto.getUsername());
        System.out.println(result);
    }

}
