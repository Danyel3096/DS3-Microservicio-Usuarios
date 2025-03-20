package com.ds3.team8.users_service.controllers;


import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/users") // Indica la URL base para acceder a los servicios de esta clase
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    // Obtener todos los usuarios
    @GetMapping("/")
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
}
