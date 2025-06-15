package se.sti.fredrik.secureapp.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import se.sti.fredrik.secureapp.DTO.AppUserDTO;
import se.sti.fredrik.secureapp.Model.AppUser;
import se.sti.fredrik.secureapp.Model.LoginRequest;
import se.sti.fredrik.secureapp.Repository.AppUserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

//? ActiveProfiles gör att den automatiskt tar application-test.properties
@ActiveProfiles("test")

@AutoConfigureMockMvc

//? Med denna så tas allt bort automatiskt efter varje test.
//? Behövs ingen @BeforeEach .deleteAll()
@Transactional
class RegisterUserTest {

    @Autowired
    //? Skapar HTTP-anrop i testerna
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository userRepository;

    //? Behövs två token för testen
    //? En för Admin och en för User
    private String adminToken;
    private String userToken;

    //? Hjälpmetod för att använda request-token
    private String getAToken(String username, String password) throws Exception {
        MvcResult mvcResult = mockMvc
                         //! Skickar ett HTTP post anrop mot min authcontrollerr mot addressen request token
                        .perform(post("/auth/request-token")
                         //! Lägger till att mediatypen som vi skickar är av typen JSON
                        .contentType(MediaType.APPLICATION_JSON)
                         //! Skapar ett nytt objekt för inloggningen som innehåller ett användarnamn och en lösenor
                                //!
                        .content(objectMapper.writeValueAsString( new LoginRequest(username, password)
                        )))
                //! Vi förväntar oss att allt har gått OK, alltså att allt har gått som det ska göra
                .andExpect(status().isOk())
                //! Efter det så skickar vi med allt
                .andReturn();

        //! Detta returnerar sedan hela JWT strängen som vi använder längre ner under
        //! ".header("Authorization", "Bearer " + adminToken)"
        return mvcResult.getResponse().getContentAsString();

    }

    //? Metod för att skapa en vanlig user för att minska koden nedan:
    private void createAppUserWithOutDTO(String username, String password, String role) {
        AppUser templateUser = new AppUser();
        templateUser.setUsername(username);
        //? Hashar Lösenordet
        templateUser.setPassword(passwordEncoder.encode(password));
        templateUser.setRole(role);
        templateUser.setGivenConsent(true);
        userRepository.save(templateUser);
    }

    //? Metod för att skapa en DTO användare för att minska koden nedan:
    private AppUserDTO createAppUserWithDTO(String username, String password, String role) {
        AppUserDTO templateDTOUser = new AppUserDTO();
        templateDTOUser.setUsername(username);
        //? Hashar Lösenordet
        templateDTOUser.setPassword(password);
        templateDTOUser.setRole(role);
        templateDTOUser.setGivenConsent(true);
        return templateDTOUser;
    }

    @BeforeEach
    void setUpWithJWT() throws Exception {
        //? Ser till att userRepotot är tomt när vi kör testerna för att undvika problem
        userRepository.deleteAll();
        createAppUserWithOutDTO("admin", "adminPassword123", "ADMIN");
        createAppUserWithOutDTO("user", "userPassword123", "USER");

        this.adminToken = getAToken("admin", "adminPassword123");
        this.userToken = getAToken("user", "userPassword123");

        System.out.println();
        System.out.println(adminToken);
        System.out.println("Admin token skapat");
        System.out.println();
    }

    @Test
    void registerUser_WithAdminRole_ShouldReturnCreated() throws Exception {
        AppUserDTO dto = createAppUserWithDTO("Håkan", "A1b2c3!@", "USER");

        //? Kolla att användaren är skapad - status = .isCreated()
        mockMvc.perform(post("/admin/register")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                //? Denna ska ge 201 om det är ett okej test
                .andExpect(status().isCreated());

        //? Kolla om användaren Håkan är sparad

        AppUser savedUser = userRepository.findByUsername("Håkan");
        assertNotNull(savedUser, "Finns användaren i databasen? - Ja det gör den");

        System.out.println();
        System.out.println();
        System.out.println("Testet lyckades då vi kom in i databasen och la till en användare med användarnamnet Håkan ");
        System.out.println();
        System.out.println();

    }

    @Test
    void registerUser_WithUserRole_ShouldNotWork() throws Exception {
        AppUserDTO dto = createAppUserWithDTO("Kalle", "P4ssword!", "USER");

        mockMvc.perform(post("/admin/register")
                         //! Skicka med allt som behövs för authentiseringen för JWT och då också med en token
                        //! Däremot är token av typen user vilket inte har rättigheter att lägga in saker i databasen
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                //! Denna borde faila då användaren ej har de rättigheter som behövs för att kunna komma in och
                //! posta i våran databas
                .andExpect(status().isForbidden());

        System.out.println();
        System.out.println();
        System.out.println("Testet lyckades då användaren ej kom in i databasen via JWT, behövs en admin roll för det");
        System.out.println();
        System.out.println();
    }

    @Test
    void registerUser_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        AppUserDTO dto = createAppUserWithDTO("Peter", "XyZ!234", "USER");

        mockMvc.perform(post("/admin/register")
                         //! Märk väl här att vi inte skickar med någon .header med authorization, bearer och en admin token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        System.out.println();
        System.out.println();
        System.out.println("Testet lyckades då användaren inte är authentiserad att komma in i basen. " +
                "Här finns ett krav på authentisering via JWT som vi ej skickar med. ");
        System.out.println();
        System.out.println();
    }
}
