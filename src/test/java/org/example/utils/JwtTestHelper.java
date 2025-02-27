package org.example.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTestHelper {

    private static String jwtToken;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    /**
     * Logs in and retrieves a JWT token. If already logged in, returns the existing token.
     */
    public String getJwtToken(String username, String password) throws Exception {
        performLogin(username, password);
        return jwtToken;
    }

    /**
     * Performs the login and stores the token.
     */
    public void performLogin(String username, String password) throws Exception {
        MvcResult loginResponse = mockMvc.perform(MockMvcRequestBuilders.get("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("username", username)
                .header("password", password))
            .andReturn();

        String responseBody = loginResponse.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        jwtToken = jsonNode.get("payload").asText();

        log.debug("Extracted JWT Token: {}", jwtToken);
    }
}
