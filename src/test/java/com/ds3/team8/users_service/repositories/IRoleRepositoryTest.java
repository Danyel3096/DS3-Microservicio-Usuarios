package com.ds3.team8.users_service.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ds3.team8.users_service.enums.Role;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IRoleRepositoryTest {
    @Autowired
    private IRoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll(); // Limpia la base de datos antes de cada prueba
    }

    @Test
    void testSaveRole() {
        // Guardar una role
        Role role = roleRepository.save(new Role(null, "Vendedor2", true, List.of()));

        // Verificar que se guardó correctamente
        assertNotNull(role.getId());
        assertEquals("Vendedor2", role.getName());
        assertTrue(role.getIsActive());
        // Verificar que la role se encuentra en la base de datos
        Optional<Role> savedRole = roleRepository.findById(role.getId());
        assertTrue(savedRole.isPresent());
        assertEquals("Vendedor2", savedRole.get().getName());
    }

    @Test
    void testFindByName() {
        // Guardar una role
        roleRepository.save(new Role(null, "Cliente2", true, List.of()));

        // Buscar por nombre
        Optional<Role> foundRole = roleRepository.findByName("Cliente2");

        // Verificar que se encontró correctamente
        assertTrue(foundRole.isPresent());
        assertEquals("Cliente2", foundRole.get().getName());
    }

    @Test
    void testFindByIsActiveTrue() {
        // Guardar roles
        roleRepository.save(new Role(null, "Vendedor2", true, List.of()));
        roleRepository.save(new Role(null, "Cliente2", false, List.of()));

        // Buscar solo roles activas
        List<Role> activeRoles = roleRepository.findByIsActiveTrue();

        // Verificar que solo se devuelve la activa
        assertEquals(1, activeRoles.size());
        assertEquals("Vendedor2", activeRoles.get(0).getName());
    }
}
