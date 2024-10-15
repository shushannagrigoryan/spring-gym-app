package org.example.config;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.AuthInterceptor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.example")
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    /** Setting dependencies. */
    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/trainees/**", "/trainers/**", "/users/**", "/training-types/**", "/trainings/**")
                .excludePathPatterns("/trainees/register", "/trainers/register");

    }

    /**
     * Flyway configuration.
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.repair();
        flyway.migrate();
        log.info("Flyway configuration completed successfully");
        return flyway;
    }

}
