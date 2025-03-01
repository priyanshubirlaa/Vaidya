package com.spring.vaidya.exception;

public class PrescriptionNotFoundException extends RuntimeException {
    public PrescriptionNotFoundException(Long id) {
        super("Prescription not found with ID: " + id);
    }
}
