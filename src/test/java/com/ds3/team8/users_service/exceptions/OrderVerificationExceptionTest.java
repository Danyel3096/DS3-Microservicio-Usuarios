package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OrderVerificationExceptionTest {
    @Test
    void testConstructorWithMessage() {
        String message = "No se pudo verificar los pedidos";
        OrderVerificationException exception = new OrderVerificationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Causa");
        OrderVerificationException exception = new OrderVerificationException("Error", cause);

        assertEquals("Error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCauseOnly() {
        Throwable cause = new RuntimeException("Causa directa");
        OrderVerificationException exception = new OrderVerificationException(cause);

        assertEquals(cause, exception.getCause());
    }
}
