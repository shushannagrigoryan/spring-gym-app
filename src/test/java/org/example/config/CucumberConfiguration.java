package org.example.config;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.example.Main;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureMockMvc
@Slf4j
@CucumberContextConfiguration
@SpringBootTest
@TestPropertySource(properties = "eureka.client.enabled=false")
@ContextConfiguration(classes = Main.class)
public class CucumberConfiguration {
}



