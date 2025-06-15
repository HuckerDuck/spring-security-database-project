package se.sti.fredrik.secureapp.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Utility class for handling authentication for tests
 * Provides methods to format JSON and obtain access tokens via the login endpoint
 */
public class TestLoginHelper {

    /**
     * Builds a JSON string representing a login body request
     *
     * @param username the username to format in the login request
     * @param password the password to format in the login request
     * @return a JSON string with the correct formatting
     */
    public static String buildLoginJson(String username, String password) {
        return """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);
    }

    /**
     * Performs a login request at the authentication endpoint using MockMvc
     * and obtains a Jwt access token from the response
     * <p>Uses {@link #buildLoginJson} to format</p>
     *
     * @param username the username to authenticate with
     * @param password the password to authenticate with
     * @param mockMvc the MockMvc instance to perform the HTTP request
     * @return the Jwt access token as a string
     * @throws Exception if the login request fails
     */
    public static String obtainAccessToken(String username, String password, MockMvc mockMvc) throws Exception {
        String login = buildLoginJson(username, password);

        MvcResult result = mockMvc.perform(post("/auth/request-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }
}
