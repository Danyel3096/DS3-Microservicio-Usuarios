package com.ds3.team8.users_service.repositories;

import com.ds3.team8.users_service.entities.PasswordResetToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token); // Buscar token por su valor
}
