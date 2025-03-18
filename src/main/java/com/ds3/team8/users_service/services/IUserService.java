package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.entities.User;

import java.util.List;

public interface IUserService {
    List<User> findAll(); // Obtener todos los usuarios
}
