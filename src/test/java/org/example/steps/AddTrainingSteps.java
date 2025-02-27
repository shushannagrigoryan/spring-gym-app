package org.example.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainingCreateRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.services.UpdateTrainerWorkloadSenderService;
import org.example.services.UpdateWorkloadService;
import org.example.utils.JwtTestHelper;
import org.example.utils.TraineeCreationHelper;
import org.example.utils.TrainerCreationHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@RequiredArgsConstructor
public class AddTrainingSteps {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtTestHelper jwtTestHelper;
    private final TraineeCreationHelper traineeCreationHelper;
    private final TrainerCreationHelper trainerCreationHelper;
    private final UpdateWorkloadService updateWorkloadService;
    private final UpdateTrainerWorkloadSenderService updateTrainerWorkloadSenderService;
    private final TrainingCreateRequestDto trainingCreateRequestDto = new TrainingCreateRequestDto();
    private String traineeUsername;
    private String traineePassword;
    private String trainerUsername;
    private String trainingName;
    private String trainingDuration;
    private String trainingDate;
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
        UserDto userDto = traineeCreationHelper.getCreatedUser(firstName, lastName);
        this.traineeUsername = userDto.getUsername();
        this.traineePassword = userDto.getPassword();

        UserDto userDto1 = trainerCreationHelper.getCreatedUser(firstName, lastName);
        this.trainerUsername = userDto1.getUsername();
    }

    @Before(value = "@auth", order = 2)
    public void ensureLoggedIn() throws Exception {
        log.debug("Ensuring logged in");
        jwtToken = jwtTestHelper.getJwtToken(traineeUsername, traineePassword);
    }

    /**
     * Setting the trainingName, trainingDate and trainingDuration for when an authorized user makes a request.
     *
     * @param trainingName     trainingName
     * @param trainingDate     trainingDate
     * @param trainingDuration trainingDuration
     */
    @Given("an authorized user given the traineeUsername, trainerUsername, trainingName {string},"
        + " trainingDate {string}, trainingDuration {string}")
    public void givenTrainingInputData(String trainingName, String trainingDate, String trainingDuration) {
        assertNotNull(jwtToken);
        this.trainingDate = trainingDate;
        this.trainingName = trainingName;
        this.trainingDuration = trainingDuration;
    }

    /**
     * Setting the trainingName, trainingDate and trainingDuration for when an unauthorized user makes a request.
     *
     * @param trainingName     trainingName
     * @param trainingDate     trainingDate
     * @param trainingDuration trainingDuration
     */
    @Given("an unauthorized user given the traineeUsername, trainerUsername, trainingName {string},"
        + " trainingDate {string}, trainingDuration {string}")
    public void traineeWithoutJwtToken(String trainingName, String trainingDate, String trainingDuration) {
        this.trainingDate = trainingDate;
        this.trainingName = trainingName;
        this.trainingDuration = trainingDuration;
    }

    /**
     * Simulates post request to create a new training.
     */
    @When("the user sends a POST request to create a new training")
    public void theUserSubmitsARegistrationRequest() throws Exception {
        trainingCreateRequestDto.setTrainerUsername(trainerUsername);
        trainingCreateRequestDto.setTraineeUsername(traineeUsername);
        trainingCreateRequestDto.setTrainingDate(LocalDateTime.parse(trainingDate));
        trainingCreateRequestDto.setTrainingName(trainingName);
        trainingCreateRequestDto.setTrainingDuration(new BigDecimal(trainingDuration));

        doNothing().when(updateTrainerWorkloadSenderService)
            .send(any(UpdateTrainerWorkloadRequestDto.class));
        doNothing().when(updateWorkloadService).confirmWorkloadUpdate(anyList(), any(ActionType.class));


        response = mockMvc.perform(post("/trainings")
                .header("Authorization", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(trainingCreateRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    }

    /**
     * Simulates adding new training request without jwtToken.
     */
    @When("an unauthorized user sends a POST request to create a new training")
    public void theTraineeSubmitsARegistrationRequestWithoutJwtToken() throws Exception {
        trainingCreateRequestDto.setTrainerUsername(trainerUsername);
        trainingCreateRequestDto.setTraineeUsername(traineeUsername);
        trainingCreateRequestDto.setTrainingDate(LocalDateTime.parse(trainingDate));
        trainingCreateRequestDto.setTrainingName(trainingName);
        trainingCreateRequestDto.setTrainingDuration(new BigDecimal(trainingDuration));

        response = mockMvc.perform(post("/trainings")
                .content(objectMapper.writeValueAsString(trainingCreateRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    }

    /**
     * Simulates post request to create a new training when the TrainerWorkloadService is not responding.
     */
    @When("the user sends a POST request to create a new training, the trainerWorkloadService is not responding")
    public void theUserSubmitsARegistrationRequestButTrainerWorkloadServiceIsNotResponding() throws Exception {
        trainingCreateRequestDto.setTrainerUsername(trainerUsername);
        trainingCreateRequestDto.setTraineeUsername(traineeUsername);
        trainingCreateRequestDto.setTrainingDate(LocalDateTime.parse(trainingDate));
        trainingCreateRequestDto.setTrainingName(trainingName);
        trainingCreateRequestDto.setTrainingDuration(new BigDecimal(trainingDuration));

        doNothing().when(updateTrainerWorkloadSenderService)
            .send(any(UpdateTrainerWorkloadRequestDto.class));

        doThrow(new RuntimeException("Trainer workload service is currently not available."))
            .when(updateWorkloadService).confirmWorkloadUpdate(anyList(), any(ActionType.class));


        response = mockMvc.perform(post("/trainings")
                .header("Authorization", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(trainingCreateRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    }


    /**
     * Asserts that the response status for adding new training matches the expected status code.
     *
     * @param status the expected HTTP status code
     */
    @Then("the create training response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that the right message is present in the response.
     */
    @Then("the response for creating a new training should contain the message {string}")
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
    @Then("the response for creating a new training should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        assertTrue(responseBody.contains(errorMessage));
    }
}
