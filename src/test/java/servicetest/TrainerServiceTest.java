//package servicetest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.Optional;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.example.exceptions.GymEntityNotFoundException;
//import org.example.exceptions.GymIllegalArgumentException;
//import org.example.password.PasswordGeneration;
//import org.example.repository.TrainerRepository;
//import org.example.services.TraineeService;
//import org.example.services.TrainerService;
//import org.example.services.TrainingTypeService;
//import org.example.username.UsernameGenerator;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerServiceTest {
//    @Mock
//    private UsernameGenerator usernameGenerator;
//    @Mock
//    private PasswordGeneration passwordGeneration;
//
//    @Mock
//    private TrainerRepository trainerRepository;
//    @Mock
//    private TrainingTypeService trainingTypeService;
//    @Mock
//    private TraineeService traineeService;
//
//    @InjectMocks
//    private TrainerService trainerService;
//
//    @Test
//    public void testRegisterTrainerSuccess() {
//        //given
//        String password = "myPassword";
//        TrainerEntity trainerEntity = new TrainerEntity();
//        trainerEntity.setUser(new UserEntity());
//        trainerEntity.getUser().setPassword(password);
//        when(usernameGenerator.generateUsername(trainerEntity.getUser().getFirstName(),
//                trainerEntity.getUser().getLastName()))
//                .thenReturn("Jack.Jones");
//        when(trainerRepository.save(trainerEntity)).thenReturn(new TrainerEntity());
//
//
//        //when
//        trainerService.registerTrainer(trainerEntity);
//
//        //then
//        verify(passwordGeneration).generatePassword();
//        verify(usernameGenerator).generateUsername(trainerEntity.getUser().getFirstName(),
//                trainerEntity.getUser().getLastName());
//
//    }
//
//    @Test
//    public void testGetTraineeByUsernameSuccess() {
//        //given
//        String firstName = "traineeF1";
//        String lastName = "traineeF2";
//        String username = firstName.concat(".").concat(lastName);
//        TrainerEntity trainerEntity = new TrainerEntity();
//        UserEntity user = new UserEntity();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        trainerEntity.setUser(user);
//        trainerEntity.setSpecialization(new TrainingTypeEntity());
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainerEntity));
//
//        //when
//        trainerService.getTrainerByUsername(username);
//
//        //then
//        verify(trainerRepository).findByUsername(username);
//    }
//
//    @Test
//    public void testGetTrainerByUsernameFailure() {
//        //given
//        String username = "John.Smith";
//
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        //then
//        assertThrows(GymEntityNotFoundException.class,
//                () -> trainerService.getTrainerByUsername(username),
//                String.format("Trainer with username %s does not exist.", username));
//        verify(trainerRepository).findByUsername(username);
//    }
//
//    @Test
//    public void testGetTrainerByIdSuccess() {
//        //given
//        TrainerEntity trainerEntity = new TrainerEntity();
//        Long id = 1L;
//        trainerEntity.setId(id);
//        when(trainerRepository.findById(id)).thenReturn(Optional.of(trainerEntity));
//
//        //when
//        TrainerEntity trainer = trainerService.getTrainerById(id);
//
//        //then
//        verify(trainerRepository).findById(id);
//        assertNotNull(trainer);
//    }
//
//    @Test
//    public void testGetTrainerBIdFailure() {
//        //given
//        Long id = 1L;
//
//        when(trainerRepository.findById(id)).thenReturn(Optional.empty());
//
//        //then
//        assertThrows(GymEntityNotFoundException.class,
//                () -> trainerService.getTrainerById(id),
//                String.format("No trainer with id: %d", id));
//        verify(trainerRepository).findById(id);
//    }
//
//
//    @Test
//    public void testNotAssignedOnTraineeActiveTrainers() {
//        //given
//        String username = "A.A";
//        when(traineeService.getTraineeByUsername(username)).thenReturn(new TraineeEntity());
//        UserEntity user = new UserEntity();
//        TrainerEntity trainer = new TrainerEntity();
//        trainer.setUser(user);
//        when(trainerRepository.getTrainersNotAssignedToTraineeActiveTrainers(username))
//                .thenReturn(List.of(trainer));
//
//        //when
//        List<TrainerEntity> result = trainerService.notAssignedOnTraineeActiveTrainers(username);
//
//        //then
//        verify(trainerRepository).getTrainersNotAssignedToTraineeActiveTrainers(username);
//        assertEquals(1, result.size());
//    }
//
//
//    @Test
//    public void testUpdateTrainerByUsernameInvalidId() {
//        //given
//        String username = "A.A";
//        TrainerEntity trainer = new TrainerEntity();
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        trainer.setUser(user);
//        trainerRepository.findByUsername(username);
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        //then
//        RuntimeException exception =
//                assertThrows(GymIllegalArgumentException.class,
//                        () -> trainerService.updateTrainer(trainer));
//        assertEquals(String.format("No trainer with username: %s", username), exception.getMessage());
//    }
//
//    @Test
//    public void testUpdateTraineeSuccess() {
//        // given
//        String firstName = "A";
//        String lastName = "V";
//        TrainerEntity trainerEntity = new TrainerEntity();
//        UserEntity user = new UserEntity();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setUsername("A.V");
//        trainerEntity.setUser(user);
//        TrainingTypeEntity specialization = new TrainingTypeEntity();
//        specialization.setId(1L);
//        trainerEntity.setSpecialization(specialization);
//
//        when(trainerRepository.findByUsername("A.V")).thenReturn(Optional.of(trainerEntity));
//        user.setFirstName("B");
//        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
//
//        // when
//        TrainerEntity trainer = trainerService.updateTrainer(trainerEntity);
//
//        // then
//        assertNotNull(trainer);
//        assertEquals("B", trainer.getUser().getFirstName());
//        assertEquals("A.V", trainer.getUser().getUsername());
//    }
//
//    @Test
//    public void testGetTrainerProfileSuccess() {
//        //given
//        String username = "A.A";
//        TrainerEntity trainer = new TrainerEntity();
//        TrainingEntity training = new TrainingEntity();
//        training.setTrainer(new TrainerEntity());
//        trainer.setTrainings(List.of(training));
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
//
//        //when
//        TrainerEntity result = trainerService.getTrainerProfile(username);
//
//        //then
//        verify(trainerRepository).findByUsername(username);
//        assertEquals(trainer, result);
//
//    }
//
//    @Test
//    public void testGetTrainerProfileFailure() {
//        //given
//        String username = "A.A";
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        //then
//        assertThrows(GymEntityNotFoundException.class,
//                () -> trainerService.getTrainerProfile(username),
//                String.format("Trainer with username %s does not exist.", username));
//
//    }
//
//    @Test
//    public void testDeactivateTrainerSuccess() {
//        //given
//        String username = "A.I";
//        TrainerEntity trainer = new TrainerEntity();
//        UserEntity user = new UserEntity();
//        user.setActive(true);
//        user.setUsername(username);
//        trainer.setUser(user);
//
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
//        TrainerEntity activatedTrainer = new TrainerEntity();
//        activatedTrainer.setUser(user);
//        activatedTrainer.getUser().setActive(true);
//        when(trainerRepository.save(trainer)).thenReturn(activatedTrainer);
//
//        //when
//        String result = trainerService.changeActiveStatus(username, false);
//
//        //then
//        verify(trainerRepository).findByUsername(username);
//        verify(trainerRepository).save(trainer);
//        assertEquals("Successfully set trainer active status to " + false, result);
//    }
//
//    @Test
//    public void testDeactivateTrainerAlreadyInactive() {
//        //given
//        String username = "A.I";
//        TrainerEntity trainer = new TrainerEntity();
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        trainer.setUser(user);
//        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
//
//        //when
//        String result = trainerService.changeActiveStatus(username, false);
//
//        //then
//        verify(trainerRepository).findByUsername(username);
//        assertEquals("Trainer isActive status is already " + false, result);
//    }
//
//    @Test
//    public void testDeactivateTrainerNotFound() {
//        //given
//        String username = "A.I";
//        doThrow(new GymEntityNotFoundException(String.format("Trainer with username %s does not exist.", username)))
//                .when(trainerRepository).findByUsername(username);
//        assertThrows(GymEntityNotFoundException.class, () ->
//                        trainerService.changeActiveStatus(username, false),
//                String.format("Trainer with username %s does not exist.", username));
//    }
//
//}
