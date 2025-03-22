package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.UserNotFoundException;
import com.ds3.team8.users_service.repositories.IRoleRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor // Crea un constructor con los atributos final e inyecta dependencias sin @Autowired.
public class UserServiceImpl implements IUserService {
    //Importante los "final" de aquí para que funcione el RequiredArgsConstructor
    private final IUserRepository userRepository;

    private final IRoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Crear un usuario
    @Override
    @Transactional
    public User save(User user) {
        // Verificar si el correo existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // Obtener el rol y verificar si existe
        Role role = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new RuntimeException("El rol especificado no existe"));    

        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Actualizar/Modificar un usuario
    @Override
    @Transactional
    public User update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar los campos si los valores no son nulos
        if (userRequest.getFirstName() != null) user.setFirstName(userRequest.getFirstName());
        if (userRequest.getLastName() != null) user.setLastName(userRequest.getLastName());   
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null) user.setPassword(userRequest.getPassword());   
        if (userRequest.getPhone() != null) user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) user.setAddress(userRequest.getAddress());      
        if (userRequest.getIsActive() != null) user.setIsActive(userRequest.getIsActive());   
        if (userRequest.getRole() != null) user.setRole(userRequest.getRole());

        return userRepository.save(user);
    }

    // Buscar usuarios con paginación
    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return convertToResponse(user);
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



