package com.ds3.team8.users_service.exceptions;

public class UserHasOrdersException extends RuntimeException {
    public UserHasOrdersException(String message) {
        super(message);
    }

    public UserHasOrdersException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasOrdersException(Throwable cause) {
        super(cause);
    }
}
