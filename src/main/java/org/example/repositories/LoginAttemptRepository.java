package org.example.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import org.example.entity.LoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LoginAttemptRepository extends JpaRepository<LoginAttemptEntity, Long> {
    Optional<LoginAttemptEntity> findByUserIp(String userIp);
    @Modifying
    @Query("update LoginAttemptEntity l set l.failedCount = ?1, l.lastFailedAttempt = ?2 where l.userIp =?3")
    void updateByUserIp(Integer failedCount, LocalDateTime lastFailedAttempt, String userIp);
}
