package com.ds3.team8.users_service.mappers;

import com.ds3.team8.users_service.dtos.UserRequest;
import com.ds3.team8.users_service.dtos.UserResponse;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.enums.Role;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toUser(UserRequest userRequest) {
        if (userRequest == null) return null;

        return new User(
            userRequest.getFirstName(),
            userRequest.getLastName(),
            userRequest.getEmail(),
            userRequest.getPhone(),
            userRequest.getAddress(),
            userRequest.getRole() != null ? userRequest.getRole() : Role.CUSTOMER
        );
    }

    public User updateUser(User user, UserRequest userRequest) {
        if (user == null || userRequest == null) return null;

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setRole(userRequest.getRole() != null ? userRequest.getRole() : Role.CUSTOMER);

        return user;
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        if (users == null) return List.of();
        if (users.isEmpty()) return List.of();

        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }
}
