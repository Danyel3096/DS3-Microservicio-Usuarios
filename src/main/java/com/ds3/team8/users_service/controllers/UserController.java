package com.ds3.team8.users_service.controllers;


import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
