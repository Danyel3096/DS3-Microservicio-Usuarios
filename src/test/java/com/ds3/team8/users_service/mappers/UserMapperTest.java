package com.ds3.team8.users_service.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.enums.Role;
import java.util.List;
import java.time.LocalDateTime;

public class UserMapperTest {
    private final UserMapper mapper = new UserMapper();

    @Test
    void toUserResponse_shouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Diego");
        user.setLastName("Tolosa");
        user.setEmail("diego@example.com");
        user.setPhone("+573001112233");
        user.setAddress("Calle 123");
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserResponse response = mapper.toUserResponse(user);

        assertNotNull(response);
        assertEquals("Diego", response.getFirstName());
        assertEquals("Tolosa", response.getLastName());
        assertEquals("diego@example.com", response.getEmail());
    }

    @Test
    void toUser_shouldMapRequestToEntity() {
        UserRequest request = new UserRequest();
        request.setFirstName("Diego");
        request.setLastName("Tolosa");
        request.setEmail("diego@example.com");
        request.setPhone("+573001112233");
        request.setAddress("Calle 123");
        request.setRole(Role.CUSTOMER);

        User user = mapper.toUser(request);

        assertNotNull(user);
        assertEquals("Diego", user.getFirstName());
        assertEquals(Role.CUSTOMER, user.getRole());
    }

    @Test
    void updateUser_shouldUpdateFields() {
        User user = new User();
        user.setFirstName("Antiguo");

        UserRequest request = new UserRequest();
        request.setFirstName("Nuevo");
        request.setLastName("Apellido");
        request.setEmail("nuevo@example.com");
        request.setPhone("123456789");
        request.setAddress("Calle Nueva");
        request.setRole(Role.ADMIN);

        User updated = mapper.updateUser(user, request);

        assertEquals("Nuevo", updated.getFirstName());
        assertEquals("ADMIN", updated.getRole().name());
    }

    @Test
    void toUserResponseList_shouldMapList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Diego");
        user1.setLastName("Tolosa");
        user1.setEmail("diego@example.com");
        user1.setPhone("123");
        user1.setAddress("Calle 1");
        user1.setRole(Role.CUSTOMER);

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Ana");
        user2.setLastName("Pérez");
        user2.setEmail("ana@example.com");
        user2.setPhone("456");
        user2.setAddress("Calle 2");
        user2.setRole(Role.CUSTOMER);

        List<UserResponse> responses = mapper.toUserResponseList(List.of(user1, user2));

        assertEquals(2, responses.size());
        assertEquals("Ana", responses.get(1).getFirstName());
    }
}
