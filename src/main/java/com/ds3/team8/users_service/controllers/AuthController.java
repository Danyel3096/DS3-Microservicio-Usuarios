package com.ds3.team8.users_service.controllers;

import com.ds3.team8.users_service.dtos.*;
import com.ds3.team8.users_service.services.IAuthService;
import com.ds3.team8.users_service.services.IPasswordResetTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/auth") // Indica la URL base para acceder a los servicios de esta clase
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    private final IAuthService authService;
    private final IPasswordResetTokenService passwordResetTokenService;

    public AuthController(IAuthService authService, IPasswordResetTokenService passwordResetTokenService) {
        this.authService = authService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    // Registrar usuario
    @Operation(summary = "Registrar un usuario", description = "Registrar un usuario en el sistema.", security = {})
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest) {
        UserResponse savedUser = authService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Inicio de sesión de usuarios
    @Operation(summary = "Inicio de sesión de usuario", description = "Inicio de sesión de usuarios en el sistema.", security = {})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    // Enviar correo de restablecimiento de contraseña
    @Operation(summary = "Enviar correo de restablecimiento de contraseña", description = "Enviar un correo electrónico para restablecer la contraseña del usuario.", security = {})    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendPasswordResetEmail(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        passwordResetTokenService.sendPasswordResetEmail(forgotPasswordRequest);
        return ResponseEntity.ok("Correo de restablecimiento de contraseña enviado.");
    }

    // Restablecer contraseña
    @Operation(summary = "Restablecer contraseña", description = "Restablecer la contraseña del usuario utilizando un token de restablecimiento.", security = {})
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordResetTokenService.resetPassword(passwordResetRequest);
        return ResponseEntity.ok("Contraseña restablecida correctamente.");
    }

    // Mostrar mensaje para restablecer la contraseña
    @Operation(summary = "Mostrar mensaje para restablecer la contraseña", description = "Mostrar un mensaje para restablecer la contraseña del usuario.", security = {})
    @GetMapping("/reset-password")
    public ResponseEntity<String> showResetPasswordMessage() {
        return ResponseEntity.ok("Restablecer la contraseña del usuario.");
    }

}
