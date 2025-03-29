package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.*;
import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.RoleNotFoundException;
import com.ds3.team8.users_service.exceptions.UserAlreadyExistsException;
import com.ds3.team8.users_service.repositories.IRoleRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    private IUserRepository userRepository;

    private IRoleRepository roleRepository;

    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    private final String DEFAULT_ROLE_NAME = "Cliente";

    public AuthServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        // Validar si ya existe el correo
        Optional<User> userWithSameEmail = userRepository.findByEmail(registerRequest.getEmail());
        if (userWithSameEmail.isPresent() && userWithSameEmail.get().getIsActive()) {
            throw new UserAlreadyExistsException(registerRequest.getEmail());
        }

        // Validar que el rol exista y esté activo
        Role role = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .filter(Role::getIsActive)
                .orElseThrow(() -> new RoleNotFoundException(DEFAULT_ROLE_NAME));

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setAddress(registerRequest.getAddress());
        user.setIsActive(true);
        user.setRole(role);

        // Guardar y devolver DTO
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest authRequest) {
        // Verificar si el correo y la contraseña son correctas
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        // Generar y devolver el token JWT
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthResponse(token);
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getIsActive(),
                user.getRole().getId()
        );
    }
}
