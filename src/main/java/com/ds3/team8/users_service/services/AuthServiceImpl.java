package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.repositories.IRoleRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {

    private IUserRepository userRepository;

    private IRoleRepository roleRepository;

    @Autowired
    public AuthServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public User register(User user) {
        // Verificar si el correo existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // Asignar el rol por defecto
        Role defaultRole = roleRepository.findByName("Cliente")
                .orElseThrow(() -> new RuntimeException("Rol Cliente no encontrado."));

        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        // Verificar si el correo existe
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas."));

        // Generar y devolver el token JWT
        return UUID.randomUUID().toString();
    }
}
