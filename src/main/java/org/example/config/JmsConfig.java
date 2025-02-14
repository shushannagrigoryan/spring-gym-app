package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.persistence.EntityManagerFactory;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Slf4j
@RequiredArgsConstructor
public class JmsConfig {
    private final Tracer tracer;


    /**
     * ActiveMQConnectionFactory.
     */
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
    }

    /**
     * MessageConverter.
     */

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        ObjectMapper mapper = ObjectMapperConfig.objectMapper();
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter(mapper);

        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        return new MessageConverter() {
            @Override
            @NonNull
            public Message toMessage(@NonNull Object object, @NonNull Session session) throws JMSException {
                Message message = converter.toMessage(object, session);
                return tracingMessagePostProcessor().postProcessMessage(message);
            }

            @NonNull
            public Object fromMessage(@NonNull Message message) throws jakarta.jms.JMSException {
                return converter.fromMessage(message);

            }
        };
    }


    /**
     * JmsTemplate config.
     */
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    /** MessagePostProcessor config.*/
    @Bean
    public MessagePostProcessor tracingMessagePostProcessor() {
        return message -> {
            log.debug("Running the message post processor.");
            if (tracer.currentSpan() != null) {
                log.debug("traceId  = {} ", Objects.requireNonNull(tracer.currentSpan()).context().traceId());
                message.setStringProperty(
                    "traceId", Objects.requireNonNull(tracer.currentSpan()).context().traceId());
            }
            return message;
        };
    }

    /**
     * DefaultJmsListenerContainerFactory.
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ObservationRegistry observationRegistry) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setTransactionManager(jmsTransactionManager());
        factory.setObservationRegistry(observationRegistry);
        factory.setErrorHandler(t -> {
            log.info("Handling error in listener for messages, error: " + t.getMessage());
            log.info(t.getCause().getLocalizedMessage());
        });
        return factory;
    }

    @Bean
    public PlatformTransactionManager jmsTransactionManager() {
        return new JmsTransactionManager(connectionFactory());
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public CompletableFuture<String> responseFuture() {
        return new CompletableFuture<>();
    }
}
