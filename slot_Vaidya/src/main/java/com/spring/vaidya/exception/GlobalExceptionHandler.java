package com.spring.vaidya.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler to manage and return standardized error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DoctorNotFoundException and returns a 404 response.
     *
     * @param ex The DoctorNotFoundException.
     * @return A ResponseEntity containing the error message and details.
     */
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles SlotNotFoundException and returns a 404 response.
     *
     * @param ex The SlotNotFoundException.
     * @return A ResponseEntity containing the error message and details.
     */
    @ExceptionHandler(SlotNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSlotNotFoundException(SlotNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles SlotOverlapException and returns a 400 response.
     *
     * @param ex The SlotOverlapException.
     * @return A ResponseEntity containing the error message and details.
     */
    @ExceptionHandler(SlotOverlapException.class)
    public ResponseEntity<Map<String, Object>> handleSlotOverlapException(SlotOverlapException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException (e.g., invalid slot range format) and returns a 400 response.
     *
     * @param ex The IllegalArgumentException.
     * @return A ResponseEntity containing the error message and details.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all unexpected exceptions and returns a 500 response.
     *
     * @param ex The unexpected exception.
     * @return A ResponseEntity containing the error message and details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        return buildResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Utility method to build a structured JSON response for exceptions.
     *
     * @param message The error message.
     * @param status  The HTTP status code.
     * @return A ResponseEntity containing a structured JSON error response.
     */
    private ResponseEntity<Map<String, Object>> buildResponseEntity(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}
