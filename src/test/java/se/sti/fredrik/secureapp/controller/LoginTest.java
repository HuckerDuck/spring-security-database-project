package se.sti.fredrik.secureapp.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;


import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        appUserRepository.deleteAll();
        AppUser testUser = new AppUser();
        testUser.setUsername("test");
        testUser.setPassword(passwordEncoder.encode("Password123--"));
        testUser.setRole("ADMIN");
        testUser.setGivenConsent(true);
        appUserRepository.save(testUser);
    }

    @Test
    void JwtTokenOnSuccessfulLogin() throws Exception {
        String login = """
                {
                    "username": "test",
                    "password": "Password123--"
                }
                """;

        mockMvc.perform(post("/auth/request-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(emptyOrNullString())));
    }

    @Test
    void UnauthorizedOnWrongCredentials() throws Exception {
        String login = """
                {
                    "username": "test",
                    "password": "wrongPassword123--"
                }
                """;

        mockMvc.perform(post("/auth/request-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isUnauthorized());
    }
}
