package com.ds3.team8.users_service.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

class FeignClientInterceptorTest {
    private FeignClientInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new FeignClientInterceptor();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void apply_shouldAddHeadersFromRequest() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authenticated-User-Id")).thenReturn("123");
        when(mockRequest.getHeader("X-Authenticated-User-Role")).thenReturn("ADMIN");

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        RequestTemplate template = new RequestTemplate();

        // Act
        interceptor.apply(template);

        // Assert
        assertEquals("123", template.headers().get("X-Authenticated-User-Id").iterator().next());
        assertEquals("ADMIN", template.headers().get("X-Authenticated-User-Role").iterator().next());
    }

    @Test
    void apply_shouldNotAddHeadersIfAttributesAreNull() {
        // Arrange
        RequestContextHolder.resetRequestAttributes(); // Sin atributos
        RequestTemplate template = new RequestTemplate();

        // Act
        interceptor.apply(template);

        // Assert
        assertEquals(0, template.headers().size());
    }
}
