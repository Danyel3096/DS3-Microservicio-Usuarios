package com.ds3.team8.users_service.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Builder  // Permite construir objetos usando el patrón Builder
@Entity  // Indica que esta clase es una entidad JPA
@Table(name = "users")  // Nombre de la tabla en la base de datos
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false, unique = true) // Relación 1 a 1 con la tabla roles
    private Role role;
}
