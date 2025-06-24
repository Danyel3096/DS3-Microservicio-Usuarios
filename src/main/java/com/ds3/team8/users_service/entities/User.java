package com.ds3.team8.users_service.entities;

import java.time.LocalDateTime;

import com.ds3.team8.users_service.enums.Role;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity  // Indica que esta clase es una entidad JPA
@Table(name = "users")  // Nombre de la tabla en la base de datos
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName; // Nombre del usuario

    @Column(name = "last_name", nullable = false)
    private String lastName; // Apellido del usuario

    @Column(nullable = false, unique = true)
    private String email; // Correo del usuario

    @Column(nullable = false)
    private String password; // Contraseña del usuario

    @Column(nullable = false)
    private String phone; // Número de telefono del usuario

    @Column(nullable = false)
    private String address; // Dirección del usuario

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER; // Rol del usuario

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public User(String firstName, String lastName, String email, String phone, String address, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
}
