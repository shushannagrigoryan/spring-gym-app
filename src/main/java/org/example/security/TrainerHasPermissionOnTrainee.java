package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.services.TrainingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerHasPermissionOnTrainee {

    //TODO I think here could be not static
    private static final SimpleGrantedAuthority ROLE_TRAINER = new SimpleGrantedAuthority("ROLE_TRAINER");
    private final TrainingService trainingService;

    /**
     * Returns true if the trainer has permission on trainee.
     */
    public boolean hasPermission(Authentication authentication, String traineeUsername) {
        if (authentication.getAuthorities().contains(ROLE_TRAINER)) {
            return trainingService.trainingExists(traineeUsername, authentication.getName());
        }
        return false;
    }
}
