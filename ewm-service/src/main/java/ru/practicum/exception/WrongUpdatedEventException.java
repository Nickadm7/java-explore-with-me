package ru.practicum.exception;

public class WrongUpdatedEventException extends RuntimeException {
    public WrongUpdatedEventException(String message) {
        super(message);
    }
}
