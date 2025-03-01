package com.example.vaidya.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.vaidya.entity.ErrorResponse;

/**
 * 📌 Global Exception Handler
 * - Centralized error handling for the application.
 * - Catches and processes exceptions uniformly.
 * - Logs errors for debugging and monitoring.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * ✅ Handles UserNotFoundException
     * - Triggered when a requested user is not found.
     * - Returns HTTP 404 (Not Found) with a meaningful error response.
     *
     * @param ex The thrown UserNotFoundException.
     * @return Structured error response with HTTP 404 status.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * ✅ Handles All Other Exceptions (Global Handler)
     * - Catches any unexpected exceptions that occur in the application.
     * - Logs the error details for debugging.
     * - Returns HTTP 500 (Internal Server Error) with a generic error message.
     *
     * @param ex The thrown Exception.
     * @return Structured error response with HTTP 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return buildErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 🔹 Helper Method to Build a Standardized Error Response
     * - Creates a structured JSON response for errors.
     * - Includes timestamp, HTTP status, reason, and message.
     * - Logs the error response before returning it.
     *
     * @param message The error message to be returned.
     * @param status  The HTTP status code.
     * @return ResponseEntity containing an ErrorResponse object.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Current timestamp
                status.value(),       // HTTP status code
                status.getReasonPhrase(), // Status description
                message               // Error message
        );

        logger.error("Error Response: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, status);
    }
}
