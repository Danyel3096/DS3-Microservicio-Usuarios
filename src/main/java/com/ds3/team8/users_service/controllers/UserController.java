package com.ds3.team8.users_service.controllers;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.enums.Role;
import com.ds3.team8.users_service.services.IUserService;
import com.ds3.team8.users_service.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/users") // Indica la URL base para acceder a los servicios de esta clase
@Tag(name = "Usuarios", description = "Endpoints para usuarios")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    // Obtener todos los usuarios
    @Operation(summary = "Obtener todos los usuarios", description = "Obtener todos los usuarios del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        return ResponseEntity.ok(userService.findAll());
    }

    // Crear un usuario
    @Operation(summary = "Guardar un usuario", description = "Guardar un usuario en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PostMapping
    public ResponseEntity<UserResponse> saveUser(
        @Valid @RequestBody UserRequest userRequest,
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        UserResponse savedUser = userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Actualizar un usuario
    @Operation(summary = "Actualizar un usuario", description = "Actualizar un usuario en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        UserResponse updatedUser = userService.update(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // Buscar usuarios con paginación
    // Ejemplo URL /api/v1/users/pageable?page=0&size=8
    @Operation(summary = "Obtener los usuarios con paginación", description = "Obtener los usuarios con paginación del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/pageable")
    public Page<UserResponse> findAllPageable(
        Pageable pageable,
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        return userService.findAllPageable(pageable);
    }

    // Obtener un Usuario por ID
    @Operation(summary = "Obtener un usuario", description = "Obtener un usuario por su id del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // Eliminación lógica de un usuario
    @Operation(summary = "Eliminar un usuario", description = "Eliminar un usuario por su id en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
        @PathVariable Long id,
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}