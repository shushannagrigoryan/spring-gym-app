package repositorytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
    public void testSaveTrainee() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);

        //when
        traineeRepository.save(trainee);

        //then
        verify(session).persist(user);
        verify(session).merge(trainee);
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
        Optional<TraineeEntity> result = traineeRepository.findByUsername(username);

        //then
        assertEquals(Optional.of(trainee), result);
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
        Optional<TraineeEntity> result = traineeRepository.findById(id);

        //then
        verify(session).get(TraineeEntity.class, id);
        assertEquals(Optional.of(trainee), result);
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
        traineeRepository.deleteByUsername(username);

        //then
        verify(session).remove(user);
        verify(session).remove(trainee);
    }

    @Test
    public void testDeleteTraineeById() {
        //given
        Long id  = 1L;
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        String hql = "from TraineeEntity t where t.id =:id";
        when(session.createQuery(hql, TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        //when
        traineeRepository.deleteById(id);

        //then
        verify(session).remove(user);
        verify(session).remove(trainee);
    }

    @Test
    public void testDelete() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        doNothing().when(session).remove(trainee);
        doNothing().when(session).remove(user);

        //when
        traineeRepository.delete(trainee);

        //then
        verify(session).remove(user);
        verify(session).remove(trainee);
    }

}
