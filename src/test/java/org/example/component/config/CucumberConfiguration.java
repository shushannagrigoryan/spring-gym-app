package org.example.component.config;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.example.Main;
import org.example.services.GetWorkloadService;
import org.example.services.TrainerWorkloadSenderService;
import org.example.services.UpdateTrainerWorkloadSenderService;
import org.example.services.UpdateWorkloadService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureMockMvc
@Slf4j
@CucumberContextConfiguration
@SpringBootTest
@TestPropertySource(properties = "eureka.client.enabled=false")
@ContextConfiguration(classes = Main.class)
@MockBean(UpdateWorkloadService.class)
@MockBean(UpdateTrainerWorkloadSenderService.class)
@MockBean(TrainerWorkloadSenderService.class)
@MockBean(GetWorkloadService.class)
public class CucumberConfiguration {
}



