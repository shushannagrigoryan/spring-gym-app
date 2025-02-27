package org.example.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.dto.requestdto.ChangePasswordRequestDto;
import org.example.utils.JwtTestHelper;
import org.example.utils.TraineeCreationHelper;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class ChangeUserPasswordSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TraineeCreationHelper traineeCreationHelper;
    private final JwtTestHelper jwtTestHelper;
    private MvcResult response;
    private String username;
    private String password;
    private UserDto userDto;
    private String jwtToken;

    /**
     * Before changePassword request ensure that the user exists in db.
     */
    @Before(value = "@createUser", order = 1)
    public void ensureUserExists() throws Exception {
        log.debug("Ensuring user exists");
        String firstName = "A";
        String lastName = "C";
        userDto = traineeCreationHelper.getCreatedUser(firstName, lastName); //Ensure user exists in db.
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
    }

    /**
     * Ensure a valid jwtToken exists for the user.
     */
    @Before(value = "@auth", order = 2)
    public void ensureLoggedIn() throws Exception {
        log.debug("Ensuring logged in");
        jwtToken = jwtTestHelper.getJwtToken(username, password);
    }


    /**
     * Given a user with valid username and password.
     */
    @Given("a user with valid username and password")
    public void userWithUsernameAndPassword() {
        assertNotNull(userDto);
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
    }

    /**
     * Simulates put request to change password.
     */
    @When("the user submits a put request to change password")
    public void theUserSubmitsAChangePasswordRequest() throws Exception {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setPassword(password);
        changePasswordRequestDto.setNewPassword("newPassword");
        response = mockMvc.perform(put("/users/{username}/password", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequestDto))
                .header("Authorization", "Bearer " + jwtToken))
            .andReturn();
    }

    /**
     * Simulates put request to change password.
     */
    @When("the user submits a put request without jwtToken")
    public void theUserSubmitsAChangePasswordRequestWithoutJwt() throws Exception {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setPassword(password);
        changePasswordRequestDto.setNewPassword("newPassword");
        response = mockMvc.perform(put("/users/{username}/password", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequestDto)))
            .andReturn();
    }

    /**
     * Asserts that the response status for the changePassword request  matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the changePassword response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that the correct message is present in the response.
     */
    @Then("the changePassword response should contain a message {string}")
    public void theResponseShouldContainMessage(String expectedMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String message = jsonNode.get("message").asText();
        assertEquals(expectedMessage, message);
    }

    /**
     * Asserts that the response for changePassword request contains the expected error message.
     *
     * @param errorMessage the expected error message in the response body
     */
    @Then("the changePassword response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(errorMessage));
    }
}
