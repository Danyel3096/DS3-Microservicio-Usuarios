package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DeliveryVerificationExceptionTest {
    @Test
    void testConstructorWithMessage() {
        String message = "Error al verificar entregas del usuario";
        DeliveryVerificationException exception = new DeliveryVerificationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Causa específica");
        DeliveryVerificationException exception = new DeliveryVerificationException("Error", cause);

        assertEquals("Error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCauseOnly() {
        Throwable cause = new RuntimeException("Causa directa");
        DeliveryVerificationException exception = new DeliveryVerificationException(cause);

        assertEquals(cause, exception.getCause());
    }
}
