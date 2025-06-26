package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;



import org.junit.jupiter.api.Test;

public class BadRequestExceptionTest {
    @Test
    void constructor_shouldSetMessageCorrectly() {
        String message = "Solicitud incorrecta";
        BadRequestException exception = new BadRequestException(message);

        assertEquals(message, exception.getMessage());
    }
}
