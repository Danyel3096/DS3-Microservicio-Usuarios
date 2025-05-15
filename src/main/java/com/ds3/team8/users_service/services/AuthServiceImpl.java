package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.*;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import com.ds3.team8.users_service.exceptions.UnauthorizedException;
import com.ds3.team8.users_service.mappers.UserMapper;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.utils.JwtUtil;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(IUserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        // Verificar si el correo ya existe y el usuario está activo
        Optional<User> existingUser = userRepository.findByEmailAndIsActiveTrue(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Correo ya registrado");
        }

        // Crear el nuevo usuario
        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        // Verificar si el usuario existe y está activo
        Optional<User> userOptional = userRepository.findByEmailAndIsActiveTrue(authRequest.getEmail());
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("Correo o contraseña inválidos");
        }
        User user = userOptional.get();
        // Verificar la contraseña
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Correo o contraseña inválidos");
        }
        // Generar el token JWT
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}
