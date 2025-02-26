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

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private static String jwtToken; // Storing JWT for reuse

    /**
     * Logs in and retrieves a JWT token. If already logged in, returns the existing token.
     */
    public String getJwtToken() throws Exception {
        if (jwtToken == null) {
            performLogin();
        }
        return jwtToken;
    }

    /**
     * Performs the login and stores the token.
     */
    public void performLogin() throws Exception {
        log.debug("performing login");
        MvcResult loginResponse = mockMvc.perform(MockMvcRequestBuilders.get("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("username", "T.D4")
                .header("password", "xsTzndddTm"))
            .andReturn();

        String responseBody = loginResponse.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        jwtToken = jsonNode.get("payload").asText();

        log.debug("Extracted JWT Token: {}", jwtToken);
    }
}
