package com.ds3.team8.users_service.repositories;

import com.ds3.team8.users_service.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameAndIsActiveTrue(String name); // Buscar rol por nombre y activo
    Optional<Role> findByIdAndIsActiveTrue(Long id); // Buscar rol por ID y activo
}