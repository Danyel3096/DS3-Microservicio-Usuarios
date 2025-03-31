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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1L);
        role.setIsActive(true);

        user = new User();
        user.setId(1L);
        user.setFirstName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan@example.com");
        user.setPassword("password123");
        user.setPhone("123456789");
        user.setAddress("Calle Falsa 123");
        user.setIsActive(true);
        user.setRole(role);

        userRequest = new UserRequest();
        userRequest.setFirstName("Juan");
        userRequest.setLastName("Perez");
        userRequest.setEmail("juan@example.com");
        userRequest.setPassword("password123");
        userRequest.setPhone("123456789");
        userRequest.setAddress("Calle Falsa 123");
        userRequest.setRoleId(1L);
    }

    @Test
    void findAll_ReturnsUserList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> users = userService.findAll();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Juan", users.get(0).getFirstName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void save_WhenEmailExists_ThrowsException() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.save(userRequest));

        verify(userRepository, times(1)).findByEmail(userRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void save_WhenRoleDoesNotExist_ThrowsException() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(userRequest.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.save(userRequest));

        verify(roleRepository, times(1)).findById(userRequest.getRoleId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void save_SuccessfullyCreatesUser() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(userRequest.getRoleId())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.save(userRequest);

        assertNotNull(response);
        assertEquals("Juan", response.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findById_WhenUserExists_ReturnsUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.findById(1L);

        assertNotNull(response);
        assertEquals("Juan", response.getFirstName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenUserDoesNotExist_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void delete_UserExists_SetsInactive() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        assertFalse(user.getIsActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findAllPageable_ReturnsPagedUsers() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<UserResponse> responsePage = userService.findAllPageable(Pageable.unpaged());

        assertFalse(responsePage.isEmpty());
        assertEquals(1, responsePage.getTotalElements());

        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }
}

