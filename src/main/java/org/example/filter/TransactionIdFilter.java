package org.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@WebFilter(urlPatterns = {"/*"})
public class TransactionIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
