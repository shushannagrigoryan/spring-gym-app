package org.example.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepo extends JpaRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByUser_Username(String username);

    List<TrainerEntity> findByTraineesNotContainingAndUserActive(Set<TraineeEntity> trainees, boolean active);

}
