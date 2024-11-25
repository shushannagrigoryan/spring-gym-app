package org.example.repositories;

import java.util.List;
import java.util.Optional;
import org.example.entity.TokenEntity;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    List<TokenEntity> findByUserAndRevoked(UserEntity user, boolean revoked);

    Optional<TokenEntity> findByToken(String token);
}
