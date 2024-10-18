package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SwaggerConfig {
    /**
     * config.
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
                );
    }

}
