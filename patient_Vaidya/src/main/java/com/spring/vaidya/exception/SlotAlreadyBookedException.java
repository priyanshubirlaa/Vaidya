package com.spring.vaidya.exception;
public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
