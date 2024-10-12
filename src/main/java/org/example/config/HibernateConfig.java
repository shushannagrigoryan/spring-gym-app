//package org.example.config;
//
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//
//@Configuration
//@PropertySource("classpath:application.properties")
//@ComponentScan(basePackages = "org.example")
//@EnableAspectJAutoProxy
//@EnableTransactionManagement
//public class HibernateConfig {
//    /**
//     * Data Source.
//     */
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/gymdb");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("postgres");
//        return dataSource;
//    }
//
//    /**
//     * SessionFactory.
//     */
//    @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource());
//        sessionFactory.setPackagesToScan("org.example.entity");
//
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        hibernateProperties.put("hibernate.show_sql", "true");
//        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
//        sessionFactory.setHibernateProperties(hibernateProperties);
//
//        sessionFactory.setAnnotatedClasses(
//                TrainerEntity.class,
//                TraineeEntity.class,
//                TrainingEntity.class,
//                TrainingTypeEntity.class,
//                UserEntity.class
//        );
//
//        return sessionFactory;
//    }
//
//    /**
//     * Hibernate transaction manager.
//     */
//    @Bean
//    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory);
//        return transactionManager;
//    }
//}
