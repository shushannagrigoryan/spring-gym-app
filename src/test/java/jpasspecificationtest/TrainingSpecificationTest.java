package jpasspecificationtest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.entity.TrainingEntity;
import org.example.jpaspecifications.TrainingSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class TrainingSpecificationTest {

    @Mock
    private Root<TrainingEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Path<Object> traineePath;

    @Mock
    private Path<Object> userPath;

    @Mock
    private Path<Object> usernamePath;

    @Mock
    private Path<Object> trainerPath;

    @Test
    void testHasTraineeUsername() {
        String username = "testUser";
        when(root.get("trainee")).thenReturn(traineePath);
        when(traineePath.get("user")).thenReturn(userPath);
        when(userPath.get("username")).thenReturn(usernamePath);

        when(cb.equal(usernamePath, username)).thenReturn(mock(Predicate.class));

        Specification<TrainingEntity> specification = TrainingSpecification.hasTraineeUsername(username);
        Predicate predicate = specification.toPredicate(root, query, cb);

        assertNotNull(predicate);
        verify(cb).equal(usernamePath, username);
    }

    @Test
    void testHasTrainerUsername() {
        when(userPath.get("username")).thenReturn(usernamePath);
        when(root.get("trainer")).thenReturn(trainerPath);
        when(trainerPath.get("user")).thenReturn(userPath);
        String trainerUsername = "trainerUser";
        when(cb.equal(usernamePath, trainerUsername)).thenReturn(mock(Predicate.class));

        Specification<TrainingEntity> specification = TrainingSpecification.hasTrainerUsername(trainerUsername);
        Predicate predicate = specification.toPredicate(root, query, cb);

        assertNotNull(predicate);
        verify(cb).equal(usernamePath, trainerUsername);
    }
}

