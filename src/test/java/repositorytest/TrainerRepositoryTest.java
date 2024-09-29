package repositorytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.repository.TrainerRepository;
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
public class TrainerRepositoryTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<TrainerEntity> query;
    @InjectMocks
    private TrainerRepository trainerRepository;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreateTrainer() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);

        //when
        trainerRepository.createTrainer(trainer);

        //then
        verify(session).persist(user);
        verify(session).persist(trainer);
    }

    @Test
    public void testGetTrainerByUsername() {
        //given
        String username = "A.B";
        TrainerEntity trainer = new TrainerEntity();
        String hql = "from TrainerEntity t where t.user.username = :username";
        when(session.createQuery(hql, TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainer);

        //when
        TrainerEntity result = trainerRepository.getTrainerByUsername(username);

        //then
        assertEquals(trainer, result);
        verify(session).createQuery(hql, TrainerEntity.class);
        verify(query).setParameter("username", username);
        verify(query).uniqueResult();
    }

    @Test
    public void testGetTrainerById() {
        //given
        Long id = 1L;
        UserEntity user = new UserEntity();
        user.setId(id);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(session.get(TrainerEntity.class, id)).thenReturn(trainer);

        //when
        TrainerEntity result = trainerRepository.getTrainerById(id);

        //then
        verify(session).get(TrainerEntity.class, id);
        assertEquals(trainer, result);
    }

    @Test
    public void testChangeTrainerPassword() {
        //given
        String username = "A.B";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        String hql = "from TrainerEntity t where t.user.username =:username";
        when(session.createQuery(hql, TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainer);

        String password = "password12";
        trainer.getUser().setPassword(password);

        //when
        trainerRepository.changeTrainerPassword(username, password);

        //then
        verify(session).merge(trainer);
    }

    @Test
    public void testActivateTrainer() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);

        //when
        trainerRepository.activateTrainer(trainer);

        //then
        verify(session).merge(trainer);
        assertTrue(trainer.getUser().isActive());
    }

    @Test
    public void testDeactivateTrainer() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);

        //when
        trainerRepository.deactivateTrainer(trainer);

        //then
        verify(session).merge(trainer);
        assertFalse(trainer.getUser().isActive());
    }

    @Test
    public void testUpdateTrainerById() {
        //given
        Long id = 1L;
        TrainerEntity trainer = new TrainerEntity();

        //when
        trainerRepository.updateTrainerById(id, trainer);

        //then
        verify(session).merge(trainer);
    }

}
