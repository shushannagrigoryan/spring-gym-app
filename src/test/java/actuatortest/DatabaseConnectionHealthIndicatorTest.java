package actuatortest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.example.actuator.DatabaseConnectionHealthIndicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

@ExtendWith(MockitoExtension.class)
public class DatabaseConnectionHealthIndicatorTest {
    @Mock
    private DataSource dataSource;
    @InjectMocks
    private DatabaseConnectionHealthIndicator databaseConnectionHealthIndicator;

    @Test
    public void testHealthSuccess() throws SQLException {
        //given
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1000)).thenReturn(true);

        //when
        Health health = databaseConnectionHealthIndicator.health();

        //then
        assertEquals(Status.UP, health.getStatus());
        assertEquals("Available", health.getDetails().get("Database"));

    }

    @Test
    public void testHealthConnectionFailure() throws SQLException {
        //given
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1000)).thenReturn(false);

        //when
        Health health = databaseConnectionHealthIndicator.health();

        //then
        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Unavailable", health.getDetails().get("Database"));

    }

    @Test
    public void testHealthFailure() throws SQLException {
        //given
        when(dataSource.getConnection()).thenThrow(new SQLException("Database error"));

        //when
        Health health = databaseConnectionHealthIndicator.health();

        //then
        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Error", health.getDetails().get("Database"));

    }
}
