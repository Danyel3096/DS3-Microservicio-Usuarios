package com.ds3.team8.users_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds3.team8.users_service.enums.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // Obtener rol por nombre

    // metodo para encontrar activas
    List<Role> findByIsActiveTrue();
}
