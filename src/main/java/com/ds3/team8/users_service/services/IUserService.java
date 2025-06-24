package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface IUserService {
    List<UserResponse> findAll(); // Obtener todos los usuarios
    UserResponse save(UserRequest userRequest); // Crear un usuario
    UserResponse update(Long id, UserRequest userRequest); // Actualizar un usuario
    Page<UserResponse> findAllPageable(Pageable pageable); // Obtener todos los usuarios
    UserResponse findById(Long id); // Obtener un usuario por su id
    void delete(Long id); // Eliminar un usuario por su id
}

