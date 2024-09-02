package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TrainerDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TrainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    private UserService userService;
    private SaveDataToFile saveDataToFile;
    private TrainerMapper trainerMapper;

    @Autowired
    public void setDependencies(UserService userService, SaveDataToFile saveDataToFile,
                                TrainerMapper trainerMapper){
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
        this.trainerMapper = trainerMapper;
    }

    public void createTrainer(TrainerEntity trainerEntity){
        log.debug("Creating trainer with firstName: {}, lastName: {}",
                trainerEntity.getFirstName(), trainerEntity.getLastName());

        if (!ValidatePassword.isValidPassword(trainerEntity.getPassword())){
            log.debug("Invalid password for trainer");
            throw new IllegalPasswordException(trainerEntity.getPassword());
        }

        String username = userService.generateUsername(trainerEntity.getFirstName(),
                trainerEntity.getLastName());
        trainerEntity.setUsername(username);
        trainerDao.createTrainer(trainerEntity);
        log.debug("Created a new trainer with username: "+ username);
        saveDataToFile.writeMapToFile("Trainer");
    }

    public TrainerDto getTrainerByUsername(String username){
        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(username);
        if (!trainer.isPresent()){
            log.debug("No trainer with the username: " + username);
            throw new IllegalUsernameException(username);
        }
        log.debug("Getting trainer with username: " + username);
        return trainerMapper.entityToDto(trainer.get());
    }

    public TrainerDto getTrainerById(Long id){
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);
        if (!trainer.isPresent()){
            throw new IllegalIdException(id);
        }
        log.debug("Getting trainer with username: " + id);
        return trainerMapper.entityToDto(trainer.get());
    }

    public void updateTrainerById(Long id, TrainerEntity trainerEntity){
        if (!ValidatePassword.isValidPassword(trainerEntity.getPassword())){
            log.debug("Invalid password for trainer");
            throw new IllegalPasswordException(trainerEntity.getPassword());
        }

        String username = userService.generateUsername(trainerEntity.getFirstName(),
                trainerEntity.getLastName());
        trainerEntity.setUsername(username);
        trainerEntity.setUserId(id);
        trainerDao.updateTrainerById(id, trainerEntity);
        log.debug("Updated trainer with id: " + id);
        saveDataToFile.writeMapToFile("Trainer");
    }
}
