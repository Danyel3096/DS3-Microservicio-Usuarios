package com.ds3.team8.users_service.dtos;

import com.ds3.team8.users_service.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Boolean isActive;
    private Role role;
}
