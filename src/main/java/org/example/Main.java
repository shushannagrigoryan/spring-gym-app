package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = "org.example.entity")
@EnableJpaRepositories(basePackages = "org.example.repositories")
@EnableTransactionManagement
public class Main {
    /**
     * Main method.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);


    }

}