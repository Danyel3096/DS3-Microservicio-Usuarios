package com.ds3.team8.users_service.exceptions;

public class UserHasDeliveriesException extends RuntimeException {
    public UserHasDeliveriesException(String message) {
        super(message);
    }

    public UserHasDeliveriesException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasDeliveriesException(Throwable cause) {
        super(cause);
    }
}
