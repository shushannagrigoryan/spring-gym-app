package org.example.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TraineeCreateRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.dto.responsedto.TraineeResponseDto;
import org.example.services.TraineeService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
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
    private final TraineeService traineeService;

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
        Mockito.when(traineeService.registerTrainee(Mockito.any()))
            .thenReturn(new TraineeResponseDto("A.B", "password"));


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
        Assertions.assertEquals(status, response.getResponse().getStatus());
    }

    /**
     * Asserts that the trainee username and password are present in the response.
     */
    @Then("the traineeRegistration response should contain a generated username and password")
    public void theResponseShouldContainAGeneratedUsernameAndPassword() throws Exception {

        String responseBody = response.getResponse().getContentAsString();

        ResponseDto<TraineeResponseDto> responseDto = objectMapper.readValue(responseBody,
            objectMapper.getTypeFactory().constructParametricType(ResponseDto.class, TraineeResponseDto.class));

        TraineeResponseDto traineeResponseDto = responseDto.getPayload();


        Assertions.assertNotNull(traineeResponseDto.getUsername());
        Assertions.assertNotNull(traineeResponseDto.getPassword());

        Assertions.assertEquals("A.B", traineeResponseDto.getUsername());
        Assertions.assertEquals("password", traineeResponseDto.getPassword());
    }

    /**
     * Asserts that the trainee registration response contains the expected error message.
     *
     * @param errorMessage the expected error message in the response body
     */
    @Then("the traineeRegistration response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) throws Exception {
        String responseBody = response.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(errorMessage));
    }
}
