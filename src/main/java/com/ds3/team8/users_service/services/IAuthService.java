package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.*;

public interface IAuthService {
    UserResponse register(RegisterRequest registerRequest); // Registro de usuarios
    AuthResponse login(AuthRequest authRequest); // Inicio de sesión de usuarios
    UserResponse getAuthenticatedUser(); // Obtener información del usuario autenticado
}
