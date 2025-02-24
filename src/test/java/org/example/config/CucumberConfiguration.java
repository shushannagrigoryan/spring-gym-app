package org.example.config;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.example.Main;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

//@AutoConfigureMockMvc
//@CucumberContextConfiguration
//@SpringBootTest

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@Slf4j
//@TestPropertySource(properties = "eureka.client.enabled=false")

@AutoConfigureMockMvc
@Slf4j
@TestPropertySource(properties = "eureka.client.enabled=false")
@CucumberContextConfiguration
@MockBean(TraineeService.class)
@MockBean(TrainerService.class)


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@SpringBootTest
//@AutoConfigureMockMvc
@TestPropertySource(properties = "eureka.client.enabled=false")
@ContextConfiguration(classes = Main.class)
public class CucumberConfiguration {
}



