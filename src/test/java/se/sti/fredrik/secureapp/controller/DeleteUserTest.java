package se.sti.fredrik.secureapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
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

    @BeforeEach
    void setUp() {
        // Clear database before each test
        userRepository.deleteAll();

        // Create test user
        AppUser testUser = new AppUser();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole("USER");
        testUser.setGivenConsent(true);
        AppUser savedUser = userRepository.save(testUser);
        testUserId = savedUser.getId();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUser_WithAdminRole_ShouldRemoveUserFromDatabase() throws Exception {
        assertTrue(userRepository.findById(testUserId).isPresent());

        mockMvc.perform(delete("/admin/delete/{id}", testUserId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findById(testUserId).isPresent());
    }

    @Test
    @WithMockUser(username = "regularuser", roles = {"USER"})
    public void deleteUser_WithUserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/admin/delete/{id}", testUserId)
                        .with(csrf())
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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUser_NonExistingUser_ShouldReturnNotFound() throws Exception {
        Long nonExistingId = 999L;
        mockMvc.perform(delete("/admin/delete/{id}", nonExistingId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}