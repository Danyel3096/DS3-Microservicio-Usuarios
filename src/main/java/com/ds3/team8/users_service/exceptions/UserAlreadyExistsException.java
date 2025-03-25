package com.ds3.team8.users_service.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("El usuario con correo '" + email + "' ya existe.");
    }
}
