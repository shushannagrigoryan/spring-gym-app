package org.example.repositories;

import org.example.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepo extends JpaRepository<TrainingEntity, Long> {

}
