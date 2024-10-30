package org.example.repositories;

import org.example.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainingRepository extends JpaRepository<TrainingEntity, Long>,
        JpaSpecificationExecutor<TrainingEntity> {

}
