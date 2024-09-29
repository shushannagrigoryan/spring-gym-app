package repositorytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingRepositoryTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<TrainingEntity> query;
    @InjectMocks
    private TrainingRepository trainingRepository;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreateTraining() {
        //given
        TrainingEntity training = new TrainingEntity();

        //when
        trainingRepository.createTraining(training);

        //then
        verify(session).persist(training);
    }

    @Test
    public void testGetTrainingById() {
        //given
        Long id = 1L;
        TrainingEntity training = new TrainingEntity();
        training.setId(id);
        when(session.get(TrainingEntity.class, id)).thenReturn(training);

        //when
        TrainingEntity result = trainingRepository.getTrainingById(id);

        //then
        verify(session).get(TrainingEntity.class, id);
        assertEquals(training, result);
    }


    @Test
    public void testUpdateTraining() {
        //given
        TrainingEntity training = new TrainingEntity();

        //when
        trainingRepository.updateTraining(training);

        //then
        verify(session).merge(training);
    }

    @Test
    public void testGetAllTrainings() {
        //given
        String hql = "from TrainingEntity";
        List<TrainingEntity> trainings = Arrays.asList(new TrainingEntity(), new TrainingEntity());
        when(session.createQuery(hql, TrainingEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        //when
        List<TrainingEntity> result = trainingRepository.getAllTrainings();

        //then
        verify(session).createQuery(hql, TrainingEntity.class);
        verify(query).getResultList();
        assertEquals(trainings, result);
    }

    @Test
    public void testGetTraineeTrainingsList() {
        //given
        List<TrainingEntity> trainings = new ArrayList<>();
        String username = "A.B";
        String hql = "from TrainingEntity t where t.trainee.user.username = :traineeUsername";
        when(session.createQuery(hql, TrainingEntity.class)).thenReturn(query);
        when(query.setParameter("traineeUsername", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        //when
        List<TrainingEntity> result = trainingRepository.getTraineeTrainingsList(username);

        //then
        assertNotNull(result);
        verify(session).createQuery(hql, TrainingEntity.class);
        verify(query).setParameter("traineeUsername", username);
        verify(query).getResultList();

    }
}
