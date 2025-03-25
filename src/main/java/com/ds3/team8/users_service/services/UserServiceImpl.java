package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.RoleNotFoundException;
import com.ds3.team8.users_service.exceptions.UserAlreadyExistsException;
import com.ds3.team8.users_service.exceptions.UserNotFoundException;
import com.ds3.team8.users_service.repositories.IRoleRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private IUserRepository userRepository;

    private IRoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @Override
    public List<UserResponse> findAll() {
        // Obtener todos los usuarios
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Crear un usuario
    @Override
    public UserResponse save(UserRequest userRequest) {
        // Validar si ya existe el correo
        Optional<User> userWithSameEmail = userRepository.findByEmail(userRequest.getEmail());
        if (userWithSameEmail.isPresent() && userWithSameEmail.get().getIsActive()) {
            throw new UserAlreadyExistsException(userRequest.getEmail());
        }

        // Validar que el rol exista y esté activo
        Role role = roleRepository.findById(userRequest.getRoleId())
                .filter(Role::getIsActive)
                .orElseThrow(() -> new RoleNotFoundException(userRequest.getRoleId()));

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setIsActive(true); // Asegurar que el usuario esté activo por defecto
        user.setRole(role);

        // Guardar y devolver DTO
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    // Actualizar/Modificar un usuario
    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        return null;
    }

    // Buscar usuarios con paginación
    @Override
    public Page<UserResponse> findAllPageable(Pageable pageable) {
        return null;
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return convertToResponse(user);
    }

    @Override
    public void delete(Long id){
        // Buscar el usuario en la base de datos
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Cambiar el estado a inactivo
        existingUser.setIsActive(false);

        // Guardar los cambios en la base de datos
        userRepository.save(existingUser);
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



