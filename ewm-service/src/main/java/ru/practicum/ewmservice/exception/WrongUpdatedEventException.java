package ru.practicum.ewmservice.exception;

public class WrongUpdatedEventException extends RuntimeException {
    public WrongUpdatedEventException(String message) {
        super(message);
    }
}
