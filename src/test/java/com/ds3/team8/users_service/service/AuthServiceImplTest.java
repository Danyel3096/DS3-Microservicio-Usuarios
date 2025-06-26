package com.ds3.team8.users_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import com.ds3.team8.users_service.enums.Role;
import com.ds3.team8.users_service.exceptions.UnauthorizedException;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import com.ds3.team8.users_service.dtos.AuthRequest;
import com.ds3.team8.users_service.dtos.AuthResponse;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.mappers.UserMapper;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.services.AuthServiceImpl;
import com.ds3.team8.users_service.utils.JwtUtil;
import com.ds3.team8.users_service.entities.User;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setFirstName("Diego");
        userRequest.setLastName("Tolosa");
        userRequest.setEmail("diego@example.com");
        userRequest.setPassword("Test@1234");
        userRequest.setConfirmPassword("Test@1234");
        userRequest.setPhone("+573001112233");
        userRequest.setAddress("Calle 123");
        userRequest.setRole(Role.CUSTOMER);
    }

    @Test
    void register_success() {
        User user = new User("Diego", "Tolosa", "diego@example.com", "+573001112233", "Calle 123", Role.CUSTOMER);
        user.setPassword("hashed_password");

        when(userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(userRequest)).thenReturn(user);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(new UserResponse(1L, "Diego", "Tolosa", "diego@example.com", "+573001112233", "Calle 123", Role.CUSTOMER, null, null));

        UserResponse response = authService.register(userRequest);

        assertEquals("diego@example.com", response.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void register_fails_whenEmailAlreadyExists() {
        when(userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.register(userRequest));
        assertEquals("Correo ya registrado", exception.getMessage());
    }

    @Test
    void register_fails_whenPasswordsDoNotMatch() {
        userRequest.setConfirmPassword("otraClave");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.register(userRequest));
        assertEquals("Las contraseñas no coinciden", exception.getMessage());
    }

    @Test
    void login_success() {
        AuthRequest authRequest = new AuthRequest("diego@example.com", "Test@1234");
        User user = new User();
        user.setEmail("diego@example.com");
        user.setPassword("hashed_password");

        when(userRepository.findByEmailAndIsActiveTrue(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(authRequest);

        assertEquals("fake-jwt-token", response.getToken());
    }

    @Test
    void login_fails_whenUserNotFound() {
        AuthRequest authRequest = new AuthRequest("no@existe.com", "Test@1234");

        when(userRepository.findByEmailAndIsActiveTrue(authRequest.getEmail())).thenReturn(Optional.empty());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(authRequest));
        assertEquals("Correo o contraseña inválidos", exception.getMessage());
    }

    @Test
    void login_fails_whenPasswordIncorrect() {
        AuthRequest authRequest = new AuthRequest("diego@example.com", "wrongPassword");
        User user = new User();
        user.setPassword("hashed_password");

        when(userRepository.findByEmailAndIsActiveTrue(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(authRequest));
        assertEquals("Correo o contraseña inválidos", exception.getMessage());
    }   
}
