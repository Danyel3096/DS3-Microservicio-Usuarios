package com.ds3.team8.users_service.controllers;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.services.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/users") // Indica la URL base para acceder a los servicios de esta clase
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    // Obtener todos los usuarios
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Crear un usuario
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse savedUser = userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Actualizar un usuario
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.update(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // Buscar usuarios con paginación
    // Cambia la URL a algo como /api/v1/users/pageable?page=0&size=10&sort=firstName,asc
    // Para el sort= se puede usar cualquier atributo de la entidad User
    // Por ejemplo para ordenar por apellido desde la Z a la A sería sort=lastName,desc
    // Para el caso de roles se puede usar sort=role.name,asc (sort=entity.attribute,asc o desc)
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/pageable")
    public Page<UserResponse> findAllPageable(Pageable pageable) {
        return userService.findAllPageable(pageable);
    }

    // Obtener un Usuario por ID
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // Eliminación lógica de un usuario
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}