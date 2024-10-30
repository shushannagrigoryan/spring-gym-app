package filtertest;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import org.example.filter.TransactionIdFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class TransactionIdFilterTest {

    @Mock
    private ServletRequest request;

    @Mock
    private ServletResponse response;

    @Mock
    private FilterChain chain;
    @InjectMocks
    private TransactionIdFilter filter;


    @Test
    void testDoFilter() throws IOException, ServletException {
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
        assertNull(MDC.get("transactionId"));
    }
}
