package org.example.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.dto.requestdto.TrainerCreateRequestDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerCreationHelper {

    private static UserDto userDto;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TrainerCreateRequestDto trainerCreateRequestDto = new TrainerCreateRequestDto();

    /**
     * Created a trainer and returns a {@code UserDto }. If user already exists, returns the existing trainer.
     */
    public UserDto getCreatedUser(String firstName, String lastName) throws Exception {
        log.debug("createUser method");
        createUser(firstName, lastName);
        return userDto;
    }

    /**
     * Performs the login and stores the token.
     */
    public void createUser(String firstName, String lastName) throws Exception {
        log.debug("performing login");
        trainerCreateRequestDto.setFirstName(firstName);
        trainerCreateRequestDto.setLastName(lastName);
        trainerCreateRequestDto.setSpecialization("1");
        MvcResult userCreationResponse = mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerCreateRequestDto)))
            .andReturn();

        String responseBody = userCreationResponse.getResponse().getContentAsString();
        log.debug("RESPONSE = {}", responseBody);
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode payloadNode = jsonNode.get("payload");
        userDto = new UserDto(payloadNode.get("username").asText(), payloadNode.get("password").asText());

        log.debug("Generated user: {}", userDto);
    }
}
