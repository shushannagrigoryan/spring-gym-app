package repositorytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.repository.TraineeRepository;
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
public class TraineeRepositoryTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<TraineeEntity> query;
    @InjectMocks
    private TraineeRepository traineeRepository;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreateTrainee() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);

        //when
        traineeRepository.createTrainee(trainee);

        //then
        verify(session).persist(user);
        verify(session).persist(trainee);
    }

    @Test
    public void testGetTraineeByUsername() {
        //given
        String username = "A.B";
        TraineeEntity trainee = new TraineeEntity();
        String hql = "from TraineeEntity t where t.user.username = :username";
        when(session.createQuery(hql, TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        //when
        TraineeEntity result = traineeRepository.getTraineeByUsername(username);

        //then
        assertEquals(trainee, result);
        verify(session).createQuery(hql, TraineeEntity.class);
        verify(query).setParameter("username", username);
        verify(query).uniqueResult();
    }

    @Test
    public void testGetTraineeById() {
        //given
        Long id = 1L;
        UserEntity user = new UserEntity();
        user.setId(id);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(session.get(TraineeEntity.class, id)).thenReturn(trainee);

        //when
        TraineeEntity result = traineeRepository.getTraineeById(id);

        //then
        verify(session).get(TraineeEntity.class, id);
        assertEquals(trainee, result);
    }

    @Test
    public void testChangeTraineePassword() {
        //given
        String username = "A.B";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        String hql = "from TraineeEntity t where t.user.username =:username";
        when(session.createQuery(hql, TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        String password = "password12";
        trainee.getUser().setPassword(password);

        //when
        traineeRepository.changeTraineePassword(username, password);

        //then
        verify(session).merge(trainee);
    }

    @Test
    public void testActivateTrainee() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);

        //when
        traineeRepository.activateTrainee(trainee);

        //then
        verify(session).merge(trainee);
        assertTrue(trainee.getUser().isActive());
    }

    @Test
    public void testDeactivateTrainee() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);

        //when
        traineeRepository.deactivateTrainee(trainee);

        //then
        verify(session).merge(trainee);
        assertFalse(trainee.getUser().isActive());
    }

    @Test
    public void testUpdateTraineeById() {
        //given
        Long id = 1L;
        TraineeEntity trainee = new TraineeEntity();

        //when
        traineeRepository.updateTraineeById(id, trainee);

        //then
        verify(session).merge(trainee);
    }

    @Test
    public void testDeleteTraineeByUsername() {
        //given
        String username = "A.B";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        String hql = "from TraineeEntity t where t.user.username =:username";
        when(session.createQuery(hql, TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        //when
        traineeRepository.deleteTraineeByUsername(username);

        //then
        verify(session).remove(user);
        verify(session).remove(trainee);
    }
}
