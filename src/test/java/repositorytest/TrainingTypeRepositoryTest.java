//package repositorytest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import org.example.entity.TrainingTypeEntity;
//import org.example.repository.TrainingTypeRepository;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingTypeRepositoryTest {
//    @Mock
//    private SessionFactory sessionFactory;
//    @Mock
//    private Session session;
//    @Mock
//    private Query<TrainingTypeEntity> query;
//    @InjectMocks
//    private TrainingTypeRepository trainingTypeRepository;
//
//    @BeforeEach
//    void setUp() {
//        when(sessionFactory.getCurrentSession()).thenReturn(session);
//    }
//
//
//    @Test
//    public void testGetTrainingTypeById() {
//        //given
//        Long id = 1L;
//        String hql = "from TrainingTypeEntity t where t.id = :id";
//
//        TrainingTypeEntity trainingType = new TrainingTypeEntity();
//        trainingType.setId(id);
//        when(session.createQuery(hql, TrainingTypeEntity.class)).thenReturn(query);
//        when(query.setParameter("id", id)).thenReturn(query);
//        when(query.uniqueResult()).thenReturn(trainingType);
//
//
//        //when
//        Optional<TrainingTypeEntity> result = trainingTypeRepository.findById(id);
//
//        //then
//        verify(session).createQuery(hql, TrainingTypeEntity.class);
//        verify(query).setParameter("id", id);
//        verify(query).uniqueResult();
//        assertEquals(Optional.of(trainingType), result);
//    }
//
//    @Test
//    public void testGetTrainingTypeByName() {
//        //given
//        String name = "trainingType1";
//        String hql = "from TrainingTypeEntity t where t.trainingTypeName = :name";
//        TrainingTypeEntity trainingType = new TrainingTypeEntity();
//        trainingType.setTrainingTypeName(name);
//        when(session.createQuery(hql, TrainingTypeEntity.class)).thenReturn(query);
//        when(query.setParameter("name", name)).thenReturn(query);
//        when(query.uniqueResult()).thenReturn(trainingType);
//
//        //when
//        TrainingTypeEntity result = trainingTypeRepository.getTrainingTypeByName(name);
//
//        //then
//        verify(session).createQuery(hql, TrainingTypeEntity.class);
//        verify(query).setParameter("name", name);
//        verify(query).uniqueResult();
//        assertEquals(trainingType, result);
//    }
//
//    @Test
//    public void testFindAll() {
//        //given
//        List<TrainingTypeEntity> trainingTypes = Arrays.asList(new TrainingTypeEntity(), new TrainingTypeEntity());
//        String hql = "from TrainingTypeEntity";
//        when(session.createQuery(hql, TrainingTypeEntity.class)).thenReturn(query);
//        when(query.getResultList()).thenReturn(trainingTypes);
//
//        //when
//        List<TrainingTypeEntity> result = trainingTypeRepository.findAll();
//
//        //then
//        verify(session).createQuery(hql, TrainingTypeEntity.class);
//        verify(query).getResultList();
//        assertEquals(trainingTypes, result);
//    }
//
//
//}
