package com.ds3.team8.users_service.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ds3.team8.users_service.dtos.AuthRequest;
import com.ds3.team8.users_service.dtos.AuthResponse;
import com.ds3.team8.users_service.dtos.ForgotPasswordRequest;
import com.ds3.team8.users_service.dtos.PasswordResetRequest;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.services.IAuthService;
import com.ds3.team8.users_service.services.IPasswordResetTokenService;
import com.ds3.team8.users_service.enums.Role;

public class AuthControllerTest {
    private IAuthService authService;
    private IPasswordResetTokenService passwordResetTokenService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(IAuthService.class);
        passwordResetTokenService = mock(IPasswordResetTokenService.class);
        authController = new AuthController(authService, passwordResetTokenService);
    }

    @Test
    void register_shouldReturnCreatedUser() {
        UserRequest request = new UserRequest();
        request.setFirstName("Diego");
        request.setLastName("Tolosa");
        request.setEmail("diego@example.com");
        request.setPassword("Test@123");
        request.setConfirmPassword("Test@123");
        request.setPhone("+573001112233");
        request.setAddress("Calle 123");
        request.setRole(Role.CUSTOMER);

        UserResponse response = new UserResponse(1L, "Diego", "Tolosa", "diego@example.com", "+573001112233", "Calle 123", Role.CUSTOMER, null, null);

        when(authService.register(request)).thenReturn(response);

        ResponseEntity<UserResponse> result = authController.register(request);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void login_shouldReturnAuthResponse() {
        AuthRequest request = new AuthRequest("diego@example.com", "Test@123");
        AuthResponse response = new AuthResponse("fake-jwt-token");

        when(authService.login(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.login(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("fake-jwt-token", result.getBody().getToken());
    }

    @Test
    void sendPasswordResetEmail_shouldReturnOkMessage() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("diego@example.com");

        doNothing().when(passwordResetTokenService).sendPasswordResetEmail(request);

        ResponseEntity<String> result = authController.sendPasswordResetEmail(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Correo de restablecimiento de contraseña enviado.", result.getBody());
    }

    @Test
    void resetPassword_shouldReturnSuccessMessage() {
        PasswordResetRequest request = new PasswordResetRequest("token123", "NewPassword@123", "NewPassword@123");

        doNothing().when(passwordResetTokenService).resetPassword(request);

        ResponseEntity<String> result = authController.resetPassword(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Contraseña restablecida correctamente.", result.getBody());
    }

    @Test
    void showResetPasswordMessage_shouldReturnMessage() {
        ResponseEntity<String> result = authController.showResetPasswordMessage();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Restablecer la contraseña del usuario.", result.getBody());
    }
}
