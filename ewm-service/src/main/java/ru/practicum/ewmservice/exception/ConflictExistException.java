package ru.practicum.ewmservice.exception;

public class ConflictExistException extends RuntimeException{
    public ConflictExistException(String message) {
        super(message);
    }
}
