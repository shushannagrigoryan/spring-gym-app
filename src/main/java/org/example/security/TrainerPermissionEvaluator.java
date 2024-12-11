package org.example.security;

import java.io.Serializable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.services.TraineeService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class TrainerPermissionEvaluator implements PermissionEvaluator {
    private static final SimpleGrantedAuthority ROLE_TRAINER = new SimpleGrantedAuthority("ROLE_TRAINER");
    private final TraineeService traineeService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication.getAuthorities().contains(ROLE_TRAINER)) {
            List<String> assignedTrainees = traineeService.getAssignedTrainees(
                authentication.getName());
            return assignedTrainees.contains((String) targetDomainObject);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        return false;
    }
}