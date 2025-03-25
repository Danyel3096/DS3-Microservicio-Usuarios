package com.ds3.team8.users_service.repositories;

import com.ds3.team8.users_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Obtener usuario por correo
}
