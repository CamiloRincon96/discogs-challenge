package com.clara.discogschallenge.infrastructure.exception;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntimeExceptionShouldReturnBadRequestWithMessage() {
        String errorMessage = "Test error message";
        RuntimeException exception = new RuntimeException(errorMessage);

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(exception);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().get("error"));
    }
} 