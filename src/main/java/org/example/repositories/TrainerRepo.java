package org.example.repositories;

import java.util.Optional;
import org.example.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepo extends JpaRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByUser_Username(String username);
}
