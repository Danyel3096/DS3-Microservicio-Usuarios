package com.ds3.team8.users_service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    void registerUser_success() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Diego");
        userRequest.setLastName("Tolosa");
        userRequest.setEmail("diego@example.com");
        userRequest.setPassword("Test@1234");
        userRequest.setConfirmPassword("Test@1234");
        userRequest.setPhone("+573001112233");
        userRequest.setAddress("Calle 123");
        userRequest.setRole(Role.CUSTOMER);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("diego@example.com"))
                .andExpect(jsonPath("$.firstName").value("Diego"))
                .andExpect(jsonPath("$.lastName").value("Tolosa"));
    }

    @Test
    @Order(2)
    void registerUser_fails_whenEmailExists() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Diego");
        userRequest.setLastName("Tolosa");
        userRequest.setEmail("diego@example.com"); // mismo correo que en el test anterior
        userRequest.setPassword("Test@1234");
        userRequest.setConfirmPassword("Test@1234");
        userRequest.setPhone("+573001112233");
        userRequest.setAddress("Calle 123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Correo ya registrado"));
    }

    @Test
    @Order(3)
    void registerUser_fails_whenPasswordsDoNotMatch() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Ana");
        userRequest.setLastName("Pérez");
        userRequest.setEmail("ana@example.com");
        userRequest.setPassword("Test@1234");
        userRequest.setConfirmPassword("Test@0000"); // contraseña no coincide
        userRequest.setPhone("+573001112234");
        userRequest.setAddress("Calle 124");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Las contraseñas no coinciden"));
    }

    @Test
    @Order(4)
    void registerUser_fails_whenValidationFails() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName(""); // inválido
        userRequest.setLastName("");
        userRequest.setEmail("no-es-un-correo");
        userRequest.setPassword("123");
        userRequest.setConfirmPassword("123");
        userRequest.setPhone("123");
        userRequest.setAddress("");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.email").value("El campo 'email' debe ser un correo electrónico válido"))
                .andExpect(jsonPath("$.errors.password").exists());
    }
}
