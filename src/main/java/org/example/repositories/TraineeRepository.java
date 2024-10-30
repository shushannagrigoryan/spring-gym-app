package org.example.repositories;

import java.util.Optional;
import org.example.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<TraineeEntity, Long> {
    Optional<TraineeEntity> findByUser_Username(String username);
}
