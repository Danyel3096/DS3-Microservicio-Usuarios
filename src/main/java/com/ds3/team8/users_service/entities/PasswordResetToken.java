package com.ds3.team8.users_service.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity  // Indica que esta clase es una entidad JPA
@Table(name = "password_reset_tokens")  // Nombre de la tabla en la base de datos
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token; // Token de restablecimiento de contraseña

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Usuario asociado al token

    @Column(nullable = false)
    private LocalDateTime expiration; // Fecha de expiración del token

    public PasswordResetToken(String token, LocalDateTime expiration) {
        this.token = token;
        this.expiration = expiration;
    }
}
