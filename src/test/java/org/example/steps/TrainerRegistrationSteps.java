package org.example.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.services.TrainerService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class TrainerRegistrationSteps {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TrainerService trainerService;
    private MvcResult response;
    private final TrainerCreateRequestDto trainerCreateRequestDto  = new TrainerCreateRequestDto();

    /**
     * Initializes a trainer with the given first name, last name, and specialization.
     *
     * @param firstName     the first name of the trainer
     * @param lastName      the last name of the trainer
     * @param specialization the specialization of the trainer
     */
    @Given("a trainer with first name {string} and last name {string} and specialization {string}")
    public void trainerWithFirstNameAndLastName(String firstName, String lastName, String specialization) {
        log.debug("firstName {}", firstName);
        log.debug("lastName {}", lastName);
        log.debug("specialization {}", specialization);
        trainerCreateRequestDto.setFirstName(firstName);
        trainerCreateRequestDto.setLastName(lastName);
        trainerCreateRequestDto.setSpecialization(specialization);
    }

    /**
     * Initializes a trainer with the given first name and last name.
     *
     * @param firstName     the first name of the trainer
     * @param lastName      the last name of the trainer
     */
    @Given("a trainer with first name {string} and last name {string} and missing specialization")
    public void trainerWithFirstNameAndLastNameAndMissingSpecialization(String firstName, String lastName) {
        log.debug("firstName {}", firstName);
        log.debug("lastName {}", lastName);
        trainerCreateRequestDto.setFirstName(firstName);
        trainerCreateRequestDto.setLastName(lastName);
    }

    /**
     * Initializes a trainer with the given first name, last name, and invalid specialization.
     *
     * @param firstName     the first name of the trainer
     * @param lastName      the last name of the trainer
     * @param specialization the invalid specialization of the trainer
     */
    @Given("a trainer with first name {string} and last name {string} and invalid specialization {string}")
    public void trainerWithFirstNameAndLastNameAndInvalidSpecialization(
        String firstName, String lastName, String specialization) {
        log.debug("firstName {}", firstName);
        log.debug("lastName {}", lastName);
        log.debug("specialization {}", specialization);
        trainerCreateRequestDto.setFirstName(firstName);
        trainerCreateRequestDto.setLastName(lastName);
        trainerCreateRequestDto.setSpecialization(specialization);
    }

    /**
     * Initializes a trainer with the given empty first name.
     */
    @Given("a trainer with an empty first name")
    public void trainerWithAnEmptyFirstName() {
        trainerCreateRequestDto.setFirstName("");
        trainerCreateRequestDto.setLastName("B");
    }

    /**
     * Simulates trainee registration request.
     */
    @When("the trainer submits a registration request")
    public void theTrainerSubmitsARegistrationRequest() throws Exception {
        Mockito.when(trainerService.registerTrainer(Mockito.any()))
            .thenReturn(new TrainerResponseDto("A.B", "password"));


        response = mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerCreateRequestDto)))
            .andReturn();
    }

    /**
     * Asserts that the trainee registration response status matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        Assertions.assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that the trainee username and password are present in the response.
     */
    @Then("the response should contain a generated username and password")
    public void theResponseShouldContainAGeneratedUsernameAndPassword() throws Exception {
        String responseBody = response.getResponse().getContentAsString();

        ResponseDto<TrainerResponseDto> responseDto = objectMapper.readValue(responseBody,
            objectMapper.getTypeFactory().constructParametricType(ResponseDto.class, TrainerResponseDto.class));

        TrainerResponseDto trainerResponseDto = responseDto.getPayload();

        Assertions.assertNotNull(trainerResponseDto.getUsername());
        Assertions.assertNotNull(trainerResponseDto.getPassword());

        Assertions.assertEquals("A.B", trainerResponseDto.getUsername());
        Assertions.assertEquals("password", trainerResponseDto.getPassword());
    }

    @Then("the response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(errorMessage));
    }
}