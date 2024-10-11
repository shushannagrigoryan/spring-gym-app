package org.example.repository;

import java.util.Optional;
import java.util.Set;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByUser_Username(String username);

    Set<TrainerEntity> getTrainerEntityByTraineesNotContainingAndUser_isActive(TraineeEntity traineeEntity,
                                                                     boolean isActive);


}
