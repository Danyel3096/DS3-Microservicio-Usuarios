package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.client.DeliveryClient;
import com.ds3.team8.users_service.client.OrderClient;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.enums.Role;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import com.ds3.team8.users_service.exceptions.NotFoundExceptionTest;
import com.ds3.team8.users_service.mappers.UserMapper;
import com.ds3.team8.users_service.repositories.IUserRepository;

import feign.FeignException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OrderClient orderClient;
    private final DeliveryClient deliveryClient;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(IUserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, 
                           OrderClient orderClient, DeliveryClient deliveryClient) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.orderClient = orderClient;
        this.deliveryClient = deliveryClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        // Obtener todos los usuarios activos
        List<User> users = userRepository.findAllByIsActiveTrue();
        if (users.isEmpty()) {
            logger.warn("No se encontraron usuarios activos");
            throw new NotFoundExceptionTest("No se encontraron usuarios activos");
        }
        // Mapear a DTOs
        logger.info("Número de usuarios activos encontrados: {}", users.size());
        return userMapper.toUserResponseList(users);
    }

    @Override
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        // Verificar si el correo ya existe y el usuario está activo
        Optional<User> existingUser = userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Intento de registro con correo ya existente: {}", userRequest.getEmail());
            throw new BadRequestException("Correo ya registrado");
        }
        // Crear nuevo usuario
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado: {}", savedUser.getEmail());
        // Mapear a DTO
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserRequest userRequest) {
        // Verificar si el usuario existe
        Optional<User> existingUserOptional = userRepository.findByIdAndIsActiveTrue(id);
        if (existingUserOptional.isEmpty()) {
            logger.warn("Intento de actualización de usuario con ID no encontrado: {}", id);
            throw new NotFoundExceptionTest("Usuario no encontrado");
        }

        User existingUser = existingUserOptional.get();
        // Verificar si el correo ya existe y el usuario está activo
        Optional<User> existingEmailUser = userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(existingUser.getId())) {
            logger.warn("Intento de actualización con correo ya existente: {}", userRequest.getEmail());
            throw new BadRequestException("Correo ya registrado");
        }

        User user = userMapper.updateUser(existingUser, userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // Guardar el usuario actualizado en la base de datos
        User updatedUser = userRepository.save(user);
        logger.info("Usuario actualizado: {}", updatedUser.getEmail());
        // Mapear a DTO
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAllPageable(Pageable pageable) {
        // Obtener todos los usuarios activos con paginación
        Page<User> usersPage = userRepository.findAllByIsActiveTrue(pageable);
        if (usersPage.isEmpty()) {
            logger.warn("No se encontraron usuarios activos en la paginación");
            throw new NotFoundExceptionTest("No se encontraron usuarios activos");
        }
        // Mapear a DTOs
        logger.info("Número de usuarios activos encontrados (paginados): {}", usersPage.getTotalElements());
        return usersPage.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        // Verificar si el usuario existe
        Optional<User> userOptional = userRepository.findByIdAndIsActiveTrue(id);
        if (userOptional.isEmpty()) {
            logger.warn("Intento de búsqueda de usuario con ID no encontrado: {}", id);
            throw new NotFoundExceptionTest("Usuario no encontrado");
        }
        // Mapear a DTO
        logger.info("Usuario encontrado con ID: {}", id);
        return userMapper.toUserResponse(userOptional.get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Verificar si el usuario existe
        Optional<User> userOptional = userRepository.findByIdAndIsActiveTrue(id);
        if (userOptional.isEmpty()) {
            logger.warn("Intento de eliminación de usuario con ID no encontrado: {}", id);
            throw new NotFoundExceptionTest("Usuario no encontrado");
        }
        User user = userOptional.get();

        // Verificar si el usuario tiene pedidos asociados
        validateUserHasNoOrders(id);

        // Verificar si el usuario es repartidor y tiene entregas asociadas
        if(Role.DRIVER.equals(user.getRole())) {
            validateUserHasNoDeliveries(id);
        }

        // Marcar el usuario como inactivo
        user.setIsActive(false);
        logger.info("Usuario marcado como inactivo: {}", user.getEmail());
        userRepository.save(user);
    }

    private void validateUserHasNoOrders(Long userId) {
        try {
            if (orderClient.userHasOrders(userId)) {
                logger.warn("Intento de eliminación de usuario con pedidos asociados: {}", userId);
                throw new RuntimeException("No se puede eliminar el usuario porque tiene pedidos asociados");
            }
        } catch (FeignException e) {
            logger.error("Error al verificar los pedidos del usuario con ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("No se pudo verificar los pedidos del usuario. Intente más tarde.");
        }
    }

    private void validateUserHasNoDeliveries(Long userId) {
        try {
            if (deliveryClient.userHasDeliveries(userId)) {
                logger.warn("Intento de eliminación de usuario repartidor con entregas asociadas: {}", userId);
                throw new RuntimeException("No se puede eliminar el usuario repartidor porque tiene entregas asociadas");
            }
        } catch (FeignException e) {
            logger.error("Error al verificar las entregas del usuario con ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("No se pudo verificar las entregas del usuario. Intente más tarde.");
        }
    }
}



