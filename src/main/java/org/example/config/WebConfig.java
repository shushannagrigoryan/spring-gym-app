//package org.example.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import java.sql.SQLException;
//import java.util.Map;
//import javax.sql.DataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.flywaydb.core.Flyway;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = "org.example")
//@EnableJpaRepositories(basePackages = "org.example.repository")
//@EnableTransactionManagement
//@Slf4j
//public class WebConfig implements WebMvcConfigurer {
//
//    /** data source. */
//    @Bean
//    public DataSource dataSource() throws SQLException {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/gymdb");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("postgres");
//        log.debug("DataSource configuration finished successfully.");
//        log.debug("dataSource schema: {}", dataSource.getSchema());
//        log.debug("dataSource connection: {} ",dataSource.getConnection());
//        return dataSource;
//    }
//    //
//    //    /** entity manager factory. */
//    //    @Bean
//    //    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
//    //        System.out.println("Manager factory configuration.");
//    //        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//    //        em.setDataSource(dataSource());
//    //        em.setPackagesToScan("org.example.entity");
//    //
//    //        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//    //        em.setJpaVendorAdapter(vendorAdapter);
//    //
//    //        em.setJpaPropertyMap(Map.of(
//    //                "hibernate.hbm2ddl.auto", "update",
//    //                "hibernate.show_sql", "true",
//    //                "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"
//    //
//    //        ));
//    //        log.debug("Configured jpa dialect : {} ",em.getJpaDialect());
//    //        //log.debug("Configured entity manager factory: {}", em.getNativeEntityManagerFactory());
//    //        log.debug("Manager factory configuration finished successfully.");
//    //        return em;
//    //    }
//    //
//    //    @Bean
//    //    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//    //        return new JpaTransactionManager(entityManagerFactory);
//    //    }
//
//    //    @Bean
//    //    public DataSource dataSource() {
//    //
//    //        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//    //        return builder.setType(EmbeddedDatabaseType.HSQL).build();
//    //    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(true);
//
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan("org.example.entity");
//        factory.setDataSource(dataSource());
//        return factory;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory);
//        return txManager;
//    }
//
//    /** Flyway configuration.*/
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
//
//
//}
