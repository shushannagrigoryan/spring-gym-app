//package repositorytest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import org.example.entity.UserEntity;
//import org.example.repository.UserRepository;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.MutationQuery;
//import org.hibernate.query.Query;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class UserRepositoryTest {
//    @Mock
//    private SessionFactory sessionFactory;
//    @Mock
//    private Session session;
//    @Mock
//    private Query<String> query;
//    @Mock
//    private Query<UserEntity> query1;
//    @Mock
//    private MutationQuery mutationQuery;
//    @InjectMocks
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        when(sessionFactory.getCurrentSession()).thenReturn(session);
//    }
//
//    @Test
//    public void testSaveUser() {
//        //given
//        UserEntity user = new UserEntity();
//        when(session.merge(user)).thenReturn(new UserEntity());
//        doNothing().when(session).flush();
//
//        //when
//        userRepository.save(user);
//
//        //then
//        verify(session).merge(user);
//    }
//
//    @Test
//    public void testGetAllUsernames() {
//        //given
//        String hql = "select t.username from UserEntity t";
//        List<String> usernames = Arrays.asList("A.B", "A.B1");
//        when(session.createQuery(hql, String.class)).thenReturn(query);
//        when(query.getResultList()).thenReturn(usernames);
//
//        //when
//        List<String> result = userRepository.getAllUsernames();
//
//        //then
//        verify(session).createQuery(hql, String.class);
//        verify(query).getResultList();
//        assertEquals(usernames, result);
//    }
//
//    @Test
//    public void testGetUserByUsername() {
//        //given
//        String username = "A.B";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        String hql = "from UserEntity u where u.username = :username";
//        when(session.createQuery(hql, UserEntity.class)).thenReturn(query1);
//        when(query1.setParameter("username", username)).thenReturn(query1);
//        when(query1.uniqueResult()).thenReturn(user);
//
//        //when
//        Optional<UserEntity> result = userRepository.getUserByUsername(username);
//
//        //then
//        assertNotNull(result);
//        assertEquals(Optional.of(user), result);
//        verify(session).createQuery(hql, UserEntity.class);
//        verify(query1).setParameter("username", username);
//        verify(query1).uniqueResult();
//    }
//
//    @Test
//    public void testGetByUsernameAndPasswordSuccess() {
//        //given
//        String username = "A.B";
//        String password = "myPassword";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        user.setPassword(password);
//        String hql = "from UserEntity u where u.username = :username and u.password= :password";
//        when(session.createQuery(hql, UserEntity.class)).thenReturn(query1);
//        when(query1.setParameter("username", username)).thenReturn(query1);
//        when(query1.setParameter("password", password)).thenReturn(query1);
//        when(query1.uniqueResult()).thenReturn(user);
//
//        //when
//        Optional<UserEntity> result = userRepository.getUserByUsernameAndPassword(username, password);
//
//        //then
//        assertNotNull(result);
//        assertEquals(Optional.of(user), result);
//        verify(session).createQuery(hql, UserEntity.class);
//        verify(query1).setParameter("username", username);
//        verify(query1).setParameter("password", password);
//        verify(query1).uniqueResult();
//    }
//
//    @Test
//    public void testUpdatePassword() {
//        //given
//        String username = "A.B";
//        String password = "myPassword";
//        UserEntity user = new UserEntity();
//        user.setUsername(username);
//        user.setPassword(password);
//        String hql = "update UserEntity u set u.password =:newPassword where u.username = :username";
//        when(session.createMutationQuery(hql)).thenReturn(mutationQuery);
//        when(mutationQuery.setParameter("username", username)).thenReturn(mutationQuery);
//        when(mutationQuery.setParameter("newPassword", password)).thenReturn(mutationQuery);
//        when(mutationQuery.executeUpdate()).thenReturn(1);
//
//        //when
//        userRepository.updatePassword(username, password);
//
//        //then
//
//        verify(session).createMutationQuery(hql);
//        verify(mutationQuery).setParameter("username", username);
//        verify(mutationQuery).setParameter("newPassword", password);
//        verify(mutationQuery).executeUpdate();
//
//    }
//
//    @Test
//    public void testDeleteById() {
//        //given
//        Long id = 1L;
//        UserEntity user = new UserEntity();
//        when(session.get(UserEntity.class, id)).thenReturn(user);
//        doNothing().when(session).remove(user);
//
//        //when
//        userRepository.deleteById(id);
//
//        //then
//        verify(session).get(UserEntity.class, id);
//        verify(session).remove(user);
//
//    }
//
//
//}
