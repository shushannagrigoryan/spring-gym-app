package org.example.repositories;

import java.util.Optional;
import org.example.entity.LoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttemptEntity, Long> {
    Optional<LoginAttemptEntity> findByUserIp(String userIp);
}
