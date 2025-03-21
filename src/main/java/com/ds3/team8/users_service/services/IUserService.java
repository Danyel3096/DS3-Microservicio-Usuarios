package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.entities.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
public interface IUserService {
    List<User> findAll(); // Obtener todos los usuarios

    User save(User user); // Crear un usuario

    User update(Long id, UserRequest user); // Actualizar/Modificar un usuario

    Page<User> findAllPageable(Pageable pageable); // Obtener todos los usuarios
}
