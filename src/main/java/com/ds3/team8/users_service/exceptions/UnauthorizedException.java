package com.ds3.team8.users_service.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message){
        super(message);
    }
}
