package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserHasDeliveriesExceptionTest {
    @Test
    void testConstructorWithMessage() {
        String message = "El usuario tiene entregas asociadas";
        UserHasDeliveriesException exception = new UserHasDeliveriesException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Causa");
        UserHasDeliveriesException exception = new UserHasDeliveriesException("Error", cause);

        assertEquals("Error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCauseOnly() {
        Throwable cause = new RuntimeException("Causa directa");
        UserHasDeliveriesException exception = new UserHasDeliveriesException(cause);

        assertEquals(cause, exception.getCause());
    }
}
