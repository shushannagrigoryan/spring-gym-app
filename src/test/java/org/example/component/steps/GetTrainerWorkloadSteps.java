package org.example.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.component.dto.UserDto;
import org.example.component.utils.JwtTestHelper;
import org.example.component.utils.TrainerCreationHelper;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.services.GetWorkloadService;
import org.example.services.TrainerWorkloadSenderService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class GetTrainerWorkloadSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtTestHelper jwtTestHelper;
    private final TrainerCreationHelper trainerCreationHelper;
    private final GetWorkloadService getWorkloadService;
    private final TrainerWorkloadSenderService trainerWorkloadSenderService;
    private String trainerUsername;
    private String trainerPassword;

    private String trainingYear;
    private String trainingMonth;
    private MvcResult response;
    private String jwtToken;

    /**
     * Ensure that the user exists in db.
     */
    @Before(value = "@createUser", order = 1)
    public void ensureUserExists() throws Exception {
        log.debug("Ensuring user exists");
        String firstName = "A";
        String lastName = "C";

        UserDto userDto1 = trainerCreationHelper.getCreatedUser(firstName, lastName);
        this.trainerUsername = userDto1.getUsername();
        this.trainerPassword = userDto1.getPassword();
    }

    @Before(value = "@auth", order = 2)
    public void ensureLoggedIn() throws Exception {
        log.debug("Ensuring logged in");
        jwtToken = jwtTestHelper.getJwtToken(trainerUsername, trainerPassword);
    }

    /**
     * Setting the trainingYear and trainingMonth for when an authorized user makes a request.
     *
     * @param trainingYear  trainingYear
     * @param trainingMonth trainingMonth
     */
    @Given("an authorized user and given the trainerUsername, trainingYear {string} and trainingMonth {string}")
    public void givenTrainingInputData(String trainingYear, String trainingMonth) {
        assertNotNull(jwtToken);
        this.trainingYear = trainingYear;
        this.trainingMonth = trainingMonth;
    }

    /**
     * Setting the trainingYear and trainingMonth for when an unauthorized user makes a request.
     *
     * @param trainingYear  trainingYear
     * @param trainingMonth trainingMonth
     */
    @Given("an unauthorized user and given the trainerUsername, trainingYear {string} and trainingMonth {string}")
    public void givenTrainingInputDataAndAnUnAuthorizedUser(String trainingYear, String trainingMonth) {
        this.trainingYear = trainingYear;
        this.trainingMonth = trainingMonth;
    }

    /**
     * Simulates get request to get trainer;s workload.
     */
    @When("the user sends a GET request to retrieve trainer's workload")
    public void theUserSubmitsAGetRequestToRetrieveTrainerWorkload() throws Exception {

        doNothing().when(trainerWorkloadSenderService)
            .send(any(TrainerWorkloadRequestDto.class));

        GetTrainerWorkloadResponseDto getTrainerWorkloadResponseDto = new GetTrainerWorkloadResponseDto(
            trainerUsername, trainingYear, trainingMonth, BigDecimal.valueOf(60));
        ResponseEntity<ResponseDto<GetTrainerWorkloadResponseDto>> responsePayload =
            ResponseEntity.ok(new ResponseDto<>(getTrainerWorkloadResponseDto,
                "Successfully retrieved trainer's workload"));

        doReturn(responsePayload).when(getWorkloadService).getWorkload(any(TrainerWorkloadRequestDto.class));


        response = mockMvc.perform(get("/trainers/workload")
                .param("username", trainerUsername)
                .param("year", trainingYear)
                .param("month", trainingMonth)
                .header("Authorization", "Bearer " + jwtToken))
            .andReturn();
    }

    /**
     * Simulates a GET request to retrieve trainer's workload without jwtToken.
     */
    @When("an unauthorized user sends a POST request to retrieve trainer's workload")
    public void theTraineeSubmitsARegistrationRequestWithoutJwtToken() throws Exception {
        response = mockMvc.perform(get("/trainers/workload")
                .param("username", trainerUsername)
                .param("year", trainingYear)
                .param("month", trainingMonth))
            .andReturn();
    }


    /**
     * Asserts that the response status for adding new training matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the response status for getting trainer's workload should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that the right payload is present in the response.
     */
    @Then("the response for getting trainer's workload should contain trainer's username, year, month, and workload")
    public void theResponseShouldContainTheWorkload() throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode payload = jsonNode.get("payload");
        String username = payload.get("username").asText();
        String month = payload.get("month").asText();
        String year = payload.get("year").asText();
        String workload = payload.get("workload").asText();
        assertEquals(username, trainerUsername);
        assertEquals(month, trainingMonth);
        assertEquals(year, trainingYear);
        assertNotNull(workload);
    }

    /**
     * Asserts that the right message is present in the response.
     */
    @Then("the response for getting trainer's workload should contain the message {string}")
    public void theResponseShouldContainTheMessage(String expectedMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String responseMessage = jsonNode.get("message").asText();

        assertEquals(expectedMessage, responseMessage);
    }

    /**
     * Asserts that the response for getting trainingTypes request contains the expected error message.
     *
     * @param errorMessage the expected error message in the response body
     */
    @Then("the response for getting trainer's workload should contain the error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        assertTrue(responseBody.contains(errorMessage));
    }
}
