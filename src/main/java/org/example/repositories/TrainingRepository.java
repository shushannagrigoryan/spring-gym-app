package org.example.repositories;

import org.example.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainingRepository extends JpaRepository<TrainingEntity, Long>,
    JpaSpecificationExecutor<TrainingEntity> {

    boolean existsByTrainee_User_UsernameAndTrainer_User_Username(String trainee, String trainer);
}
