package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import com.ds3.team8.users_service.exceptions.NotFoundException;
import com.ds3.team8.users_service.mappers.UserMapper;
import com.ds3.team8.users_service.repositories.IRoleRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        // Obtener todos los usuarios activos
        List<User> users = userRepository.findAllByIsActiveTrue();
        // Mapear a DTOs
        return userMapper.toUserResponseList(users);
    }

    @Override
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        // Verificar si el correo ya existe y el usuario está activo
        Optional<User> existingUser = userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Correo ya registrado");
        }

        // Verificar si el rol existe
        Optional<Role> roleOptional = roleRepository.findByIdAndIsActiveTrue(userRequest.getRoleId());
        if (roleOptional.isEmpty()) {
            throw new NotFoundException("Rol no encontrado");
        }

        // Crear nuevo usuario
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(roleOptional.get());
        
        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);
        // Mapear a DTO
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserRequest userRequest) {
        // Verificar si el usuario existe
        Optional<User> existingUserOptional = userRepository.findByIdAndIsActiveTrue(id);
        if (existingUserOptional.isEmpty()) {
            throw new NotFoundException("Usuario no encontrado");
        }

        User existingUser = existingUserOptional.get();

        // Verificar si el correo ya existe y el usuario está activo
        Optional<User> existingEmailUser = userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(existingUser.getId())) {
            throw new BadRequestException("Correo ya registrado");
        }

        // Verificar si el rol existe
        Optional<Role> roleOptional = roleRepository.findByIdAndIsActiveTrue(userRequest.getRoleId());
        if (roleOptional.isEmpty()) {
            throw new NotFoundException("Rol no encontrado");
        }

        // Actualizar los campos del usuario
        existingUser.setFirstName(userRequest.getFirstName());
        existingUser.setLastName(userRequest.getLastName());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhone(userRequest.getPhone());
        existingUser.setAddress(userRequest.getAddress());
        existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        existingUser.setRole(roleOptional.get());

        // Guardar el usuario actualizado en la base de datos
        User updatedUser = userRepository.save(existingUser);
        // Mapear a DTO
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAllPageable(Pageable pageable) {
        // Obtener todos los usuarios activos con paginación
        Page<User> usersPage = userRepository.findAllByIsActiveTrue(pageable);
        // Mapear a DTOs
        return usersPage.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        // Verificar si el usuario existe
        Optional<User> userOptional = userRepository.findByIdAndIsActiveTrue(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Usuario no encontrado");
        }

        // Mapear a DTO
        return userMapper.toUserResponse(userOptional.get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Verificar si el usuario existe
        Optional<User> userOptional = userRepository.findByIdAndIsActiveTrue(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Usuario no encontrado");
        }

        // Marcar el usuario como inactivo
        User user = userOptional.get();
        user.setIsActive(false);
        userRepository.save(user);
    }
}