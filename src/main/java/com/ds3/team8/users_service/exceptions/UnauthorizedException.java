package com.ds3.team8.users_service.exceptions;

public class UnauthorizedException {
    public UnauthorizedException(String message){
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause){
        super(message, cause);
    }

    public UnauthorizedException(Throwable cause){
        super(cause);
    }
}
