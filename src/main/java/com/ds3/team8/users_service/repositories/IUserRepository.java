package com.ds3.team8.users_service.repositories;

import com.ds3.team8.users_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIsActiveTrue(String email); // Obtener usuario por correo electrónico y activo
    Optional<User> findByIdAndIsActiveTrue(Long id); // Obtener usuario por ID y activo
    List<User> findAllByIsActiveTrue(); // Obtener todos los usuarios activos
    Page<User> findAllByIsActiveTrue(Pageable pageable); // Obtener todos los usuarios activos con paginación
}
