package com.spring.vaidya.exception;

public class DuplicatePrescriptionException extends RuntimeException {
    public DuplicatePrescriptionException(String message) {
        super(message);
    }
}
