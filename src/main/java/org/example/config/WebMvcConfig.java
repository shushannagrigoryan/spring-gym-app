package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EntityScan(basePackages = "org.example.entity")
@EnableJpaRepositories(basePackages = "org.example.repositories")
@EnableTransactionManagement
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * OpenApi config.
     */
    @Bean
    public OpenAPI customOpenApi() {
        log.debug("Configuring Custom OpenApi");
        return new OpenAPI()
            .servers(List.of(
                    new Server().url("http://localhost:8080/gym")
                )
            )
            .info(
                new Info().title("Gym Api.")
                    .description("Documentation for Gym Api.")
                    .version("1.0.0")
            );
    }
}
