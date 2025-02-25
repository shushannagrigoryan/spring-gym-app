package org.example.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeCreateRequestDto;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class TraineeRegistrationSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private MvcResult response;
    private final TraineeCreateRequestDto traineeCreateRequestDto = new TraineeCreateRequestDto();

    @Given("a trainee with first name {string} and last name {string}")
    public void traineeWithFirstNameAndLastName(String firstName, String lastName) {
        traineeCreateRequestDto.setFirstName(firstName);
        traineeCreateRequestDto.setLastName(lastName);
    }

    @Given("a trainee with an empty first name")
    public void traineeWithAnEmptyFirstName() {
        traineeCreateRequestDto.setFirstName("");
        traineeCreateRequestDto.setLastName("B");
    }

    /**
     * Simulates trainee registration request.
     */
    @When("the trainee submits a registration request")
    public void theTraineeSubmitsARegistrationRequest() throws Exception {
        response = mockMvc.perform(post("/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeCreateRequestDto)))
            .andReturn();
    }

    /**
     * Asserts that the trainee registration response status matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the traineeRegistration response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that the trainee username and password are present in the response.
     */
    @Then("the traineeRegistration response should contain a generated username and password")
    public void theResponseShouldContainAGeneratedUsernameAndPassword() throws Exception {

        String responseBody = response.getResponse().getContentAsString();
        log.debug("ResponseBody = {}", responseBody);

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        JsonNode payloadNode = jsonNode.get("payload");
        String username = payloadNode.get("username").asText();
        String password = payloadNode.get("password").asText();

        assertNotNull(username);
        assertNotNull(password);
        assertTrue(username.startsWith(traineeCreateRequestDto.getFirstName().concat(".")
            .concat(traineeCreateRequestDto.getLastName())));
    }

    /**
     * Asserts that the trainee registration response contains the expected error message.
     *
     * @param errorMessage the expected error message in the response body
     */
    @Then("the traineeRegistration response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        assertTrue(responseBody.contains(errorMessage));
    }
}
