package com.ds3.team8.users_service.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data  // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity  // Indica que esta clase es una entidad JPA
@Table(name = "roles")  // Nombre de la tabla en la base de datos
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Nombre del rol

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Relación 1 a muchos con la tabla usuarios
    private List<User> users;
}
