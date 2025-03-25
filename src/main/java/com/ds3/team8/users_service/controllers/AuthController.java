package com.ds3.team8.users_service.controllers;

import com.ds3.team8.users_service.dtos.*;
import com.ds3.team8.users_service.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/auth") // Indica la URL base para acceder a los servicios de esta clase
public class AuthController {

    private IAuthService authService;

    public AuthController(IAuthService authService){
        this.authService = authService;
    }

    // Registrar usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();

        // Crear usuario
        UserResponse newUser = authService.register(registerRequest);
        response.put("message", "El usuario ha sido creado con éxito!");
        response.put("user", newUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Inicio de sesión de usuarios
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }
}
