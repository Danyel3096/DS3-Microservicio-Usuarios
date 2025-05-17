package com.ds3.team8.users_service.controllers;

import com.ds3.team8.users_service.dtos.*;
import com.ds3.team8.users_service.services.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/auth") // Indica la URL base para acceder a los servicios de esta clase
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService){
        this.authService = authService;
    }

    // Registrar usuario
    @Operation(summary = "Registrar un usuario", description = "Registrar un usuario en el sistema.", security = {})
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse savedUser = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Inicio de sesión de usuarios
    @Operation(summary = "Inicio de sesión de usuario", description = "Inicio de sesión de usuarios en el sistema.", security = {})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }
}
