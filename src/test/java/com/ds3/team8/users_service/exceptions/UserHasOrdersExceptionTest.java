package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserHasOrdersExceptionTest {
    @Test
    void testConstructorWithMessage() {
        String message = "Usuario tiene pedidos activos";
        UserHasOrdersException exception = new UserHasOrdersException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Causa");
        UserHasOrdersException exception = new UserHasOrdersException("Error", cause);

        assertEquals("Error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCauseOnly() {
        Throwable cause = new RuntimeException("Causa");
        UserHasOrdersException exception = new UserHasOrdersException(cause);

        assertEquals(cause, exception.getCause());
    }
}
