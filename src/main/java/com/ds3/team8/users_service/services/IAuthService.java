package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.AuthRequest;
import com.ds3.team8.users_service.dtos.AuthResponse;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;

public interface IAuthService {
     UserResponse register(UserRequest userRequest); // Registro de usuarios
    AuthResponse login(AuthRequest authRequest); // Inicio de sesión de usuarios
}
