package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.AuthInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.example")
@Slf4j
@EnableJpaRepositories(basePackages = "org.example.repositories")
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    /** Setting dependencies. */
    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/trainees/**", "/trainers/**", "/users/**", "/trainingTypes/**", "/trainings/**");
    }

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


    /** config.*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");

        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

}
