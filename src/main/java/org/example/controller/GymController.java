package org.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GymController {
    @GetMapping
    public ResponseEntity<String> hello() {
        System.out.println("Gym App");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
