package org.example.repositories;

import java.util.Optional;
import org.example.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    @Modifying
    @Query("update TokenEntity t set t.revoked =?1 where t.token =?2")
    int updateByTokenSetRevoked(Boolean revoked, String token);
}
