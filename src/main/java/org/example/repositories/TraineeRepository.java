package org.example.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<TraineeEntity, Long> {
    Optional<TraineeEntity> findByUser_Username(String username);

    List<TraineeEntity> findByTrainingsTrainerIn(Set<TrainerEntity> trainerEntities);
}
