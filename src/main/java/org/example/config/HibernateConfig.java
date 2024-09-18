package org.example.config;

import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "org.example")
public class HibernateConfig {

    /**
     * create sessionfactory object.
     *
     * @return sessionfactory
     */
    @Bean
    public SessionFactory sessionFactory() {

        try {
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();

            ServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            configuration.addAnnotatedClass(TraineeEntity.class);
            configuration.addAnnotatedClass(UserEntity.class);
            configuration.addAnnotatedClass(TrainerEntity.class);
            configuration.addAnnotatedClass(TrainingEntity.class);
            configuration.addAnnotatedClass(TrainingTypeEntity.class);

            return configuration.configure().buildSessionFactory(registry);
        } catch (HibernateException e) {
            throw new RuntimeException("Failed to create SessionFactory", e);
        }

    }


}
