package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.entities.User;

public interface IAuthService {
    User register(User user); // Registro de usuarios
    String login(String email, String password); // Inicio de sesión de usuarios
}
