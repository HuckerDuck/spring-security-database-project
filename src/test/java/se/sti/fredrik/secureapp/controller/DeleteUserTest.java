package se.sti.fredrik.secureapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class DeleteUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "password";

    private final String USER_USERNAME = "user";
    private final String USER_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        AppUser testUser = new AppUser();
        testUser.setUsername("victim");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("USER");
        testUser.setGivenConsent(true);
        testUserId = userRepository.save(testUser).getId();

        AppUser admin = new AppUser();
        admin.setUsername(ADMIN_USERNAME);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setRole("ADMIN");
        admin.setGivenConsent(true);
        userRepository.save(admin);

        AppUser regularUser = new AppUser();
        regularUser.setUsername(USER_USERNAME);
        regularUser.setPassword(passwordEncoder.encode(USER_PASSWORD));
        regularUser.setRole("USER");
        regularUser.setGivenConsent(true);
        userRepository.save(regularUser);
    }

    private RequestPostProcessor bearerToken(String token) {
        return request -> {
            request.addHeader("Authorization", "Bearer " + token);
            return request;
        };
    }

    @Test
    public void deleteUser_WithAdminRole_ShouldRemoveUserFromDatabase() throws Exception {
        String token = TestLoginHelper.obtainAccessToken(ADMIN_USERNAME, ADMIN_PASSWORD, mockMvc);

        assertTrue(userRepository.findById(testUserId).isPresent());

        mockMvc.perform(delete("/admin/delete/{id}", testUserId)
                        .with(csrf())
                        .with(bearerToken(token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findById(testUserId).isPresent());
    }

    @Test
    public void deleteUser_WithUserRole_ShouldReturnForbidden() throws Exception {
        String token = TestLoginHelper.obtainAccessToken(USER_USERNAME, USER_PASSWORD, mockMvc);

        mockMvc.perform(delete("/admin/delete/{id}", testUserId)
                        .with(csrf())
                        .with(bearerToken(token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUser_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/admin/delete/{id}", testUserId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteUser_NonExistingUser_ShouldReturnNotFound() throws Exception {
        String token = TestLoginHelper.obtainAccessToken(ADMIN_USERNAME, ADMIN_PASSWORD, mockMvc);

        Long nonExistingId = 999L;
        mockMvc.perform(delete("/admin/delete/{id}", nonExistingId)
                        .with(csrf())
                        .with(bearerToken(token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
