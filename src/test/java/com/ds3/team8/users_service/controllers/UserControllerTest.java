package com.ds3.team8.users_service.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.services.IUserService;
import com.ds3.team8.users_service.enums.Role;
import com.ds3.team8.users_service.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;



public class UserControllerTest {
    private IUserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(IUserService.class);
        userController = new UserController(userService);
    }

    private UserResponse createMockUserResponse() {
        return new UserResponse(1L, "Diego", "Tolosa", "diego@example.com", "+573001112233", "Calle 123", Role.CUSTOMER, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        try (MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {
            mockedSecurity.when(() -> SecurityUtil.validateRole("ADMIN", Role.ADMIN)).thenAnswer(invocation -> null);

            List<UserResponse> mockList = List.of(createMockUserResponse());
            when(userService.findAll()).thenReturn(mockList);

            ResponseEntity<List<UserResponse>> response = userController.getAllUsers("ADMIN");

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void saveUser_shouldReturnCreatedUser() {
        try (MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {
            mockedSecurity.when(() -> SecurityUtil.validateRole("ADMIN", Role.ADMIN)).thenAnswer(invocation -> null);

            UserRequest request = new UserRequest();
            request.setFirstName("Diego");
            request.setLastName("Tolosa");
            request.setEmail("diego@example.com");
            request.setPhone("+573001112233");
            request.setAddress("Calle 123");

            UserResponse responseMock = createMockUserResponse();
            when(userService.save(request)).thenReturn(responseMock);

            ResponseEntity<UserResponse> response = userController.saveUser(request, "ADMIN");

            assertEquals(201, response.getStatusCodeValue());
            assertEquals("Diego", response.getBody().getFirstName());
        }
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        try (MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {
            mockedSecurity.when(() -> SecurityUtil.validateRole("ADMIN", Role.ADMIN)).thenAnswer(invocation -> null);

            UserRequest request = new UserRequest();
            request.setFirstName("Nuevo");
            request.setLastName("Apellido");
            request.setEmail("nuevo@example.com");

            UserResponse updatedResponse = createMockUserResponse();
            updatedResponse.setFirstName("Nuevo");
            updatedResponse.setEmail("nuevo@example.com");

            when(userService.update(1L, request)).thenReturn(updatedResponse);

            ResponseEntity<UserResponse> result = userController.updateUser(1L, request, "ADMIN");

            assertEquals(200, result.getStatusCodeValue());
            assertEquals("Nuevo", result.getBody().getFirstName());
        }
    }

    @Test
    void findAllPageable_shouldReturnPagedUsers() {
        try (MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {
            mockedSecurity.when(() -> SecurityUtil.validateRole("ADMIN", Role.ADMIN)).thenAnswer(invocation -> null);

            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<UserResponse> page = new PageImpl<>(List.of(createMockUserResponse()));

            when(userService.findAllPageable(pageRequest)).thenReturn(page);

            Page<UserResponse> result = userController.findAllPageable(pageRequest, "ADMIN");

            assertEquals(1, result.getContent().size());
        }
    }

    @Test
    void getUserById_shouldReturnUser() {
        UserResponse mockUser = createMockUserResponse();

        when(userService.findById(1L)).thenReturn(mockUser);

        ResponseEntity<UserResponse> result = userController.getUserById(1L);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Diego", result.getBody().getFirstName());
    }

    @Test
    void deleteUser_shouldReturnNoContent() {
        try (MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {
            mockedSecurity.when(() -> SecurityUtil.validateRole("ADMIN", Role.ADMIN)).thenAnswer(invocation -> null);

            doNothing().when(userService).delete(1L);

            ResponseEntity<Void> result = userController.deleteUser(1L, "ADMIN");

            assertEquals(204, result.getStatusCodeValue());
        }
    }
}
