package com.ds3.team8.users_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Pageable;

import com.ds3.team8.users_service.client.DeliveryClient;
import com.ds3.team8.users_service.client.OrderClient;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.mappers.UserMapper;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.services.UserServiceImpl;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.enums.Role;
import com.ds3.team8.users_service.exceptions.NotFoundException;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OrderClient orderClient;

    @Mock
    private DeliveryClient deliveryClient;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("diego@example.com");
        user.setIsActive(true);
        user.setRole(Role.CUSTOMER);
        user.setPassword("encrypted");

        userRequest = new UserRequest();
        userRequest.setEmail("diego@example.com");
        userRequest.setPassword("password");

        userResponse = new UserResponse();
        userResponse.setEmail("diego@example.com");
    }

    private UserRequest buildUserRequest() {
        UserRequest request = new UserRequest();
        request.setEmail("diego@example.com");
        request.setPassword("password");
        return request;
    }

    @Test
    void findAll_success() {
        List<User> users = List.of(user);
        List<UserResponse> responses = List.of(userResponse);

        when(userRepository.findAllByIsActiveTrue()).thenReturn(users);
        when(userMapper.toUserResponseList(users)).thenReturn(responses);

        List<UserResponse> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("diego@example.com", result.get(0).getEmail());
        verify(userRepository).findAllByIsActiveTrue();
    }

    @Test
    void findAll_fails_whenEmpty() {
        when(userRepository.findAllByIsActiveTrue()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> userService.findAll());
        verify(userRepository).findAllByIsActiveTrue();
    }

    @Test
    void save_fails_whenEmailExists() {
        UserRequest request = buildUserRequest();
        when(userRepository.findByEmailAndIsActiveTrue(request.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.save(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_success() {
        when(userRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail())).thenReturn(Optional.of(user)); // mismo usuario
        when(userMapper.updateUser(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.update(1L, userRequest);

        assertNotNull(result);
        assertEquals("diego@example.com", result.getEmail());
    }

    @Test
    void update_fails_whenUserNotFound() {
        when(userRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(1L, userRequest));
    }

    @Test
    void update_fails_whenEmailExistsForAnotherUser() {
        User anotherUser = new User();
        anotherUser.setId(2L); // diferente al que se quiere actualizar

        when(userRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailAndIsActiveTrue(userRequest.getEmail())).thenReturn(Optional.of(anotherUser));

        assertThrows(BadRequestException.class, () -> userService.update(1L, userRequest));
    }

    @Test
    void findAllPageable_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user));

        when(userRepository.findAllByIsActiveTrue(pageable)).thenReturn(userPage);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.findAllPageable(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("diego@example.com", result.getContent().get(0).getEmail());
    }

    @Test
    void delete_success() {
        when(userRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        assertFalse(user.getIsActive());
        verify(userRepository).save(user);
    }

    @Test
    void delete_fails_whenUserNotFound() {
        when(userRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.delete(1L));
    }
}
