package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.*;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import com.ds3.team8.users_service.exceptions.UnauthorizedException;
import com.ds3.team8.users_service.mappers.UserMapper;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.utils.JwtUtil;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements IAuthService {
     private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(IUserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse register(UserRequest userRequest) {
        // Verificar si el correo ya existe y el usuario está activo
        Optional<User> existingUser = userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Intento de registro con correo ya existente: {}", userRequest.getEmail());
            throw new BadRequestException("Correo ya registrado");
        }

        // Verificar si las contraseñas coinciden
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            logger.warn("Las contraseñas no coinciden para el correo: {}", userRequest.getEmail());
            throw new BadRequestException("Las contraseñas no coinciden");
        }
        
        // Crear el nuevo usuario
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado: {}", savedUser.getEmail());
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest authRequest) {
        // Verificar si el usuario existe y está activo
        Optional<User> userOptional = userRepository.findByEmailAndIsActiveTrue(authRequest.getEmail());
        if (userOptional.isEmpty()) {
            logger.warn("Intento de inicio de sesión con correo no registrado: {}", authRequest.getEmail());
            throw new UnauthorizedException("Correo o contraseña inválidos");
        }

        User user = userOptional.get();

        // Verificar la contraseña
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            logger.warn("Contraseña incorrecta para el correo: {}", authRequest.getEmail());
            throw new UnauthorizedException("Correo o contraseña inválidos");
        }

        // Generar el token JWT
        String token = jwtUtil.generateToken(user);
        logger.info("Usuario autenticado: {}", user.getEmail());
        return new AuthResponse(token);
    }
}
