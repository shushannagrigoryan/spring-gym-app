package org.example.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.services.TraineeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerHasPermissionOnTrainee {
    private static final SimpleGrantedAuthority ROLE_TRAINER = new SimpleGrantedAuthority("ROLE_TRAINER");
    private final TraineeService traineeService;

    public boolean hasPermission(Authentication authentication, Object targetDomainObject) {
        if (authentication.getAuthorities().contains(ROLE_TRAINER)) {
            List<String> assignedTrainees = traineeService.getAssignedTrainees(
                authentication.getName());
            return assignedTrainees.contains((String) targetDomainObject);
        }
        return false;
    }
}
