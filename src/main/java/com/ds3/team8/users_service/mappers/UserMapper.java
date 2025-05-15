package com.ds3.team8.users_service.mappers;

import com.ds3.team8.users_service.dtos.RegisterRequest;
import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.entities.Role;
import com.ds3.team8.users_service.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // Método para convertir un objeto User a un objeto UserResponse
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole()
        );
    }

    // Método para convertir un objeto UserRequest a un objeto User
    public User toUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setRole(userRequest.getRole());
        return user;
    }

    // Método para convertir un objeto RegisterRequest a un objeto User
    public User toUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setAddress(registerRequest.getAddress());
        user.setRole(Role.CLIENT);
        return user;
    }

    // Método para convertir una lista de objetos User a una lista de objetos UserResponse
    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
