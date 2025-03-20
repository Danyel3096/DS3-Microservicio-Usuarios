package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.repositories.IRoleRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private IUserRepository userRepository;

    private IRoleRepository roleRepository;

    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        // Verificar si el correo y la contraseña son correctas
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // Generar y devolver el token JWT
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return token;
    }
}
