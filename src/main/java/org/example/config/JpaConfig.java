//package org.example.config;
//
//import java.util.Properties;
//import javax.sql.DataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.flywaydb.core.Flyway;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//@ComponentScan(basePackages = "org.example")
//@EnableJpaRepositories(basePackages = "org.example.repository")
//@Slf4j
//public class JpaConfig {
//
//    /**
//     * data source.
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
//     * entity manager.
//     */
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("org.example.entity");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(hibernateProperties());
//        return em;
//    }
//
//    private Properties hibernateProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.setProperty("hibernate.show_sql", "true");
//        return properties;
//    }
//
//    /**
//     * transaction manager.
//     */
//    @Bean
//    public JpaTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
//
//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//
//    /**
//     * Flyway configuration.
//     */
//    @Bean(initMethod = "migrate")
//    public Flyway flyway(DataSource dataSource) {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .locations("classpath:db/migration")
//                .baselineOnMigrate(true)
//                .load();
//        flyway.repair();
//        flyway.migrate();
//        log.info("Flyway configuration completed successfully");
//        return flyway;
//    }
//}
