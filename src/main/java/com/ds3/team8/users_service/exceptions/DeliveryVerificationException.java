package com.ds3.team8.users_service.exceptions;

public class DeliveryVerificationException extends RuntimeException {
    public DeliveryVerificationException(String message) {
        super(message);
    }

    public DeliveryVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeliveryVerificationException(Throwable cause) {
        super(cause);
    }
}
