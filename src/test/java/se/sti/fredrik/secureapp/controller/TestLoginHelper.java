package se.sti.fredrik.secureapp.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestLoginHelper {
    public static String buildLoginJson(String username, String password) {
        return """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);
    }

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
