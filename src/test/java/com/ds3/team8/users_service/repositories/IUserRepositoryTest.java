package com.ds3.team8.users_service.repositories;

import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IUserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();  // Limpia la base de datos antes de cada prueba
        roleRepository.deleteAll();

        // Crear y guardar un role antes de cada prueba
        role = roleRepository.save(new Role(null, "Admin", true, new ArrayList<>()));
    }

    @Test
    void testSaveUser() {
        // Crear y guardar un usuario
        User user = new User(null, "Master", "Russó", "kisamastodos@madrerusia.rs", "Clave123", "0987654321", "Communist Address", true, role);
        User savedUser = userRepository.save(user);

        // Verificar que se guardó correctamente
        assertNotNull(savedUser.getId());
        assertEquals("Master", savedUser.getFirstName());
        assertTrue(savedUser.getIsActive());
    }

    @Test
    void testFindById() {
        // Guardar un usuario
        User user = userRepository.save(new User(null, "Líder", "Chambeando con la Frenelli", "onlyblack@abrazopublico.tulua", "Clave123", "1234567890", "Cueros Fan", true, role));

        // Buscar por ID
        Optional<User> foundUser = userRepository.findById(user.getId());

        // Verificar que el usuario se encontró
        assertTrue(foundUser.isPresent());
        assertEquals("Líder", foundUser.get().getFirstName());
    }

    @Test
    void testFindAll() {
        // Guardar varios usuarios
        userRepository.save(new User(null, "Master", "Russó", "kisamastodos@madrerusia.rs", "Clave123", "0987654321", "Communist Address", true, role));
        userRepository.save(new User(null, "Líder", "Chambeando con la Frenelli", "onlyblack@abrazopublico.tulua", "Clave123", "1234567890", "Cueros Fan", true, role));

        // Obtener todos los usuarios
        List<User> users = userRepository.findAll();

        // Verificar que se guardaron 2 usuarios
        assertEquals(2, users.size());
    }
}