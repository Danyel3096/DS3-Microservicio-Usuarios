package com.ds3.team8.users_service.exceptions;

public class OrderVerificationException extends RuntimeException {
    public OrderVerificationException(String message) {
        super(message);
    }

    public OrderVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderVerificationException(Throwable cause) {
        super(cause);
    }
}
