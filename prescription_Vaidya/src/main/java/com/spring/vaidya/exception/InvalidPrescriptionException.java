package com.spring.vaidya.exception;

public class InvalidPrescriptionException extends RuntimeException {
    public InvalidPrescriptionException(String message) {
        super(message);
    }
}
