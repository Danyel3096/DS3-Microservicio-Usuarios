package com.ds3.team8.users_service.controllers;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/users") // Indica la URL base para acceder a los servicios de esta clase
@RequiredArgsConstructor // Crea un constructor con los atributos final e inyecta dependencias sin @Autowired.
public class UserController {

    private final IUserService userService; //Importante "final" para que funcione el RequiredArgsConstructor

    // Obtener todos los usuarios
    @GetMapping("") // Se quita el "/" para evitar conflictos con rutas estáticas
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();

        // Validar si hay usuarios disponibles
        if (users.isEmpty()) {
            throw new RuntimeException("No hay usuarios disponibles");
        }

        return ResponseEntity.ok(users);
    }

    // Crear un usuario
    @PostMapping("/")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        User newUser = userService.save(user);
        response.put("message", "El usuario ha sido creado con éxito!");
        response.put("user", newUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Actualizar/Modificar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {      
        Map<String, Object> response = new HashMap<>();

        User updatedUser = userService.update(id, userRequest);
        response.put("message", "El usuario ha sido actualizado con éxito!");
        response.put("user", updatedUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}