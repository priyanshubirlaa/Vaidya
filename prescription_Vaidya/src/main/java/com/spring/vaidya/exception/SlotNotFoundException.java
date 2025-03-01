package com.spring.vaidya.exception;

public class SlotNotFoundException extends RuntimeException {
    public SlotNotFoundException(Long id) {
        super("Slot with ID " + id + " not found.");
    }
}
