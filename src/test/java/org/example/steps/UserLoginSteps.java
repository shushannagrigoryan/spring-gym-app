package org.example.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class UserLoginSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private MvcResult response;
    private String username;
    private String password;

    @Given("a user with username {string} and password {string}")
    public void userWithUsernameAndPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Simulates user login request.
     */
    @When("the user submits a login request")
    public void theUserSubmitsALoginRequest() throws Exception {
        response = mockMvc.perform(get("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("username", username)
                .header("password", password)
                .remoteAddress(LocalDateTime.now().toString()))
            .andReturn();
    }

    /**
     * Asserts that the user login response status matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the login response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        Assertions.assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that a jwtToken is present in the response.
     */
    @Then("the login response should contain a generated jwtToken")
    public void theResponseShouldContainAGeneratedJwtToken() throws Exception {

        String responseBody = response.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String payload = jsonNode.get("payload").asText();
        String message = jsonNode.get("message").asText();

        assertNotNull(payload);
        assertEquals("Successfully logged in.", message);
    }

    /**
     * Asserts that the user login response contains the expected error message.
     *
     * @param errorMessage the expected error message in the response body
     */
    @Then("the login response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(errorMessage));
    }
}
