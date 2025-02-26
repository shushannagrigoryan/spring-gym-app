package org.example.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.JwtTestHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class TrainingTypesSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private MvcResult response;
    private String jwtToken;
    private final JwtTestHelper jwtTestHelper;

    @Before("@auth")
    public void ensureLoggedIn() throws Exception {
        log.debug("Ensuring logged in");
        jwtToken = jwtTestHelper.getJwtToken();
    }

    @Given("the user has a valid JWT token")
    public void traineeWithValidJwtToken() {
        assertNotNull(jwtToken);
    }

    @Given("the user does not have a JWT token")
    public void traineeWithoutJwtToken() {

    }

    /**
     * Simulates get trainingTypes request with valid jwtToken.
     */
    @When("the user sends a GET request to get training types with the JWT token")
    public void theTraineeSubmitsARegistrationRequest() throws Exception {
        response = mockMvc.perform(get("/trainingTypes")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();
    }

    /**
     * Simulates get trainingTypes request without jwtToken.
     */
    @When("the user sends a GET request to get training types without the JWT token")
    public void theTraineeSubmitsARegistrationRequestWithoutJwtToken() throws Exception {
        response = mockMvc.perform(get("/trainingTypes")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();
    }

    /**
     * Asserts that the response status for getting trainingTypes matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the getTrainingTypes response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that an empty list is present in the response.
     */
    @Then("the response should contain a list of training types and message {string}")
    public void theResponseShouldContainAListOfTrainingTypes(String expectedMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        log.debug("ResponseBody = {}", responseBody);

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        JsonNode payloadNode = jsonNode.get("payload");
        String responseMessage = jsonNode.get("message").asText();

        assertNotNull(payloadNode);
        assertEquals(expectedMessage, responseMessage);
    }

    /**
     * Asserts that the response for getting trainingTypes request contains the expected error message.
     *
     * @param errorMessage the expected error message in the response body
     */
    @Then("the get trainingTypes response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        assertTrue(responseBody.contains(errorMessage));
    }
}
